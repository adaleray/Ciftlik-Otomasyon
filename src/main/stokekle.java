package main;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.formdev.flatlaf.FlatLightLaf;

import java.util.Vector;

public class stokekle extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField dogalYemField;
    private JTextField hazirYemField;
    private JTable table;

    // Veritabanı bağlantı bilgileri
    private static final String URL = "jdbc:postgresql://localhost:5432/farm";
    private static final String KULLANICI_ADI = "postgres";
    private static final String SIFRE = "123";

    /**
     * Launch the application.
     */
    
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    stokekle frame = new stokekle();
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
    public stokekle() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(stokekle.class.getResource("/main/inek.jpg")));
        setTitle("Stok Ekle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 474, 345);
        

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

        JLabel lblNewLabel = new JLabel("Doğal Yem :");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(20, 36, 95, 13);
        contentPane.add(lblNewLabel);

        dogalYemField = new JTextField();
        dogalYemField.setBounds(133, 35, 76, 19);
        contentPane.add(dogalYemField);
        dogalYemField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Hazır Yem:");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_1.setBounds(20, 103, 82, 13);
        contentPane.add(lblNewLabel_1);

        hazirYemField = new JTextField();
        hazirYemField.setBounds(133, 102, 76, 19);
        contentPane.add(hazirYemField);
        hazirYemField.setColumns(10);

        JButton azaltButton = new JButton("Stok Azalt");
        azaltButton.setBounds(269, 34, 95, 21);
        contentPane.add(azaltButton);

        JButton eklebutton = new JButton("Stok Ekle");
        eklebutton.setBounds(269, 101, 95, 21);
        contentPane.add(eklebutton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(8, 175, 442, 113);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBounds(0, 0, 788, 22);
        contentPane.add(menuBar);

        eklebutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int dogalYem = Integer.parseInt(dogalYemField.getText());
                    int hazirYem = Integer.parseInt(hazirYemField.getText());
                    updateStock("dogalyem", dogalYem, true);
                    updateStock("haziryem", hazirYem, true);
                    JOptionPane.showMessageDialog(null, dogalYem + " kg doğal yem  ve " + hazirYem + " kg hazır yem başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException | NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Stok ekleme işlemi sırasında bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        azaltButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int dogalYem = Integer.parseInt(dogalYemField.getText());
                    int hazirYem = Integer.parseInt(hazirYemField.getText());
                    if (checkStock("dogalyem", dogalYem) && checkStock("haziryem", hazirYem)) {
                        updateStock("dogalyem", dogalYem, false);
                        updateStock("haziryem", hazirYem, false);
                        JOptionPane.showMessageDialog(null, dogalYem + " kg doğal yem  ve " + hazirYem + " kg hazır yem başarıyla azaltıldı.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "Stok azaltma işlemi başarısız: Yeterli stok yok.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Geçersiz giriş", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        refreshTable();
    }
    private void updateStock(String yemType, int amount, boolean increase) throws SQLException {
        String query;
        if (increase) {
            query = "UPDATE stok SET " + yemType + " = " + yemType + " + ? WHERE id = 1";
        } else {
            query = "UPDATE stok SET " + yemType + " = " + yemType + " - ? WHERE id = 1 AND " + yemType + " >= ?";
        }

        try (Connection conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, amount); // Stok miktarını parametre olarak ekleyin
            if (!increase) {
                pstmt.setInt(2, amount); // Azaltma işlemi için ek bir parametre ekleyin
            }
            int affectedRows = pstmt.executeUpdate();
            if (!increase && affectedRows == 0) {
                throw new SQLException("Stok azaltma işlemi başarısız: Yeterli stok yok.");
            }
            // stok mesaj göster
            dogalYemField.setText("");
            hazirYemField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Stok güncelleme işlemi başarısız: " + ex.getMessage());
        } finally {
            refreshTable(); 
        }
    }

    private boolean checkStock(String yemType, int amount) throws SQLException {
        String query = "SELECT " + yemType + " FROM stok WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int currentStock = rs.getInt(yemType);
                return currentStock >= amount;
            } else {
                return false;
            }
        }
    }

    private void refreshTable() {
        String query = "SELECT * FROM stok WHERE id = 1";
        try (Connection conn = DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static TableModel buildTableModel(ResultSet rs) throws SQLException {
        java.sql.ResultSetMetaData metaData = rs.getMetaData();

        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
