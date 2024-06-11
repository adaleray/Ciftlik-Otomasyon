package main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SirketEkle extends JFrame {
 
	private static final long serialVersionUID = 1L;
	private JTextField sirketAdiField;
    private JTextField telefonField;
    private JTextField emailField;
    private JTable sirketTable;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";

    public SirketEkle() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(SirketEkle.class.getResource("/main/inek.jpg")));
        setTitle("Şirket Ekleme ve Silme");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Şirket Adı:"));
        sirketAdiField = new JTextField();
        formPanel.add(sirketAdiField);

        formPanel.add(new JLabel("Telefon:"));
        telefonField = new JTextField();
        formPanel.add(telefonField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        JButton ekleButton = new JButton("Şirket Ekle");
        ekleButton.setBackground(Color.GREEN);
        ekleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sirketEkle();
            }
        });
        formPanel.add(ekleButton);

        JButton silButton = new JButton("Şirket Sil");
        silButton.setBackground(Color.RED);
        silButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sirketSil();
            }
        });
        formPanel.add(silButton);

        getContentPane().add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Şirket Adı", "Telefon", "Email"}, 0);
        sirketTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(sirketTable);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        sirketleriYukle();
    }

    private void sirketEkle() {
        String sirketAdi = sirketAdiField.getText();
        String telefon = telefonField.getText();
        String email = emailField.getText();

        String sql = "INSERT INTO public.sirketler (ad, telefon, email) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sirketAdi);
            pstmt.setString(2, telefon);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Şirket başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            sirketleriYukle();
            temizle();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sirketSil() {
        int selectedRow = sirketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen silinecek bir şirket seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sirketId = (int) tableModel.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM public.sirketler WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sirketId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Şirket başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            sirketleriYukle();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sirketleriYukle() {
        String sql = "SELECT * FROM public.sirketler";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                String telefon = rs.getString("telefon");
                String email = rs.getString("email");
                tableModel.addRow(new Object[]{id, ad, telefon, email});
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void temizle() {
        sirketAdiField.setText("");
        telefonField.setText("");
        emailField.setText("");
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SirketEkle().setVisible(true);
            }
        });
    }
}
