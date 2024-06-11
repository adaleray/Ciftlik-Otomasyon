package main;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;

public class SiparisVer extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";
    private static final String gnmail = "jetshoperay@gmail.com";
    private static final String gnpass = "oxkwuujfbzvvoooc";

    private JTextField DogalYemText;
    private JTextField HazirYemText;
    private JComboBox<String> SirketcomboBox;

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SiparisVer frame = new SiparisVer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sirketleriComboBoxaEkle(JComboBox<String> comboBox) {
        try (Connection conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE)) {
            String sql = "SELECT ad FROM public.sirketler";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String sirketAdi = rs.getString("ad");
                        comboBox.addItem(sirketAdi);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SiparisVer() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource("/main/inek.jpg")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 560, 360);
        setLocationRelativeTo(null);
        setTitle("Sipariş Ver");
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JPanel contentPane = new JPanel() {
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
                return new Dimension(800, 446);
            }
        };
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Doğal Yem Miktarı(kg):");
        lblNewLabel.setBounds(71, 54, 135, 13);
        contentPane.add(lblNewLabel);

        JLabel lblHazrYemMiktarkg = new JLabel("Hazır Yem Miktarı(kg):");
        lblHazrYemMiktarkg.setBounds(71, 125, 125, 13);
        contentPane.add(lblHazrYemMiktarkg);

        DogalYemText = new JTextField();
        DogalYemText.setBounds(231, 41, 176, 40);
        contentPane.add(DogalYemText);
        DogalYemText.setColumns(10);

        HazirYemText = new JTextField();
        HazirYemText.setColumns(10);
        HazirYemText.setBounds(231, 111, 176, 40);
        contentPane.add(HazirYemText);

        JLabel lblirketSein = new JLabel("Şirket Seçin:");
        lblirketSein.setBounds(71, 197, 125, 13);
        contentPane.add(lblirketSein);

        SirketcomboBox = new JComboBox<String>();
        SirketcomboBox.setBounds(231, 193, 176, 21);
        contentPane.add(SirketcomboBox);
        sirketleriComboBoxaEkle(SirketcomboBox);

        JButton Siparisbutton = new JButton("Sipariş Ver");
        Siparisbutton.setBackground(new Color(128, 255, 255));
        Siparisbutton.setBounds(275, 244, 109, 21);
        contentPane.add(Siparisbutton);

        Siparisbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                siparisVer();
            }
        });
    }

    private void siparisVer() {
        String dogalYem = DogalYemText.getText();
        String hazirYem = HazirYemText.getText();

        if (dogalYem.isEmpty() || hazirYem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
        } else {
            String sirketAdi = (String) SirketcomboBox.getSelectedItem();
            String email = getSirketEmail(sirketAdi);

            if (email != null && !email.isEmpty()) {
                String subject = "Sipariş Bilgisi";
                String body = "Doğal Yem Miktarı: " + dogalYem + " kg\nHazır Yem Miktarı: " + hazirYem + " kg\nŞirket: " + sirketAdi;
                
                new Thread(new Runnable() {
                    public void run() {
                        sendEmail(subject, body, email);
                    }
                }).start();
            } else {
                JOptionPane.showMessageDialog(this, "Seçilen şirket için e-posta adresi bulunamadı.", "E-posta Hatası", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getSirketEmail(String sirketAdi) {
        String email = null;
        try (Connection conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE)) {
            String sql = "SELECT email FROM public.sirketler WHERE ad = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, sirketAdi);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        email = rs.getString("email");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    public static void sendEmail(String subject, String body, String email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gnmail, gnpass);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gnmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            JOptionPane.showMessageDialog(null, "E-posta başarıyla gönderildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "E-posta gönderimi sırasında bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}
