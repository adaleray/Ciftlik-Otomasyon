package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MainMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final String URL = "jdbc:postgresql://localhost:5432/farm";
	private static final String KULLANICI_ADI = "postgres";
	private static final String SIFRE = "123";
	private static final String gnmail = "jetshoperay@gmail.com";
	private static final String gnpass = "oxkwuujfbzvvoooc";

	boolean dogalYemUyariGosterildi = false;
	private boolean hazirYemUyariGosterildi = false;

	public static Connection connect() throws SQLException {
		return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
	}

	public static void main(String[] args) {
		try (Connection conn = connect()) {
			if (conn != null) {
				System.out.println("Veritabanına başarıyla bağlandı!");
			} else {
				System.out.println("Veritabanına bağlantı yapılamadı!");
			}
		} catch (SQLException e) {
			System.out.println("Veritabanı bağlantısı başarısız! Hata: " + e.getMessage());
		}
		EventQueue.invokeLater(() -> {
			try {
				MainMenu frame = new MainMenu();
				UIManager.setLookAndFeel(new FlatLightLaf());
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public MainMenu() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainMenu.class.getResource("/main/inek.jpg")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 804, 446);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu hayvanMenu = new JMenu("Hayvan Menü");
		menuBar.add(hayvanMenu);

		JMenu hayvanEkle = new JMenu("Hayvan Ekle");
		hayvanEkle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HayvanEkle hayvanEkleFrame = new HayvanEkle();
				hayvanEkleFrame.setVisible(true);
			}
		});
		hayvanMenu.add(hayvanEkle);

		JMenu hayvanSil = new JMenu("Hayvan Sil");
		hayvanSil.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Hayvansil hayvanSilFrame = new Hayvansil();
				hayvanSilFrame.setVisible(true);
			}
		});
		hayvanMenu.add(hayvanSil);

		JMenu hayvanDuzenle = new JMenu("Hayvan Düzenle");
		hayvanDuzenle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Hayvandüzenle hayvanDuzenleFrame = new Hayvandüzenle();
				hayvanDuzenleFrame.setVisible(true);
			}
		});
		hayvanMenu.add(hayvanDuzenle);

		JMenu hayvanBilgi = new JMenu("Irk Bilgi");
		menuBar.add(hayvanBilgi);

		JMenu irkEkleMenu = new JMenu("Irk Ekle");
		irkEkleMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				HayvanBilgi hayvanBilgiFrame = new HayvanBilgi();
				hayvanBilgiFrame.setVisible(true);
			}
		});
		hayvanBilgi.add(irkEkleMenu);

		JMenu irkBilgiMenu = new JMenu("Ait hayvanlar");
		irkBilgiMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DetayliIrk detayliIrkFrame = new DetayliIrk();
				detayliIrkFrame.setVisible(true);
			}
		});
		hayvanBilgi.add(irkBilgiMenu);

		JMenu ciftlikMenu = new JMenu("Çiftlik Detay");
		menuBar.add(ciftlikMenu);

		JMenu yemeyenmenu = new JMenu("Hasta Olabilir");
		yemeyenmenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EksikYemTablo eksikYemTablo = new EksikYemTablo();
				eksikYemTablo.setVisible(true);
			}
		});
		ciftlikMenu.add(yemeyenmenu);

		JMenu personelMenu = new JMenu("Personel ");
		menuBar.add(personelMenu);

		JMenu personelEkle = new JMenu("Personel Ekle");
		personelEkle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				perekle perekleFrame = new perekle();
				perekleFrame.setVisible(true);
			}
		});
		personelMenu.add(personelEkle);

		JMenu personelDuzenle = new JMenu("Personel Düzenle");
		personelDuzenle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				perduzenle perduzenleFrame = new perduzenle();
				perduzenleFrame.setVisible(true);
			}
		});
		personelMenu.add(personelDuzenle);

		JMenu personelAra = new JMenu("Personel Ara");
		personelAra.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PerAra perAraFrame = new PerAra();
				perAraFrame.setVisible(true);
			}
		});
		personelMenu.add(personelAra);

		JMenu stokmenu = new JMenu("Stok Ekle");
		stokmenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				stokekle stokekle = new stokekle();
				stokekle.setVisible(true);
			}
		});
		menuBar.add(stokmenu);
		
		JMenu mnNewMenu_1 = new JMenu("Sipariş Ver");
		mnNewMenu_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SiparisVer  siparisVer = new SiparisVer();
				siparisVer.setVisible(true);
			}
		});
		menuBar.add(mnNewMenu_1);

		JMenu ayarlarmenu = new JMenu("Ayarlar");
		menuBar.add(ayarlarmenu);
		
		JMenu ayarlarhesapmenu = new JMenu("Hesap");
		ayarlarhesapmenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AyarlarHesap ayarlarHesap = new AyarlarHesap();
				ayarlarHesap.setVisible(true);
			}
		});
		ayarlarmenu.add(ayarlarhesapmenu);
		
		JMenu mnNewMenu = new JMenu("Şirket Ekle");
		mnNewMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SirketEkle sirketEkle = new SirketEkle();
				sirketEkle.setVisible(true);
			}
		});
		ayarlarmenu.add(mnNewMenu);

		JMenu cikismenu = new JMenu("Çıkış yap (" + " " + login.getAd()+")");
		cikismenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String[] options = { "Oturumu Kapat", "Uygulamayı Kapat", "İptal" };
				int choice = JOptionPane.showOptionDialog(null, "Çıkış yapmak istiyor musunuz?", "Çıkış",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (choice == 0) {
					LoginWindow loginWindow = new LoginWindow();
					loginWindow.setVisible(true);
					dispose(); // Ana pencereyi kapat
				} else if (choice == 1) {
					System.exit(0); // Uygulamayı Kapat seçeneği
				}
			}
		});
		cikismenu.setForeground(Color.RED);
		menuBar.add(cikismenu);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JScrollPane stokScrollPane = new JScrollPane();
		tabbedPane.addTab("Stok Bilgisi", null, stokScrollPane, null);

		JTable stokTable = new JTable();
		stokScrollPane.setViewportView(stokTable);
		loadStokTable(stokTable);

		JScrollPane hayvanSayisiScrollPane = new JScrollPane();
		tabbedPane.addTab("Hayvan Sayısı", null, hayvanSayisiScrollPane, null);

		JTable hayvanSayisiTable = new JTable();
		hayvanSayisiScrollPane.setViewportView(hayvanSayisiTable);
		loadHayvanSayisiTable(hayvanSayisiTable);

		JScrollPane irkSayisiScrollPane = new JScrollPane();
		tabbedPane.addTab("Irk Bazında Hayvan Sayısı", null, irkSayisiScrollPane, null);

		JTable irkSayisiTable = new JTable();
		irkSayisiScrollPane.setViewportView(irkSayisiTable);
		loadIrkSayisiTable(irkSayisiTable);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {

				updateStockDaily();
				kontrolVeSorgu();
				eksikYemKontrol();
			}
		});
	}

	private void loadStokTable(JTable table) {
		String[] columnNames = { "Doğal Yem", "Hazır Yem", "Son Güncelleme Tarihi", "Günlük Yem İhtiyacı",
				"Kaç Gün Yetecek" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		String sql = "SELECT dogalyem, haziryem, son_guncelleme_tarihi FROM public.stok";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				Vector<Object> row = new Vector<>();
				double dogalyem = rs.getDouble("dogalyem");
				double haziryem = rs.getDouble("haziryem");
				row.add(dogalyem);
				row.add(haziryem);
				row.add(rs.getDate("son_guncelleme_tarihi"));

				double[] dailyNeeds = calculateDailyNeeds();
				double dailyNeedDogal = dailyNeeds[0];
				double dailyNeedHazir = dailyNeeds[1];
				row.add(dailyNeedDogal + dailyNeedHazir);

				double daysLeftDogal = dogalyem / dailyNeedDogal;
				double daysLeftHazir = haziryem / dailyNeedHazir;
				double daysLeft = Math.min(daysLeftDogal, daysLeftHazir);
				row.add(daysLeft);

				model.addRow(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		table.setModel(model);
	}

	private void loadHayvanSayisiTable(JTable table) {
		String[] columnNames = { "Toplam Hayvan Sayısı" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		String sql = "SELECT COUNT(*) AS toplam FROM public.hayvan";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				Vector<Object> row = new Vector<>();
				row.add(rs.getInt("toplam"));
				model.addRow(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		table.setModel(model);
	}

	private void loadIrkSayisiTable(JTable table) {
		String[] columnNames = { "Irk", "Hayvan Sayısı" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		String sql = "SELECT irk, COUNT(*) AS sayi FROM public.hayvan GROUP BY irk";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				Vector<Object> row = new Vector<>();
				row.add(rs.getString("irk"));
				row.add(rs.getInt("sayi"));
				model.addRow(row);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		table.setModel(model);
	}

	private void updateStockDaily() {
		LocalDate today = LocalDate.now();
		LocalDate lastUpdateDate = getLastUpdateDate();
		if (lastUpdateDate == null || !today.isEqual(lastUpdateDate)) {
			String sql = "SELECT DISTINCT irk FROM public.hayvan";
			try (Connection conn = connect();
					PreparedStatement pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery()) {

				while (rs.next()) {
					String irk = rs.getString("irk");
					Object[] foodRequirements = getFoodRequirements(irk);

					stokDus((double) foodRequirements[0], (double) foodRequirements[1]);

					System.out.println("Stok güncellendi: " + irk);
				}

				updateLastUpdateDate(today);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private Object[] getFoodRequirements(String irk) {
		Object[] foodRequirements = new Object[2];
		String sql = "SELECT * FROM public.calculate_food_requirement(?)";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, irk);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					foodRequirements[0] = rs.getDouble(1); // Doğal
					foodRequirements[1] = rs.getDouble(2); // Hazır
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return foodRequirements;
	}

	private LocalDate getLastUpdateDate() {
		LocalDate lastUpdateDate = null;
		String sql = "SELECT son_guncelleme_tarihi FROM public.stok LIMIT 1";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				java.sql.Date date = rs.getDate("son_guncelleme_tarihi");
				if (date != null) {
					lastUpdateDate = date.toLocalDate();
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return lastUpdateDate;
	}

	private void updateLastUpdateDate(LocalDate date) {
		String sql = "UPDATE public.stok SET son_guncelleme_tarihi = ?";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setDate(1, java.sql.Date.valueOf(date));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private double[] calculateDailyNeeds() {
		double dailyNeedDogal = 0.0;
		double dailyNeedHazir = 0.0;
		String sql = "SELECT irk, COUNT(*) AS sayi FROM public.hayvan GROUP BY irk";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				String irk = rs.getString("irk");
				int count = rs.getInt("sayi");
				Object[] foodRequirements = getFoodRequirements(irk);
				dailyNeedDogal += count * (double) foodRequirements[0];
				dailyNeedHazir += count * (double) foodRequirements[1];
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return new double[] { dailyNeedDogal, dailyNeedHazir };
	}

// stok düş ve uyarı sistemi aç
	private void stokDus(double dogalYemMiktar, double hazirYemMiktar) {
		String sqlUpdate = "UPDATE public.stok SET dogalyem = dogalyem - ?, haziryem = haziryem - ?";
		String sqlCheck = "SELECT dogalyem, haziryem FROM public.stok LIMIT 1";
		try (Connection conn = connect();
				PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
				PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {

			pstmtUpdate.setDouble(1, dogalYemMiktar);
			pstmtUpdate.setDouble(2, hazirYemMiktar);
			pstmtUpdate.executeUpdate();

			try (ResultSet rs = pstmtCheck.executeQuery()) {
				if (rs.next()) {
					double currentDogalYem = rs.getDouble("dogalyem");
					double currentHazirYem = rs.getDouble("haziryem");

					String userEmail = login.getMail();

					if ((currentDogalYem < 3000 && !dogalYemUyariGosterildi)
							|| (currentHazirYem < 3000 && !hazirYemUyariGosterildi)) {
						String message = "Yem Stoğunuz 3000 kg'nin altına düştü! Kontrol Ediniz.\n";
						message += "Kalan Doğal Yem: " + currentDogalYem + " kg\n";
						message += "Kalan Hazır Yem: " + currentHazirYem + " kg";

						if (currentDogalYem < 3000 && !dogalYemUyariGosterildi) {
							JOptionPane.showMessageDialog(null, message, "Uyarı", JOptionPane.WARNING_MESSAGE);
							dogalYemUyariGosterildi = true;

							sendEmail("Yem Stoğu Uyarısı", message, userEmail);
							showOrderDialog(); 
						}

						if (currentHazirYem < 0 || currentDogalYem < 0) {
							String criticalMessage = "Yeminiz tükendi, bugün yetmeyecek!";
							JOptionPane.showMessageDialog(null, criticalMessage, "Kritik Uyarı",
									JOptionPane.ERROR_MESSAGE);

							// gerekli maili gonderiyorm
							sendEmail("Kritik Yem Uyarısı", criticalMessage, userEmail);
						}
					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void sendEmail(String subject, String body, String email) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); // Gmail's SMTP server address
		props.put("mail.smtp.port", "587");// mail portu 587 olarak gmail içnidir

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(gnmail, gnpass);
			}
		});

		try {
			Message message = new MimeMessage(session);
			// session.setDebug(true);
			// burada debug mod acılır mail gonderir sistemi testleri yapılır.

			message.setFrom(new InternetAddress(gnmail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

			System.out.println("E-posta gönderildi.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private void kontrolVeSorgu() {
		LocalDate today = LocalDate.now();
		LocalDate lastQueryDate = getLastQueryDate();

		if (lastQueryDate == null || !today.isEqual(lastQueryDate)) {
			int response = JOptionPane.showConfirmDialog(null, "Bugün hayvanlardan eksik yem yiyen oldu mu?",
					"Günlük Yem Kontrolü", JOptionPane.YES_NO_OPTION);
			System.out.println("soru soruldu");
			if (response == JOptionPane.YES_OPTION) {
				EksikYemGirisi eksikYemGirisiFrame = new EksikYemGirisi();
				eksikYemGirisiFrame.setVisible(true);
			}

			updateQueryDate(today);
		}
	}

	private LocalDate getLastQueryDate() {
		LocalDate lastQueryDate = null;
		String sql = "SELECT tarih FROM public.sorgutarih LIMIT 1";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				java.sql.Date date = rs.getDate("tarih");
				if (date != null) {
					lastQueryDate = date.toLocalDate();
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return lastQueryDate;
	}

	private void updateQueryDate(LocalDate date) {
		String sqlDelete = "DELETE FROM public.sorgutarih";
		String sqlInsert = "INSERT INTO public.sorgutarih (tarih) VALUES (?)";
		try (Connection conn = connect();
				PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);
				PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
			pstmtDelete.executeUpdate();
			pstmtInsert.setDate(1, java.sql.Date.valueOf(date));
			pstmtInsert.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void yemUpdateQueryDate(LocalDate date) {
		String sqlDelete = "DELETE FROM public.yemsorgutarih";
		String sqlInsert = "INSERT INTO public.yemsorgutarih (tarih) VALUES (?)";
		try (Connection conn = connect();
				PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);
				PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
			pstmtDelete.executeUpdate();
			pstmtInsert.setDate(1, java.sql.Date.valueOf(date));
			pstmtInsert.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private void eksikYemKontrol() {
		LocalDate today = LocalDate.now();
		LocalDate lastCheckedDate = getLastCheckedDate();

		if (lastCheckedDate == null || !today.isEqual(lastCheckedDate)) {
			String sql = "SELECT e1.kuepe_numarasi " + "FROM public.eksik_yem e1 "
					+ "JOIN public.eksik_yem e2 ON e1.kuepe_numarasi = e2.kuepe_numarasi "
					+ "WHERE e1.tarih = ? AND e2.tarih = ? ";
			try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setDate(1, java.sql.Date.valueOf(today.minusDays(1)));
				pstmt.setDate(2, java.sql.Date.valueOf(today.minusDays(2)));
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						String kuepeNumarasi = rs.getString("kuepe_numarasi");
						String bali = "Hayvan Hasta Olabilir.";
						String message = "Son 2 gün içerisinde eksik yem yiyen bir hayvan tespit edildi! Kulak Numarası: "
								+ kuepeNumarasi;
						String eraymail = login.getMail();
						sendEmail(bali, message, eraymail);
					} else {
						System.out.println("Son 2 gün içerisinde eksik yem yiyen hayvan bulunamadı.");
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			yemUpdateQueryDate(today);
		} else {
			System.out.println("Bugün zaten eksik yem kontrolü yapıldı.");
		}
	}

	private LocalDate getLastCheckedDate() {
		LocalDate lastCheckedDate = null;
		String sql = "SELECT tarih FROM public.yemsorgutarih ORDER BY tarih DESC LIMIT 1";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				java.sql.Date date = rs.getDate("tarih");
				if (date != null) {
					lastCheckedDate = date.toLocalDate();
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return lastCheckedDate;
	}
	
	private void showOrderDialog() {
        int response = JOptionPane.showConfirmDialog(null, "Yem stoğunuz azaldı. Yem siparişi vermek ister misiniz?", "Yem Siparişi", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            YemSiparisFormu siparisFormu = new YemSiparisFormu();
            siparisFormu.setVisible(true);
        }
    }



    public class YemSiparisFormu extends JFrame {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField dogalYemMiktarField;
        private JTextField hazirYemMiktarField;
        private JComboBox<String> sirketComboBox;

        public YemSiparisFormu() {
            setTitle("Yem Siparişi Ver");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new GridLayout(5, 2));

            JLabel dogalYemLabel = new JLabel("Doğal Yem Miktarı (kg):");
            dogalYemMiktarField = new JTextField();
            JLabel hazirYemLabel = new JLabel("Hazır Yem Miktarı (kg):");
            hazirYemMiktarField = new JTextField();
            JLabel sirketLabel = new JLabel("Şirket Seçin:");
            sirketComboBox = new JComboBox<>();

            loadCompanies();

            JButton gonderButton = new JButton("Siparişi Gönder");
            gonderButton.addActionListener(e -> gonderSiparis());

            add(dogalYemLabel);
            add(dogalYemMiktarField);
            add(hazirYemLabel);
            add(hazirYemMiktarField);
            add(sirketLabel);
            add(sirketComboBox);
            add(new JLabel());
            add(gonderButton);
        }

        private void loadCompanies() {
            String sql = "SELECT ad FROM public.sirketler";
            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sirketComboBox.addItem(rs.getString("ad"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        private void gonderSiparis() {
            String dogalYemMiktar = dogalYemMiktarField.getText();
            String hazirYemMiktar = hazirYemMiktarField.getText();
            String sirket = (String) sirketComboBox.getSelectedItem();
            String sql = "SELECT ad, email FROM public.sirketler WHERE ad = ?";

            try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sirket);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String sirketMail = rs.getString("email");
                        String sirketad = rs.getString("ad");
                        String subject = "Yem Siparişi";
                        String body = "Merhaba " + sirketad + ",\n\n" + dogalYemMiktar + " kg doğal yem ve " + hazirYemMiktar + " kg hazır yem siparişi vermek istiyoruz.\n\nTeşekkürler.";
                        sendEmail(subject, body, sirketMail);
                        JOptionPane.showMessageDialog(this, "Sipariş gönderildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }



}}