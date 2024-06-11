package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
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
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;


import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.*;

public class LoginWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usernameTextField;
	private JPasswordField passwordField;

	private String url = "jdbc:postgresql://localhost:5432/farm";
	private String username = "postgres";
	private String password = "123";
	private Connection con = null;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow frame = new LoginWindow();
					UIManager.setLookAndFeel(new FlatLightLaf());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public LoginWindow() {

		setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));
		setTitle("Çiftlik Otomasyonu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 804, 424);
		setLocationRelativeTo(null);
		contentPane = new JPanel() {
			/**
			 * 
			 */
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
		// Sol kenara resim ekleme
		JLabel leftImageLabel = new JLabel();
		leftImageLabel.setIcon(new ImageIcon(getClass().getResource("inek1.jpg")));
		leftImageLabel.setBounds(-199, 0, 435, 476);
		contentPane.add(leftImageLabel);
		JLabel username_label = new JLabel("Kullanıcı Adı:");
		username_label.setFont(new Font("Tahoma", Font.BOLD, 13));
		username_label.setForeground(Color.BLACK);
		username_label.setBounds(318, 39, 136, 38);
		contentPane.add(username_label);

		JLabel sifre_label = new JLabel("Şifre");
		sifre_label.setForeground(Color.BLACK);
		sifre_label.setFont(new Font("Tahoma", Font.BOLD, 13));
		sifre_label.setBounds(334, 106, 80, 38);
		contentPane.add(sifre_label);

		usernameTextField = new JTextField();
		usernameTextField.setBackground(Color.ORANGE);
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(318, 87, 96, 19);
		contentPane.add(usernameTextField);

		passwordField = new JPasswordField();
		passwordField.setBackground(Color.ORANGE);
		passwordField.setColumns(10);
		passwordField.setBounds(318, 140, 96, 19);
		contentPane.add(passwordField);

		JButton girisbutton = new RoundButton("Giriş");
		girisbutton.setBackground(Color.GREEN);
		girisbutton.setForeground(new Color(0, 128, 0));
		girisbutton.setFont(new Font("Tahoma", Font.BOLD, 15));
		girisbutton.setBounds(318, 185, 96, 44);
		contentPane.add(girisbutton);

		JLabel lblNewLabel_2 = new JLabel("LOGİN");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBounds(334, 10, 108, 38);
		contentPane.add(lblNewLabel_2);

		JButton kayitbutton = new JButton("Kayıt Ol");
		kayitbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				KayıtWindow kayıtWindow = new KayıtWindow();
				kayıtWindow.setVisible(true);
				dispose();

			}
		});
		kayitbutton.setBackground(Color.RED);
		kayitbutton.setBounds(333, 259, 81, 21);
		contentPane.add(kayitbutton);

		girisbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login(); // Giriş
			}
		});

		usernameTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login(); // Giriş
			}
		});
		passwordField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (passwordField.hasFocus()) {
					login();
				}
			}
		});

		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login(); // Giriş
			}
		});
	}

	private void login() {
		String enteredUsername = usernameTextField.getText();
		String enteredPassword = new String(passwordField.getPassword());

		// Check if either username or password is empty
		if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Kullanıcı adı veya şifre boş olamaz!");
			return; // Exit the method
		}

		try {
			con = DriverManager.getConnection(url, username, password);
			PreparedStatement pst = con.prepareStatement("SELECT * FROM t_login WHERE username=? AND password=?");
			pst.setString(1, enteredUsername);
			pst.setString(2, enteredPassword);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				String ad = rs.getString("ad");
				String soyad = rs.getString("soyad");
				String mail = rs.getString("mail");
				// İlgili verileri Login sınıfına set edin
				login.setPassword(enteredPassword);
				login.setUsername(enteredUsername);
				login.setAd(ad);
				login.setSoyad(soyad);
				login.setMail(mail);
				JOptionPane.showMessageDialog(null, "Hoş geldiniz " + ad + " " + soyad + "!");
				openMainMenu();
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Hatalı kullanıcı adı veya şifre!");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Veritabanı hatası: " + ex.getMessage());
		}
	}

	private void openMainMenu() {
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}
}

class RoundButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoundButton(String label) {
		super(label);
		setContentAreaFilled(false);
	}

	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) {
			g.setColor(Color.lightGray);
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

		super.paintComponent(g);
	}

	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
	}

	Shape shape;

	public boolean contains(int x, int y) {
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}
}
