package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EksikYemTablo extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;

    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EksikYemTablo frame = new EksikYemTablo();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public EksikYemTablo() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("AZ YEM YİYEN HAYVANLAR");
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
                return new Dimension(400, 400);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 10, 760, 360);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // Tablo verilerini yükle
        loadTableData();

        // Tablo satır seçildiğinde olay dinleyicisi ekle
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        showHayvanBilgileri(selectedRow);
                    }
                }
            }
        });

        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
    }

    private void loadTableData() {
        String query = "SELECT kuepe_numarasi, tarih, dogal_yem_miktar, hazir_yem_miktar FROM public.eksik_yem";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DefaultTableModel model = new DefaultTableModel() {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn("Küpe Numarası");
            model.addColumn("Az Yem Yediği Tarih");
            model.addColumn("Yenilen Doğal Yem Miktarı");
            model.addColumn("Yenilen Hazır Yem Miktarı");
            model.addColumn("Detay");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kuepe_numarasi"),
                    rs.getDate("tarih"),
                    rs.getBigDecimal("dogal_yem_miktar"),
                    rs.getBigDecimal("hazir_yem_miktar"),
                    "Detay"
                });
            }

            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showHayvanBilgileri(int selectedRow) {
        String kuepeNumarasi = (String) table.getValueAt(selectedRow, 0);

        String query = "SELECT irk, hastaliklari, dogum_tarihi, yavrulama_sayisi, cinsiyet, kilo, gunluk_sut, bilgi "
                     + "FROM public.hayvan WHERE kuepe_numarasi = '" + kuepeNumarasi + "'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            StringBuilder message = new StringBuilder();
            if (rs.next()) {
                message.append("Hayvan Irkı: ").append(rs.getString("irk")).append("\n");
                message.append("Hastalıkları: ").append(rs.getString("hastaliklari")).append("\n");
                message.append("Doğum Tarihi: ").append(rs.getDate("dogum_tarihi")).append("\n");
                message.append("Yavrulama Sayısı: ").append(rs.getInt("yavrulama_sayisi")).append("\n");
                message.append("Cinsiyet: ").append(rs.getString("cinsiyet")).append("\n");
                message.append("Kilo: ").append(rs.getBigDecimal("kilo")).append("\n");
                message.append("Günlük Süt: ").append(rs.getBigDecimal("gunluk_sut")).append("\n");
                message.append("Bilgi: ").append(rs.getString("bilgi")).append("\n");

                int result = JOptionPane.showOptionDialog(this, message.toString(), "Hayvan Bilgileri", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Hastalıkları Güncelle", "Bilgiyi Güncelle", "Kapat"}, "Kapat");
                if (result == JOptionPane.YES_OPTION) {
                    String newHastaliklar = JOptionPane.showInputDialog(this, "Yeni Hastalıkları Giriniz:");
                    if (newHastaliklar != null && !newHastaliklar.isEmpty()) {
                        String updateQuery = "UPDATE public.hayvan SET hastaliklari = CONCAT(hastaliklari, ', ', '" + newHastaliklar + "') WHERE kuepe_numarasi = '" + kuepeNumarasi + "'";
                        try (Statement updateStmt = conn.createStatement()) {
                            updateStmt.executeUpdate(updateQuery);
                            JOptionPane.showMessageDialog(this, "Hastalıklar başarıyla güncellendi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (result == JOptionPane.NO_OPTION) {
                    String newBilgi = JOptionPane.showInputDialog(this, "Yeni Bilgiyi Giriniz:");
                    if (newBilgi != null && !newBilgi.isEmpty()) {
                        String updateQuery = "UPDATE public.hayvan SET bilgi = '" + newBilgi + "' WHERE kuepe_numarasi = '" + kuepeNumarasi + "'";
                        try (Statement updateStmt = conn.createStatement()) {
                            updateStmt.executeUpdate(updateQuery);
                            JOptionPane.showMessageDialog(this, "Bilgi başarıyla güncellendi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                

                }
            } else {
                JOptionPane.showMessageDialog(this, "Hayvan bilgisi bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }}

   
