package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.Color;
import java.awt.Toolkit;

public class KayıtWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField kullaniciaditext;
    private JFormattedTextField numara_text;
    private JTextField mail_text;
    private JTextField paswordtext;
    private JTextField adText;
    private JTextField soyadText;

    private String url = "jdbc:postgresql://localhost:5432/farm";
    private String username = "postgres";
    private String password = "123";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    KayıtWindow frame = new KayıtWindow();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public KayıtWindow() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Çiftlik Otomasyonu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 804, 424);
    	setLocationRelativeTo(null);
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
                return new Dimension(400, 400);
            }
        };
        contentPane.setForeground(SystemColor.inactiveCaptionText);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("KAYIT OL");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setBounds(326, 24, 111, 35);
        contentPane.add(lblNewLabel);

        kullaniciaditext = new JTextField();
        kullaniciaditext.setBackground(Color.CYAN);
        kullaniciaditext.setBounds(214, 134, 100, 19);
        contentPane.add(kullaniciaditext);
        kullaniciaditext.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Kullanıcı Adınız");
        lblNewLabel_1.setBounds(219, 103, 95, 21);
        contentPane.add(lblNewLabel_1);

        JButton kayitbutton = new JButton("Kayit Oluştur");
        kayitbutton.setBackground(Color.GREEN);
        kayitbutton.setBounds(308, 239, 119, 21);
        contentPane.add(kayitbutton);

        JLabel lblNewLabel_2 = new JLabel("Şifreniz");
        lblNewLabel_2.setBounds(372, 105, 91, 17);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Telefon Numaranız");
        lblNewLabel_3.setBounds(503, 105, 131, 17);
        contentPane.add(lblNewLabel_3);

        numara_text = new JFormattedTextField();
        numara_text.setBackground(Color.CYAN);
        numara_text.setBounds(506, 135, 86, 17);
        numara_text.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        contentPane.add(numara_text);

        JLabel lblNewLabel_4 = new JLabel("E-Posta");
        lblNewLabel_4.setBounds(671, 103, 91, 21);
        contentPane.add(lblNewLabel_4);

        mail_text = new JTextField();
        mail_text.setBackground(Color.CYAN);
        mail_text.setBounds(645, 134, 117, 19);
        contentPane.add(mail_text);
        mail_text.setColumns(10);

        paswordtext = new JTextField();
        paswordtext.setBackground(Color.CYAN);
        paswordtext.setBounds(356, 134, 95, 19);
        contentPane.add(paswordtext);
        paswordtext.setColumns(10);

        JButton geridon = new JButton("Geri Dön");
        geridon.setBackground(Color.YELLOW);
        geridon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
                dispose();
            }
        });
        geridon.setBounds(316, 281, 111, 21);
        contentPane.add(geridon);

        JLabel lblNewLabel_5 = new JLabel("Ad");
        lblNewLabel_5.setBounds(19, 107, 40, 13);
        contentPane.add(lblNewLabel_5);

        adText = new JTextField();
        adText.setBackground(Color.CYAN);
        adText.setBounds(8, 134, 76, 19);
        contentPane.add(adText);
        adText.setColumns(10);

        JLabel lblNewLabel_6 = new JLabel("SoyAd");
        lblNewLabel_6.setBounds(111, 107, 40, 13);
        contentPane.add(lblNewLabel_6);

        soyadText = new JTextField();
        soyadText.setBackground(Color.CYAN);
        soyadText.setBounds(104, 134, 76, 19);
        contentPane.add(soyadText);
        soyadText.setColumns(10);

        kayitbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (kullaniciaditext.getText().isEmpty() || paswordtext.getText().isEmpty()
                        || numara_text.getText().isEmpty() || mail_text.getText().isEmpty()
                        || adText.getText().isEmpty() || soyadText.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Lütfen tüm alanları doldurunuz!");
                } else {
                    String kayitBilgileri = "Kullanıcı Adı: " + kullaniciaditext.getText() + "\n"
                            + "Şifre: " + paswordtext.getText() + "\n"
                            + "Telefon Numarası: " + numara_text.getText() + "\n"
                            + "E-Posta: " + mail_text.getText() + "\n"
                            + "Ad: " + adText.getText() + "\n"
                            + "Soyad: " + soyadText.getText();

                    int result = JOptionPane.showConfirmDialog(null, kayitBilgileri + "\n\nKayıt işlemi gerçekleştirilsin mi?",
                            "Kayıt Onayı", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        kayitEkle();
                    } else {
                        JOptionPane.showMessageDialog(null, "Kayıt işlemi iptal edildi.");
                    }
                }
            }
        });
    }

    private void kayitEkle() {
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO t_login (username, password, telefon, mail, ad, soyad) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, kullaniciaditext.getText());
            pst.setString(2, paswordtext.getText());
            pst.setString(3, numara_text.getText());
            pst.setString(4, mail_text.getText());
            pst.setString(5, adText.getText());
            pst.setString(6, soyadText.getText());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Kayıt başarıyla oluşturuldu!");
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Kayıt oluşturulurken bir hata oluştu!");
            }

            pst.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + e.getMessage());
        }
    }
}
