package main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DetayliIrk extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable turDetayTable;
    private JTable hayvanListeTable;
    private JComboBox<String> turComboBox;
    private JButton pdfOlusturButton;

    // Veritabanı bağlantısı bilgileri
    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";

    // Veritabanı bağlantısını oluştur
    public Connection baglan() throws SQLException {
        return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
    }

    // Constructor
    public DetayliIrk() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(DetayliIrk.class.getResource("/main/inek.jpg")));
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
                return new Dimension(400, 400);
            }
        };
        contentPane.setForeground(SystemColor.inactiveCaptionText);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        turComboBox = new JComboBox<>();
        turComboBox.setBounds(312, 30, 200, 25);
        contentPane.add(turComboBox);

        JLabel lblTur = new JLabel("Tür Seç:");
        lblTur.setBounds(250, 30, 70, 25);
        contentPane.add(lblTur);

        JScrollPane turDetayScrollPane = new JScrollPane();
        turDetayScrollPane.setBounds(50, 80, 700, 150);
        contentPane.add(turDetayScrollPane);

        turDetayTable = new JTable();
        turDetayScrollPane.setViewportView(turDetayTable);

        JScrollPane hayvanListeScrollPane = new JScrollPane();
        hayvanListeScrollPane.setBounds(50, 250, 700, 150);
        contentPane.add(hayvanListeScrollPane);

        hayvanListeTable = new JTable();
        hayvanListeScrollPane.setViewportView(hayvanListeTable);

        // Tür ComboBox'ını doldur
        turComboBoxDoldur();

        // Tabloları güncelle
        turComboBox.addActionListener(e -> {
            String secilenTur = (String) turComboBox.getSelectedItem();
            if (secilenTur != null) {
                turDetayGuncelle(secilenTur);
                hayvanListeGuncelle(secilenTur);
            }
        });

        // Hayvan tablosuna tıklanınca
        hayvanListeTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = hayvanListeTable.getSelectedRow();
                int columns = hayvanListeTable.getColumnCount();
                StringBuilder message = new StringBuilder();

                for (int i = 0; i < columns; i++) {
                    String columnName = hayvanListeTable.getColumnName(i);
                    String value = String.valueOf(hayvanListeTable.getValueAt(row, i));
                    message.append(columnName).append(": ").append(value).append("\n");
                }

                JOptionPane.showMessageDialog(null, message.toString(), "Seçilen Satır", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // Tur detay tablosuna tıklanınca
        turDetayTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = turDetayTable.getSelectedRow();
                int columns = turDetayTable.getColumnCount();
                StringBuilder message = new StringBuilder();

                for (int i = 0; i < columns; i++) {
                    String columnName = turDetayTable.getColumnName(i);
                    String value = String.valueOf(turDetayTable.getValueAt(row, i));
                    message.append(columnName).append(": ").append(value).append("\n");
                }

                JOptionPane.showMessageDialog(null, message.toString(), "Seçilen Satır", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        // PDF Oluşturma butonu
        pdfOlusturButton = new JButton("PDF Oluştur");
        pdfOlusturButton.setBounds(600, 30, 150, 25);
        pdfOlusturButton.setBackground(Color.ORANGE);
        contentPane.add(pdfOlusturButton);
        pdfOlusturButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pdfOlustur();
            }
        });
    }

    // Tür ComboBox'ını doldur
    private void turComboBoxDoldur() {
        try (Connection conn = baglan();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT tur FROM hayvanlar")) {

            while (rs.next()) {
                turComboBox.addItem(rs.getString("tur"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tür detaylarını güncelle
    private void turDetayGuncelle(String secilenTur) {
        DefaultTableModel model = new DefaultTableModel();
        turDetayTable.setModel(model);
        model.addColumn("Tür");
        model.addColumn("Doğal Yem");
        model.addColumn("Hazır Yem");
        model.addColumn("Günlük Su");
        model.addColumn("Aşılama");
        model.addColumn("Tırnak Bakım");
        model.addColumn("Gezginlik");
        model.addColumn("Süt Verim");
        model.addColumn("Max Yavrulama");
        model.addColumn("Notlar");

        try (Connection conn = baglan();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM hayvanlar WHERE tur = ?")) {
            pstmt.setString(1, secilenTur);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {rs.getString("tur"), rs.getInt("dogal_yem"), rs.getInt("hazir_yem"),
                        rs.getInt("gunluk_su"), rs.getString("asilama"), rs.getString("tirnak_bakim"),
                        rs.getString("gezginlik"), rs.getInt("sut_verim"), rs.getInt("max_yavrulama"),
                        rs.getString("notlar")};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hayvan listesini güncelle
    private void hayvanListeGuncelle(String secilenTur) {
        DefaultTableModel model = new DefaultTableModel();
        hayvanListeTable.setModel(model);
        model.addColumn("Irk");
        model.addColumn("Küpe Numarası");
        model.addColumn("Doğum Tarihi");
        model.addColumn("Hastalıkları");
        model.addColumn("Yavrulama Sayısı");
        model.addColumn("Cinsiyet");
        model.addColumn("Kilo");
        model.addColumn("Günlük Süt");
        model.addColumn("Tüketilen Besin (kg)");
        model.addColumn("Fiyat");
        model.addColumn("Bilgi");

        try (Connection conn = baglan();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM hayvan WHERE irk = ?")) {
            pstmt.setString(1, secilenTur);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = {rs.getString("irk"), rs.getString("kuepe_numarasi"), rs.getDate("dogum_tarihi").toString(),
                        rs.getString("hastaliklari"), rs.getInt("yavrulama_sayisi"), rs.getString("cinsiyet"),
                        rs.getBigDecimal("kilo"), rs.getBigDecimal("gunluk_sut"), rs.getBigDecimal("tuketilen_besin_kg"),
                        rs.getBigDecimal("fiyat"), rs.getString("bilgi")};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void pdfOlustur() {
        String selectedTur = (String) turComboBox.getSelectedItem();
        if (selectedTur == null) {
            JOptionPane.showMessageDialog(null, "Lütfen bir tür seçin.");
            return;
        }

        String sirketAdi = "Hızlı Erkek Yurtları A.Ş.";
        String sirketAdresi = "Kavaklıdere Mah. Bardacık Sokak No: 20 Ankara/Çankaya";
        String sirketTelefon = "+90545 654 4835";

        File logoFile = new File("C:\\Users\\Eray\\Desktop\\Gör_Proje\\er.png");

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\Eray\\Desktop\\Sistem_Analizi\\Roboto-Bold.ttf"));
            contentStream.setFont(font, 12);

            PDImageXObject logoImage = PDImageXObject.createFromFileByContent(logoFile, document);
            contentStream.drawImage(logoImage, 50, 750, logoImage.getWidth() / 4, logoImage.getHeight() / 4);

            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("HAYVAN BİLGİSİ");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(sirketAdi);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(sirketAdresi);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(sirketTelefon);
            contentStream.newLineAtOffset(0, -20);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Tür: " + selectedTur);
            contentStream.newLineAtOffset(0, -20);
            contentStream.endText();

            float bottomMargin = 600; // Table start height

            // Draw the tables
            bottomMargin = drawTable(document, contentStream, font, bottomMargin, turDetayTable);
            bottomMargin = drawTable(document, contentStream, font, bottomMargin, hayvanListeTable);

            contentStream.close();

            File file = new File("hayvan_bilgisi_" + selectedTur + ".pdf");
            document.save(file);
            document.close();

            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "PDF oluşturulurken bir hata oluştu: " + ex.getMessage());
        }
    }

    private float drawTable(PDDocument document, PDPageContentStream contentStream, PDType0Font font, float startY, JTable table) throws IOException {
        float margin = 50;
        float yStart = startY;
        float tableWidth = 1000; // Tablo genişliği
        float yPosition = yStart;
        float rowHeight = 40;
        float cellMargin = 2;

        int rowCount = table.getRowCount();
        int colCount = table.getColumnCount();
        float tableHeight = rowHeight * rowCount + 1;

        // Draw the table header
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(margin, yPosition);
        for (int i = 0; i < colCount; i++) {
            contentStream.showText(table.getColumnName(i));
            contentStream.newLineAtOffset(tableWidth / colCount, 0);
        }
        contentStream.endText();

        yPosition -= rowHeight;

        // Draw table content
        for (int i = 0; i < rowCount; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin + cellMargin, yPosition);
            for (int j = 0; j < colCount; j++) {
                String cellValue = String.valueOf(table.getValueAt(i, j));
                contentStream.showText(cellValue);
                contentStream.newLineAtOffset(tableWidth / colCount, 0);
            }
            contentStream.endText();
            yPosition -= rowHeight;
        }

        // Draw table border lines
        contentStream.moveTo(margin, yStart);
        contentStream.lineTo(margin, yStart - tableHeight);
        contentStream.moveTo(margin + tableWidth, yStart);
        contentStream.lineTo(margin + tableWidth, yStart - tableHeight);
        for (int i = 0; i <= rowCount; i++) {
            contentStream.moveTo(margin, yStart - i * rowHeight);
            contentStream.lineTo(margin + tableWidth, yStart - i * rowHeight);
        }

        contentStream.stroke();

        return yPosition - margin;
    }




    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            try {
                DetayliIrk frame = new DetayliIrk();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
