package main;
import java.awt.Color;
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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

public class Hayvandüzenle extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField yavru_sayısı;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_7;
    private JComboBox<?> combomelih;
    private JComboBox<?> cinsiyet;

    // PostgreSQL veritabanı bağlantı bilgileri
    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";

    // Veritabanı bağlantısını oluştur
    public Connection baglan() throws SQLException {
        return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
    }

    // Yeni tabloyu güncelle
    public void tabloyuGuncelle() {
        try (Connection conn = baglan()) {
            String sql = "SELECT hayvan_id, irk AS \"Irk\", kuepe_numarasi AS \"Küpe Numarası\", dogum_tarihi AS \"Doğum Tarihi\", hastaliklari AS \"Hastalıkları\", yavrulama_sayisi AS \"Yavrulama Sayısı\", cinsiyet AS \"Cinsiyet\", kilo AS \"Kilo\", gunluk_sut AS \"Günlük Süt\", tuketilen_besin_kg AS \"Tüketilen Besin Kg\", fiyat AS \"Fiyat\", bilgi AS \"Bilgi\" FROM hayvan";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            // JTable'a verileri eklemek için bir model oluşturun
            DefaultTableModel model = new DefaultTableModel();

            // Tüm sütun adlarını alın ve modele ekle
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                model.addColumn(metaData.getColumnLabel(columnIndex));
            }

            // Veritabanından alınan her bir satırı modele ekle
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                model.addRow(row);
            }

            // JTable'a modeli ata
            table.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hayvan tablosundaki bir kaydı güncelle
    public void hayvanGuncelle() {
        try (Connection conn = baglan()) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String irk = combomelih.getSelectedItem().toString();
                String kuepeNumarasi = textField.getText();
                String dogumTarihiStr = textField_1.getText();
                String hastaliklari = textField_2.getText();
                String yavruSayisiStr = yavru_sayısı.getText();
                String cinsiyetSecimi = cinsiyet.getSelectedItem().toString();
                String kiloStr = textField_3.getText();
                String gunlukSutStr = textField_4.getText();
                String tuketilenBesinKgStr = textField_5.getText();
                String fiyatStr = textField_6.getText();
                String bilgi = textField_7.getText();

                // Kullanıcıya güncellemek istediği verileri sormak için iletişim kutusu oluştur
                String message = "Seçili kaydı güncellemek istiyor musunuz?\n\n" +
                        "Irk: " + irk + "\n" +
                        "Küpe Numarası: " + kuepeNumarasi + "\n" +
                        "Doğum Tarihi: " + dogumTarihiStr + "\n" +
                        "Hastalıkları: " + hastaliklari + "\n" +
                        "Yavrulama Sayısı: " + yavruSayisiStr + "\n" +
                        "Cinsiyet: " + cinsiyetSecimi + "\n" +
                        "Kilo: " + kiloStr + "\n" +
                        "Günlük Süt: " + gunlukSutStr + "\n" +
                        "Tüketilen Besin Kg: " + tuketilenBesinKgStr + "\n" +
                        "Fiyat: " + fiyatStr + "\n" +
                        "Bilgi: " + bilgi + "\n";
                
                int confirmResult = JOptionPane.showConfirmDialog(null, message, "Güncelleme Onayı", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Veriyi güncelle
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(dogumTarihiStr);
                    java.sql.Date dogumTarihi = new java.sql.Date(parsedDate.getTime());
                    int yavruSayisi = Integer.parseInt(yavruSayisiStr);
                    double kilo = Double.parseDouble(kiloStr);
                    double gunlukSut = Double.parseDouble(gunlukSutStr);
                    double tuketilenBesinKg = Double.parseDouble(tuketilenBesinKgStr);
                    double fiyat = Double.parseDouble(fiyatStr);
                    int hayvanID = (int) table.getValueAt(selectedRow, 0); // Seçili satırın hayvan ID'sini al
                    String sql = "UPDATE hayvan SET irk = ?, kuepe_numarasi = ?, dogum_tarihi = ?, hastaliklari = ?, yavrulama_sayisi = ?, cinsiyet = ?, kilo = ?, gunluk_sut = ?, tuketilen_besin_kg = ?, fiyat = ?, bilgi = ? WHERE hayvan_id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, irk);
                    preparedStatement.setString(2, kuepeNumarasi);
                    preparedStatement.setDate(3, dogumTarihi);
                    preparedStatement.setString(4, hastaliklari);
                    preparedStatement.setInt(5, yavruSayisi);
                    preparedStatement.setString(6, cinsiyetSecimi);
                    preparedStatement.setDouble(7, kilo);
                    preparedStatement.setDouble(8, gunlukSut);
                    preparedStatement.setDouble(9, tuketilenBesinKg);
                    preparedStatement.setDouble(10, fiyat);
                    preparedStatement.setString(11, bilgi);
                    preparedStatement.setInt(12, hayvanID);
                    int etkilenenSatirSayisi = preparedStatement.executeUpdate();
                    if (etkilenenSatirSayisi > 0) {
                        JOptionPane.showMessageDialog(null, "Kayıt başarıyla güncellendi.");
                        // Tabloyu güncelle
                        tabloyuGuncelle();
                    } else {
                        JOptionPane.showMessageDialog(null, "Kayıt güncellenirken bir hata oluştu.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Lütfen güncellemek için bir kayıt seçin.");
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Hayvandüzenle frame = new Hayvandüzenle();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Hayvandüzenle() {
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

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(8, 239, 772, 146);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JLabel lblNewLabel = new JLabel("Irk:");
        lblNewLabel.setBounds(8, 10, 43, 19);
        contentPane.add(lblNewLabel);

        combomelih = new JComboBox();
        combomelih.setModel(new DefaultComboBoxModel(new String[]{"Holstein", "Jersey", "Angus", "Simmental"}));
        combomelih.setSelectedIndex(0);
        combomelih.setToolTipText("eray,\r\n\r\nerere\r\n");
        combomelih.setBounds(32, 10, 86, 19);
        contentPane.add(combomelih);

        JLabel küpelabel = new JLabel("Küpe Numarası:");
        küpelabel.setBounds(130, 11, 98, 16);
        contentPane.add(küpelabel);

        textField = new JTextField();
        textField.setBounds(233, 10, 76, 19);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Doğum Tarihi :");
        lblNewLabel_1.setBounds(317, 13, 96, 16);
        contentPane.add(lblNewLabel_1);

        textField_1 = new JTextField();
        textField_1.setBounds(421, 10, 76, 19);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Hastalıkları :");
        lblNewLabel_2.setBounds(527, 10, 86, 19);
        contentPane.add(lblNewLabel_2);

        textField_2 = new JTextField();
        textField_2.setBounds(639, 10, 76, 19);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Yavrulama Sayısı :");
        lblNewLabel_3.setBounds(8, 59, 111, 19);
        contentPane.add(lblNewLabel_3);

        yavru_sayısı = new JTextField();
        yavru_sayısı.setBounds(130, 59, 76, 19);
        contentPane.add(yavru_sayısı);
        yavru_sayısı.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Cinsiyet:");
        lblNewLabel_4.setBounds(222, 57, 66, 22);
        contentPane.add(lblNewLabel_4);

        cinsiyet = new JComboBox();
        cinsiyet.setModel(new DefaultComboBoxModel(new String[]{"Erkek", "Dişi"}));
        cinsiyet.setBounds(280, 58, 76, 21);
        contentPane.add(cinsiyet);

        JLabel melih_kilo = new JLabel("Kilo:");
        melih_kilo.setBounds(385, 62, 52, 13);
        contentPane.add(melih_kilo);

        textField_3 = new JTextField();
        textField_3.setBounds(421, 59, 76, 19);
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        JLabel lblNewLabel_5 = new JLabel("Günlük Süt:");
        lblNewLabel_5.setBounds(534, 62, 79, 13);
        contentPane.add(lblNewLabel_5);

        textField_4 = new JTextField();
        textField_4.setBounds(628, 59, 76, 19);
        contentPane.add(textField_4);
        textField_4.setColumns(10);

        JLabel lblNewLabel_6 = new JLabel("Tüketilen Besin Kg : ");
        lblNewLabel_6.setBounds(8, 104, 131, 13);
        contentPane.add(lblNewLabel_6);

        textField_5 = new JTextField();
        textField_5.setBounds(130, 101, 76, 19);
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        JLabel lblNewLabel_7 = new JLabel("Fiyat:");
        lblNewLabel_7.setBounds(248, 104, 60, 16);
        contentPane.add(lblNewLabel_7);

        textField_6 = new JTextField();
        textField_6.setBounds(322, 101, 76, 19);
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        JLabel lblNewLabel_8 = new JLabel("Hayvan Hakkında Bilgi : ");
        lblNewLabel_8.setBounds(448, 104, 165, 16);
        contentPane.add(lblNewLabel_8);

        textField_7 = new JTextField();
        textField_7.setBounds(604, 104, 176, 65);
        contentPane.add(textField_7);
        textField_7.setColumns(10);

        JButton güncelle = new JButton("Kayıt Güncelle");
        güncelle.setBackground(Color.GREEN);
        güncelle.setBounds(234, 176, 122, 21);
        contentPane.add(güncelle);

        JButton yenile = new JButton("Yenile");
        yenile.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		   tabloyuGuncelle();
        	}
        });
        yenile.setBackground(Color.YELLOW);
        yenile.setBounds(416, 176, 81, 21);
        contentPane.add(yenile);

        // Güncelle butonuna tıklanınca hayvanı güncelleyen işlem
        güncelle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                hayvanGuncelle();
            }
        });

        // Yenile butonuna tıklandığında tabloyu güncelleyin
        yenile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                tabloyuGuncelle();
            }
        });

        // Tablodaki bir satıra tıklanınca bilgileri gösteren işlem
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { // Bir satır seçilmiş mi?
                    // Seçilen satırdaki verileri alan kod buraya gelecek
                    // Bu kısımda seçilen satırdaki verileri ilgili text alanlarına yerleştireceğiz
                    combomelih.setSelectedItem(table.getValueAt(selectedRow, 1)); // Irk
                    textField.setText(table.getValueAt(selectedRow, 2).toString()); // Küpe Numarası
                    textField_1.setText(table.getValueAt(selectedRow, 3).toString()); // Doğum Tarihi
                    textField_2.setText(table.getValueAt(selectedRow, 4).toString()); // Hastalıkları
                    yavru_sayısı.setText(table.getValueAt(selectedRow, 5).toString()); // Yavrulama Sayısı
                    cinsiyet.setSelectedItem(table.getValueAt(selectedRow, 6)); // Cinsiyet
                    textField_3.setText(table.getValueAt(selectedRow, 7).toString()); // Kilo
                    textField_4.setText(table.getValueAt(selectedRow, 8).toString()); // Günlük Süt
                    textField_5.setText(table.getValueAt(selectedRow, 9).toString()); // Tüketilen Besin Kg
                    textField_6.setText(table.getValueAt(selectedRow, 10).toString()); // Fiyat
                    textField_7.setText(table.getValueAt(selectedRow, 11).toString()); // Bilgi
                }
            }
        });

        // Uygulama başladığında tabloyu güncelleyin
        tabloyuGuncelle();
    }
}
