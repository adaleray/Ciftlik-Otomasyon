package main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PerAra extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBox;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtArama;

    private String url = "jdbc:postgresql://localhost:5432/farm";
    private String username = "postgres";
    private String password = "123";
    private Connection con = null;

    // Önbellek için map tanımlayalım
    private Map<String, String> searchQueries;

    public PerAra() {
        setTitle("Personel Ara");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));
		setTitle("Çiftlik Otomasyonu");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 804, 424);


        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"ad", "soyad", "departman_adi"}));
        comboBox.setBounds(106, 24, 97, 21);
        panel.add(comboBox);

        JLabel lblNewLabel = new JLabel("Arama Türü:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(8, 27, 90, 13);
        panel.add(lblNewLabel);

        txtArama = new JTextField();
        txtArama.setBounds(215, 24, 150, 21);
        panel.add(txtArama);

        JButton arabutton = new JButton("Ara");
        arabutton.setBounds(375, 24, 81, 21);
        arabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        panel.add(arabutton);

        JButton yenilebutton = new JButton("Yenile");
        yenilebutton.setBounds(466, 24, 81, 21);
        yenilebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtArama.setText("");
                refreshTable();
            }
        });
        panel.add(yenilebutton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 80, 741, 400);
        panel.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel();
        table.setModel(tableModel);
        tableModel.addColumn("TC");
        tableModel.addColumn("İsim");
        tableModel.addColumn("Soyad");
        tableModel.addColumn("Telefon");
        tableModel.addColumn("Mail");
        tableModel.addColumn("Cinsiyet");
        tableModel.addColumn("Maaş");
        tableModel.addColumn("Deneyim Süresi");
        tableModel.addColumn("Giriş Tarihi");
        tableModel.addColumn("Departman Adı");

        getContentPane().add(panel);

        // Önbelleği doldur
        fillSearchQueries();

        // Tabloyu başlangıçta doldur
        refreshTable();
    }

    // Önbelleği dolduran yardımcı fonksiyon
    private void fillSearchQueries() {
        searchQueries = new HashMap<>();
        searchQueries.put("ad", "SELECT p.*, d.departman_adi FROM personel p INNER JOIN departman d ON p.departman_id = d.departman_id WHERE p.ad LIKE ?");
        searchQueries.put("soyad", "SELECT p.*, d.departman_adi FROM personel p INNER JOIN departman d ON p.departman_id = d.departman_id WHERE p.soyad LIKE ?");
        searchQueries.put("departman_adi", "SELECT p.*, d.departman_adi FROM personel p INNER JOIN departman d ON p.departman_id = d.departman_id WHERE d.departman_adi LIKE ?");
    }

    private void search() {
        String aramaTuru = comboBox.getSelectedItem().toString();
        String aramaMetni = txtArama.getText();

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, password);

            // Önbellekten uygun sorguyu al
            String query = searchQueries.get(aramaTuru);

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%" + aramaMetni + "%");

            ResultSet rs = pstmt.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = {rs.getString("tc"), rs.getString("ad"), rs.getString("soyad"), rs.getString("telefon"), rs.getString("mail"), rs.getString("cinsiyet"), rs.getDouble("maas"), rs.getString("deneyim_suresi"), rs.getDate("giris_tarihi"), rs.getString("departman_adi")};
                tableModel.addRow(rowData);
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void refreshTable() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.tc, p.ad, p.soyad, p.telefon, p.mail, p.cinsiyet, p.maas, p.deneyim_suresi, p.giris_tarihi, d.departman_adi " +
                                             "FROM personel p " +
                                             "INNER JOIN departman d ON p.departman_id = d.departman_id");

            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] rowData = {rs.getString("tc"), rs.getString("ad"), rs.getString("soyad"), rs.getString("telefon"), rs.getString("mail"), rs.getString("cinsiyet"), rs.getDouble("maas"), rs.getString("deneyim_suresi"), rs.getDate("giris_tarihi"), rs.getString("departman_adi")};
                tableModel.addRow(rowData);
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PerAra frame = new PerAra();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
