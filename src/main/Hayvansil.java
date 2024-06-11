package main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Hayvansil extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTable table;

    // PostgreSQL veritabanı bağlantı bilgileri
    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";

    // Veritabanı bağlantısını oluştur
    public Connection baglan() throws SQLException {
        return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
    }

    // Seçili hayvanı sil
    public void hayvanSil(int hayvanID) {
        try (Connection conn = baglan()) {
            String sql = "DELETE FROM hayvan WHERE hayvan_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, hayvanID);
            int etkilenenSatirSayisi = preparedStatement.executeUpdate();
            if (etkilenenSatirSayisi > 0) {
                JOptionPane.showMessageDialog(null, "Hayvan başarıyla silindi.");
                // Tabloyu güncelle
                tabloyuGuncelle();
            } else {
                JOptionPane.showMessageDialog(null, "Hayvan silinirken bir hata oluştu.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tabloyu güncelle
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

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Hayvansil frame = new Hayvansil();
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
    public Hayvansil() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 400);

    	setIconImage(Toolkit.getDefaultToolkit().getImage(KayıtWindow.class.getResource("/main/inek.jpg")));

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        scrollPane.setViewportView(table);

        JButton btnHayvanSil = new JButton("Hayvanı Sil");
        btnHayvanSil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) { // Bir satır seçilmiş mi?
                    int hayvanID = (int) table.getValueAt(selectedRow, 0);
                    int confirmResult = JOptionPane.showConfirmDialog(null, "Seçili hayvanı silmek istiyor musunuz?", "Hayvan Silme Onayı", JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        hayvanSil(hayvanID);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lütfen silmek istediğiniz hayvanı seçin.", "Hayvan Seçilmemiş", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        contentPane.add(btnHayvanSil, BorderLayout.SOUTH);

        // Uygulama başladığında tabloyu güncelleyin
        tabloyuGuncelle();
    }
}
