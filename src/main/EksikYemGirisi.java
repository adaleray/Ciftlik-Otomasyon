package main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class EksikYemGirisi extends JFrame {
    /**
	 * 
	 */
	
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	EksikYemGirisi frame = new EksikYemGirisi();
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTextField txtKuepeNumarasi;
    private JTextField txtDogalYem;
    private JTextField txtHazirYem;

    public EksikYemGirisi() {
        setTitle("Eksik Yem Girişi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel lblKuepeNumarasi = new JLabel("Küpe Numarası:");
        contentPane.add(lblKuepeNumarasi);

        txtKuepeNumarasi = new JTextField();
        contentPane.add(txtKuepeNumarasi);
        txtKuepeNumarasi.setColumns(10);

        JLabel lblDogalYem = new JLabel("Doğal Yem Miktarı:");
        contentPane.add(lblDogalYem);

        txtDogalYem = new JTextField();
        contentPane.add(txtDogalYem);
        txtDogalYem.setColumns(10);

        JLabel lblHazirYem = new JLabel("Hazır Yem Miktarı:");
        contentPane.add(lblHazirYem);

        txtHazirYem = new JTextField();
        contentPane.add(txtHazirYem);
        txtHazirYem.setColumns(10);

        JButton btnKaydet = new JButton("Kaydet");
        btnKaydet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kaydet();
            }
        });
        contentPane.add(btnKaydet);

        JButton btnIptal = new JButton("İptal");
        btnIptal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPane.add(btnIptal);
    }

    private void kaydet() {
        String kuepeNumarasi = txtKuepeNumarasi.getText();
        double dogalYemMiktar = Double.parseDouble(txtDogalYem.getText());
        double hazirYemMiktar = Double.parseDouble(txtHazirYem.getText());
        LocalDate today = LocalDate.now();

        if (!hayvanVarMi(kuepeNumarasi)) {
            JOptionPane.showMessageDialog(this, "Bu küpe numarasına ait hayvan bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO public.eksik_yem (kuepe_numarasi, dogal_yem_miktar, hazir_yem_miktar, tarih) VALUES (?, ?, ?, ?)";
        try (Connection conn = MainMenu.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kuepeNumarasi);
            pstmt.setDouble(2, dogalYemMiktar);
            pstmt.setDouble(3, hazirYemMiktar);
            pstmt.setDate(4, java.sql.Date.valueOf(today));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kayıt başarıyla eklendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanına kayıt eklenirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean hayvanVarMi(String kuepeNumarasi) {
        String sql = "SELECT COUNT(*) FROM public.hayvan WHERE kuepe_numarasi = ?";
        try (Connection conn = MainMenu.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kuepeNumarasi);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
