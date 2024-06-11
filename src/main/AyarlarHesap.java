package main;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AyarlarHesap extends JFrame {
    private static final long serialVersionUID = 1L;
    private String url = "jdbc:postgresql://localhost:5432/farm";
    private String dbUsername = "postgres";
    private String dbPassword = "123";

    private JPanel contentPane;
    private JTextField tfUsername, tfPassword, tfMail, tfTelefon, tfAd, tfSoyad;
    private JButton btnUpdate;

    public AyarlarHesap() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(AyarlarHesap.class.getResource("/main/inek.jpg")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Çiftlik Otomasyonu");
        setBounds(100, 100, 477, 424);

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
                return new Dimension(800, 424); // JPanel'in boyutunu ayarla
            }
        };
        contentPane.setForeground(SystemColor.inactiveCaptionText);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initUI();
        loadData();
    }

    private void initUI() {
        JLabel lblUsername = new JLabel("Kullanıcı Adı:");
        lblUsername.setBounds(50, 50, 100, 25);
        contentPane.add(lblUsername);

        tfUsername = new JTextField();
        tfUsername.setBounds(150, 50, 200, 25);
        tfUsername.setEditable(false); // Kullanıcı adı değiştirilemez yapıldı
        contentPane.add(tfUsername);
        tfUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Şifre:");
        lblPassword.setBounds(50, 90, 100, 25);
        contentPane.add(lblPassword);

        tfPassword = new JTextField();
        tfPassword.setBounds(150, 90, 200, 25);
        contentPane.add(tfPassword);
        tfPassword.setColumns(10);

        JLabel lblMail = new JLabel("Mail:");
        lblMail.setBounds(50, 130, 100, 25);
        contentPane.add(lblMail);

        tfMail = new JTextField();
        tfMail.setBounds(150, 130, 200, 25);
        contentPane.add(tfMail);
        tfMail.setColumns(10);

        JLabel lblTelefon = new JLabel("Telefon:");
        lblTelefon.setBounds(50, 170, 100, 25);
        contentPane.add(lblTelefon);

        tfTelefon = new JTextField();
        tfTelefon.setBounds(150, 170, 200, 25);
        contentPane.add(tfTelefon);
        tfTelefon.setColumns(10);

        JLabel lblAd = new JLabel("Ad:");
        lblAd.setBounds(50, 210, 100, 25);
        contentPane.add(lblAd);

        tfAd = new JTextField();
        tfAd.setBounds(150, 210, 200, 25);
        contentPane.add(tfAd);
        tfAd.setColumns(10);

        JLabel lblSoyad = new JLabel("Soyad:");
        lblSoyad.setBounds(50, 250, 100, 25);
        contentPane.add(lblSoyad);

        tfSoyad = new JTextField();
        tfSoyad.setBounds(150, 250, 200, 25);
        contentPane.add(tfSoyad);
        tfSoyad.setColumns(10);

        btnUpdate = new JButton("Güncelle");
        btnUpdate.setBounds(150, 290, 200, 30);
        contentPane.add(btnUpdate);

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });
    }

    private void loadData() {
        String currentUsername = login.getUsername();  // Login sınıfından kullanıcı adını al

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM public.t_login WHERE username = ?")) {

            stmt.setString(1, currentUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfUsername.setText(rs.getString("username"));
                tfPassword.setText(rs.getString("password"));
                tfMail.setText(rs.getString("mail"));
                tfTelefon.setText(rs.getString("telefon"));
                tfAd.setText(rs.getString("ad"));
                tfSoyad.setText(rs.getString("soyad"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateData() {
        String currentUsername = login.getUsername();  // Login sınıfından kullanıcı adını al

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement stmt = conn.prepareStatement("UPDATE public.t_login SET password = ?, mail = ?, telefon = ?, ad = ?, soyad = ? WHERE username = ?")) {

            stmt.setString(1, tfPassword.getText());
            stmt.setString(2, tfMail.getText());
            stmt.setString(3, tfTelefon.getText());
            stmt.setString(4, tfAd.getText());
            stmt.setString(5, tfSoyad.getText());
            stmt.setString(6, currentUsername);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Bilgiler başarıyla güncellendi!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AyarlarHesap().setVisible(true);
            }
        });
    }
}
