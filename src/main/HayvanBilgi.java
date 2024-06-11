package main;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class HayvanBilgi extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_5;
    private JTextField textField_3;
    private JTextField textField_6;
    private Connection conn;
    private JTable table;
    private JTextField ırkTextField;
    private JTextField textField_4;
    private JTextField textField;
    private JTextField maxyavrutext;
    private JTextField textField_7;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HayvanBilgi frame = new HayvanBilgi();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:postgresql://localhost:5432/farm";
            String user = "postgres";
            String password = "123";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HayvanBilgi() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Çiftlik Otomasyonu");
		setBounds(100, 100, 804, 424);


		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon imageIcon = new ImageIcon(getClass().getResource("eray.jpg"));
				Image image = imageIcon.getImage();
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(400, 400); // JPanel'in boyutunu ayarla
			}
		};
		contentPane.setForeground(SystemColor.inactiveCaptionText);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Irk Gir");
        lblNewLabel.setBounds(78, 22, 81, 20);
        contentPane.add(lblNewLabel);
        connectToDatabase();

        JLabel lblNewLabel_1 = new JLabel("Doğal yem (KG)");
        lblNewLabel_1.setBounds(230, 26, 98, 13);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Hazır Yem (kg)");
        lblNewLabel_2.setBounds(447, 26, 93, 13);
        contentPane.add(lblNewLabel_2);

        textField_1 = new JTextField();
        textField_1.setBounds(447, 53, 96, 19);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Günlük Su(L)");
        lblNewLabel_3.setBounds(650, 26, 87, 13);
        contentPane.add(lblNewLabel_3);

        textField_2 = new JTextField();
        textField_2.setBounds(641, 53, 96, 19);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Aşılama Periyodu");
        lblNewLabel_4.setBounds(30, 120, 115, 13);
        contentPane.add(lblNewLabel_4);

        JButton btnNewButton = new JButton("Ekle");
        btnNewButton.setBounds(322, 238, 85, 21);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_5 = new JLabel("Tırnak Bakım");
        lblNewLabel_5.setBounds(176, 120, 81, 13);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Süt Verim(L)");
        lblNewLabel_6.setBounds(485, 120, 93, 13);
        contentPane.add(lblNewLabel_6);

        textField_5 = new JTextField();
        textField_5.setBounds(482, 143, 96, 19);
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        JLabel lblNewLabel_7 = new JLabel("Gezginlik");
        lblNewLabel_7.setBounds(654, 120, 60, 13);
        contentPane.add(lblNewLabel_7);

        textField_3 = new JTextField();
        textField_3.setBounds(27, 143, 96, 19);
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        textField_6 = new JTextField();
        textField_6.setBounds(641, 143, 96, 19);
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 269, 772, 103);
        contentPane.add(scrollPane);
        
        table = new JTable();
        scrollPane.setViewportView(table);
        table.repaint();
        table.revalidate();
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                String tur = (String) table.getValueAt(row, 0);
                int dogalYem = (int) table.getValueAt(row, 1);
                int hazirYem = (int) table.getValueAt(row, 2);
                int gunlukSu = (int) table.getValueAt(row, 3);
                String asilama = (String) table.getValueAt(row, 4);
                String tirnakBakim = (String) table.getValueAt(row, 5);
                String sutVerim = (String) table.getValueAt(row, 6);
                String gezginlik = (String) table.getValueAt(row, 7);
                int maxYavrulama = (int) table.getValueAt(row, 8);
                String notlar = (String) table.getValueAt(row, 9); // Notlar sütunu

                String message = "Irk: " + tur +
                                "\nDoğal Yem: " + dogalYem +
                                "\nHazır Yem: " + hazirYem +
                                "\nGünlük Su: " + gunlukSu +
                                "\nAşılama Periyodu: " + asilama +
                                "\nTırnak Bakım: " + tirnakBakim +
                                "\nSüt Verim: " + sutVerim +
                                "\nGezginlik: " + gezginlik +
                                "\nMax Yavrulama: " + maxYavrulama +
                                "\nNotlar: " + notlar; // Notlar sütunu

                JOptionPane.showMessageDialog(contentPane, message, "IRK DETAYLI BİLGİ", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        ırkTextField = new JTextField();
        ırkTextField.setBounds(43, 52, 96, 19);
        contentPane.add(ırkTextField);
        ırkTextField.setColumns(10);
        
        textField_4 = new JTextField();
        textField_4.setBounds(176, 143, 76, 19);
        contentPane.add(textField_4);
        textField_4.setColumns(10);
        
        textField = new JTextField();
        textField.setBounds(235, 49, 93, 19);
        contentPane.add(textField);
        textField.setColumns(10);
        
        JLabel maxlabel = new JLabel("Max Yavrulama");
        maxlabel.setBounds(311, 120, 98, 13);
        contentPane.add(maxlabel);
        
        maxyavrutext = new JTextField();
        maxyavrutext.setBounds(322, 143, 76, 19);
        contentPane.add(maxyavrutext);
        maxyavrutext.setColumns(10);
        
        JLabel lblNewLabel_8 = new JLabel("Irk Hakkında Notlar");
        lblNewLabel_8.setVerticalAlignment(SwingConstants.BOTTOM);
        lblNewLabel_8.setBounds(317, 177, 134, 13);
        contentPane.add(lblNewLabel_8);
        
        textField_7 = new JTextField();
        textField_7.setBounds(97, 200, 551, 19);
        contentPane.add(textField_7);
        textField_7.setColumns(10);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField.getText().isEmpty() || textField_1.getText().isEmpty() ||
                    textField_2.getText().isEmpty() || textField_3.getText().isEmpty() ||
                    textField_4.getText().isEmpty() || textField_5.getText().isEmpty() ||
                    textField_6.getText().isEmpty() || ırkTextField.getText().isEmpty() ||
                    maxyavrutext.getText().isEmpty() || textField_7.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Veri ekleme isteği gösterildiğinde kullanıcıya verileri sormak için bir iletişim kutusu göster
                    int choice = JOptionPane.showConfirmDialog(contentPane, "Aşağıdaki verileri eklemek istiyor musunuz?\nIrk: " +
                        ırkTextField.getText() + "\nDoğal Yem: " + textField.getText() + "\nHazır Yem: " + textField_1.getText() +
                        "\nGünlük Su: " + textField_2.getText() + "\nAşılama Periyodu: " + textField_3.getText() +
                        "\nTırnak Bakım: " + textField_4.getText() + "\nSüt Verim: " + textField_5.getText() +
                        "\nGezginlik: " + textField_6.getText() + "\nMax Yavrulama: " + maxyavrutext.getText() +
                        "\nIrk Hakkında Notlar: " + textField_7.getText(), "Veri Ekleme Onayı", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        addToDatabase();
                    }
                }
            }
        });

        fillTable();
    }

    
    private void addToDatabase() {
        try {
            String tur = ırkTextField.getText();
            int dogalYem = Integer.parseInt(textField.getText());
            int hazirYem = Integer.parseInt(textField_1.getText());
            int gunlukSu = Integer.parseInt(textField_2.getText());
            String asilama = textField_3.getText();
            String tirnakBakim = textField_4.getText();
            int sutVerim = Integer.parseInt(textField_5.getText());
            String gezginlik = textField_6.getText();
            int maxYavrulama = Integer.parseInt(maxyavrutext.getText());
            String notlar = textField_7.getText(); // Yeni alan

            String sql = "INSERT INTO public.hayvanlar (tur, dogal_yem, hazir_yem, gunluk_su, asilama, tirnak_bakim, sut_verim, gezginlik, max_yavrulama, notlar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, tur);
            pst.setInt(2, dogalYem);
            pst.setInt(3, hazirYem);
            pst.setInt(4, gunlukSu);
            pst.setString(5, asilama);
            pst.setString(6, tirnakBakim);
            pst.setInt(7, sutVerim);
            pst.setString(8, gezginlik);
            pst.setInt(9, maxYavrulama);
            pst.setString(10, notlar); // Yeni alan ekleme

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(HayvanBilgi.this, "Veriler başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                fillTable(); // Veri ekledikten sonra tabloyu güncelle
            } else {
                JOptionPane.showMessageDialog(HayvanBilgi.this, "Veri eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(HayvanBilgi.this, "Veri eklenirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillTable() {
        try {
            String sql = "SELECT * FROM public.hayvanlar";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(); // Yeni bir model oluştur

            // Sütun başlıklarını ekle
            model.addColumn("Tur");
            model.addColumn("Doğal Yem");
            model.addColumn("Hazır Yem");
            model.addColumn("Günlük Su");
            model.addColumn("Aşılama Periyodu");
            model.addColumn("Tırnak Bakım");
            model.addColumn("Süt Verim");
            model.addColumn("Gezginlik");
            model.addColumn("Yavrulama Sayısı");
            model.addColumn("Notlar"); // Yeni sütun eklendi

            // Verileri model'e ekle
            while (rs.next()) {
                Object[] row = new Object[10]; // Sütun dizisinin boyutu 10 olarak ayarlandı
                row[0] = rs.getString("tur");
                row[1] = rs.getInt("dogal_yem");
                row[2] = rs.getInt("hazir_yem");
                row[3] = rs.getInt("gunluk_su");
                row[4] = rs.getString("asilama");
                row[5] = rs.getString("tirnak_bakim");
                row[6] = rs.getString("sut_verim");
                row[7] = rs.getString("gezginlik");
                row[8] = rs.getInt("max_yavrulama");
                row[9] = rs.getString("notlar"); // Yeni sütun ekleme
                model.addRow(row);
            }

            // Tabloya yeni modeli set et
            table.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(HayvanBilgi.this, "Tabloya veri eklenirken bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

}
