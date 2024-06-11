package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.sql.*;

public class perduzenle extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tc_text;
    private JTextField isim_text;
    private JTextField soyad_text;
    private JTextField telefon_text;
    private JTextField mail_text;
    private JTextField maas_text;
    private JTextField deneyim_text;
    private JComboBox<String> cinsiyetcombo;

    private JComboBox<String> comboBox;


    private JComboBox<String> comboBox_1;
    private JComboBox<String> comboBox_2;
    private JTable table;

    private String url = "jdbc:postgresql://localhost:5432/farm";
    private String username = "postgres";
    private String password = "123";
    private Connection con = null;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    perduzenle frame = new perduzenle();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public perduzenle() {
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

        JLabel per_tc = new JLabel("TC Numarası");
        per_tc.setBounds(8, 7, 91, 24);
        contentPane.add(per_tc);

        tc_text = new JTextField();
        tc_text.setBounds(8, 31, 76, 19);
        contentPane.add(tc_text);
        tc_text.setColumns(10);

        JLabel per_ad = new JLabel("İsim");
        per_ad.setBounds(130, 10, 53, 19);
        contentPane.add(per_ad);

        isim_text = new JTextField();
        isim_text.setBounds(107, 31, 76, 19);
        contentPane.add(isim_text);
        isim_text.setColumns(10);

        JLabel per_soyad = new JLabel("Soyad");
        per_soyad.setBounds(211, 13, 40, 13);
        contentPane.add(per_soyad);

        soyad_text = new JTextField();
        soyad_text.setBounds(194, 31, 76, 19);
        contentPane.add(soyad_text);
        soyad_text.setColumns(10);

        JLabel cinsiyet = new JLabel("Cinsiyet");
        cinsiyet.setBounds(301, 13, 55, 13);
        contentPane.add(cinsiyet);

        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"ERKEK", "KIZ"}));
        comboBox.setBounds(289, 30, 82, 21);
        contentPane.add(comboBox);

        JLabel telefon = new JLabel("Telefon");
        telefon.setBounds(406, 13, 57, 13);
        contentPane.add(telefon);

        telefon_text = new JTextField();
        telefon_text.setBounds(396, 31, 76, 19);
        contentPane.add(telefon_text);
        telefon_text.setColumns(10);

        JLabel mail = new JLabel("Mail");
        mail.setBounds(519, 13, 40, 13);
        contentPane.add(mail);

        mail_text = new JTextField();
        mail_text.setBounds(501, 31, 76, 19);
        contentPane.add(mail_text);
        mail_text.setColumns(10);

        JLabel alan = new JLabel("Çalışma Departmanı");
        alan.setBounds(623, 7, 129, 19);
        contentPane.add(alan);

        comboBox_1 = new JComboBox<>();
        comboBox_1.setBounds(612, 30, 119, 21);
        contentPane.add(comboBox_1);

        JLabel maas = new JLabel("Maaş");
        maas.setBounds(28, 60, 63, 13);
        contentPane.add(maas);

        maas_text = new JTextField();
        maas_text.setBounds(8, 83, 76, 19);
        contentPane.add(maas_text);
        maas_text.setColumns(10);

        JLabel gecegündüz = new JLabel("Saatler");
        gecegündüz.setBounds(117, 60, 53, 13);
        contentPane.add(gecegündüz);

        comboBox_2 = new JComboBox<>();
        comboBox_2.setModel(new DefaultComboBoxModel<>(new String[] { "GÜNDÜZ", "GECE" }));
        comboBox_2.setBounds(107, 82, 76, 21);
        contentPane.add(comboBox_2);
        populateDepartments();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(8, 196, 772, 179);
        contentPane.add(scrollPane);

        JLabel deneyimLabel = new JLabel("Deneyim Süresi");
        deneyimLabel.setBounds(200, 60, 150, 19); 
        contentPane.add(deneyimLabel);

        deneyim_text = new JTextField();
        deneyim_text.setBounds(200, 83, 100, 19); 
        contentPane.add(deneyim_text);



        
        table = new JTable();
        scrollPane.setViewportView(table);
        refreshTable();

        JButton duzenleButton = new JButton("Personel Düzenle");
        duzenleButton.setBackground(Color.GREEN);
        duzenleButton.setBounds(175, 153, 176, 21);
        duzenleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                editPersonnel();
            }
        });
        contentPane.add(duzenleButton);

        JButton silButton = new JButton("Personel Sil");
        silButton.setBackground(Color.RED);
        silButton.setBounds(396, 153, 163, 21);
        silButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deletePersonnel();
            }
        });
        contentPane.add(silButton);

        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { 
                    tc_text.setText(table.getValueAt(selectedRow, 0).toString());
                    isim_text.setText(table.getValueAt(selectedRow, 1).toString());
                    soyad_text.setText(table.getValueAt(selectedRow, 2).toString());
                    telefon_text.setText(table.getValueAt(selectedRow, 3).toString());
                    mail_text.setText(table.getValueAt(selectedRow, 4).toString());
                    comboBox.setSelectedItem(table.getValueAt(selectedRow, 5).toString());
                    maas_text.setText(table.getValueAt(selectedRow, 6).toString());
                    deneyim_text.setText(table.getValueAt(selectedRow, 7).toString());
                    String departman = table.getValueAt(selectedRow, 9).toString();
                    for (int i = 0; i < comboBox_1.getItemCount(); i++) {
                        if (comboBox_1.getItemAt(i).equals(departman)) {
                            comboBox_1.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });    }

    
    private void editPersonnel() {
    	int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String tc = table.getValueAt(selectedRow, 0).toString();
            String ad = isim_text.getText();
            String soyad = soyad_text.getText();
            String telefon = telefon_text.getText();
            String mail = mail_text.getText();
            String cinsiyet = (String) comboBox.getSelectedItem();
            String maasText = maas_text.getText();
            String deneyim = deneyim_text.getText();

            System.out.println("Güncellenen TC: " + tc);
            System.out.println("Yeni İsim: " + ad);
            System.out.println("Yeni Soyad: " + soyad);
            System.out.println("Yeni Telefon: " + telefon);
            System.out.println("Yeni Mail: " + mail);
            System.out.println("Yeni Cinsiyet: " + cinsiyet);
            System.out.println("Yeni Maaş: " + maasText);
            System.out.println("Yeni Deneyim: " + deneyim);        	if (tc.length() != 11 || !tc.matches("[0-9]+")) {
				JOptionPane.showMessageDialog(null,
						"Geçersiz TC numarası. TC numarası 11 haneli olmalı ve sadece rakam içermelidir.");
				return;
			}
            if (maasText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen maaş değeri girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                return; 
            }

            if (!isValidSalary(maasText)) {
                JOptionPane.showMessageDialog(null, "Lütfen geçerli bir maaş değeri girin.", "Hata",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double maas = Double.parseDouble(maasText);
            if (deneyim.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Lütfen deneyim süresi girin.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String departmanAdi = (String) comboBox_1.getSelectedItem();

            if (!isValidName(ad) || !isValidName(soyad)) {
                JOptionPane.showMessageDialog(null, "Geçersiz giriş.Lütfen geçerli bir isim ve soyad girin.");
                return;
            }

            if (!isValidPhone(telefon)) {
                JOptionPane.showMessageDialog(null,
                        "Lütfen geçerli bir telefon numarası girin.Örnek : 0545 654 4835");
                return;
            }

            try {
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection(url, username, password);
                Statement stmt = con.createStatement();

                
                ResultSet rs = stmt.executeQuery(
                        "SELECT departman_id FROM departman WHERE departman_adi = '" + departmanAdi + "'");
                int departmanID = 1;
                if (rs.next()) {
                    departmanID = rs.getInt("departman_id");
                }

                
                String query = "UPDATE personel SET tc = ?, ad = ?, soyad = ?, telefon = ?, mail = ?, cinsiyet = ?, maas = ?, deneyim_suresi = ?, departman_id = ? WHERE tc = ?";

                PreparedStatement preparedStatement = con.prepareStatement(query);
          
                
            	preparedStatement.setString(1, tc);
				preparedStatement.setString(2, ad);
				preparedStatement.setString(3, soyad);
				preparedStatement.setString(4, telefon);
				preparedStatement.setString(5, mail);
				preparedStatement.setString(6, cinsiyet);
				preparedStatement.setDouble(7, maas);
				preparedStatement.setString(8, deneyim);
				  preparedStatement.setInt(9, departmanID);
				  preparedStatement.setString(10, tc); // TC numarasını 10. sütuna yerleştirir

                preparedStatement.executeUpdate();



                JOptionPane.showMessageDialog(null, "Kayıt güncellendi.");

                refreshTable();

                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Kayıt güncellenirken bir hata oluştu: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lütfen düzenlemek istediğiniz bir personel seçin.");
        }
    }
    private void populateDepartments() {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT departman_adi FROM departman");
            
            while (rs.next()) {
                String departmanAdi = rs.getString("departman_adi");
                comboBox_1.addItem(departmanAdi);
            }
            
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Departmanlar alınırken bir hata oluştu: " + e.getMessage());
        }
    }

   
    private void deletePersonnel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(null, "Seçilen personeli silmek istediğinizden emin misiniz?", "Personel Sil", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String tc = table.getValueAt(selectedRow, 0).toString();

                try {
                    Class.forName("org.postgresql.Driver");
                    con = DriverManager.getConnection(url, username, password);
                    String query = "DELETE FROM personel WHERE tc = ?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, tc);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Personel silindi.");

                    refreshTable();

                    con.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Personel silinirken bir hata oluştu: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz bir personel seçin.");
        }
    }

    // İsim ve soyadının sadece harf içerip içermediğini kontrol eden metot
    public boolean isValidName(String name) {
        return name.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ]+");
    }

    public boolean isValidPhone(String phone) {
        return phone.matches("[0-9]+");
    }

    private boolean isValidSalary(String salary) {
        try {
            Double.parseDouble(salary);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void refreshTable() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("TC");
            model.addColumn("İsim");
            model.addColumn("Soyad");
            model.addColumn("Telefon");
            model.addColumn("Mail");
            model.addColumn("Cinsiyet");
            model.addColumn("Maaş");
            model.addColumn("Deneyim Süresi");
            model.addColumn("Kayıt Tarihi");
            model.addColumn("Departman");

            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT p.tc, p.ad, p.soyad, p.telefon, p.mail, p.cinsiyet, p.maas, p.deneyim_suresi, p.giris_tarihi, d.departman_adi FROM personel p JOIN departman d ON p.departman_id = d.departman_id");
            while (rs.next()) {
                model.addRow(new Object[] { rs.getString("tc"), rs.getString("ad"), rs.getString("soyad"),
                        rs.getString("telefon"), rs.getString("mail"), rs.getString("cinsiyet"), rs.getDouble("maas"),
                        rs.getString("deneyim_suresi"), rs.getDate("giris_tarihi"), rs.getString("departman_adi") });
            }
            table.setModel(model);
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Tablo güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }
}