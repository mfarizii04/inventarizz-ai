/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template

 */

package com.rizzkkonen.inventarizzai;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Farizi
 */

public class FormDashboardAdmin extends javax.swing.JFrame {

    // Main Panels
    private JPanel navbarPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Sidebar Buttons
    private JButton btnMenuDashboard;
    private JButton btnMenuAset;
    private JButton btnMenuTiket;
    private JButton btnMenuSSL;
    private JButton btnMenuProfile;
    private JButton btnLogout;

    /**

     * Creates new form FormDashboardAdmin

     */

    public FormDashboardAdmin() {
        initComponents();
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Inventarizz - IT Asset & Ticketing System");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean isAdmin = "admin".equalsIgnoreCase(UserSession.getRole());

        //bagian navbar
        navbarPanel = new JPanel(new BorderLayout());
        navbarPanel.setBackground(new Color(52, 58, 64)); // Dark Gray AdminLTE
        navbarPanel.setPreferredSize(new Dimension(getWidth(), 50));
        navbarPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel lblBrand = new JLabel("INVENTARIZZ-AI  |  IT Asset & Ticketing System");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBrand.setForeground(Color.WHITE);

        String userLogged = UserSession.getUser() != null ? UserSession.getUser() : "User";
        String roleLogged = UserSession.getRole() != null ? UserSession.getRole().toUpperCase() : "USER";

        JLabel lblUserInfo = new JLabel("Login sebagai " + userLogged + " (Rolenya " + roleLogged + ")");
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUserInfo.setForeground(new Color(200, 205, 210));

        navbarPanel.add(lblBrand, BorderLayout.WEST);
        navbarPanel.add(lblUserInfo, BorderLayout.EAST);

        //sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(34, 45, 50)); // Darker Sidebar Gray
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        // Inisialisasi Button Sidebar
        btnMenuDashboard = createSidebarButton("Dashboard Overview");
        btnMenuAset = createSidebarButton("Manajemen Aset IT");
        btnMenuTiket = createSidebarButton("Manajemen Tiket");
        btnMenuSSL = createSidebarButton("SSL Aplikasi");
        btnMenuProfile = createSidebarButton("Profile & Auth");
        btnLogout = createSidebarButton("Logout");
        btnLogout.setBackground(new Color(180, 40, 40)); // Red Highlight Logout

        // Add ke Sidebar (Tombol SSL hanya dipasang jika Role = ADMIN)
        sidebarPanel.add(btnMenuDashboard);
        sidebarPanel.add(btnMenuAset);
        sidebarPanel.add(btnMenuTiket);

        if (isAdmin) {
            sidebarPanel.add(btnMenuSSL);
        }

        sidebarPanel.add(btnMenuProfile);
        sidebarPanel.add(Box.createVerticalStrut(20)); // Spacer
        sidebarPanel.add(btnLogout);

        //content card layout
        cardLayout = new CardLayout();

        contentPanel = new JPanel(cardLayout);

        contentPanel.setBackground(new Color(244, 246, 249)); // Light Gray Background



        //buat bagian halaman2 modul cardnya
        // Jika Admin load PanelDashboard, jika User biasa load PanelDashboardUser
        if (isAdmin) {
            contentPanel.add(new PanelDashboard(), "CARD_DASHBOARD");
        } else {
            contentPanel.add(new PanelDashboardUser(), "CARD_DASHBOARD");
        }

        contentPanel.add(new PanelAsetIT(), "CARD_ASET");
        contentPanel.add(new PanelTiket(), "CARD_TIKET");
        contentPanel.add(new PanelSSL(), "CARD_SSL");
        contentPanel.add(new PanelProfile(), "CARD_PROFILE");

        // Add ke Container Utama Frame
        add(navbarPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);



        //buat switch card pas diklik
        btnMenuDashboard.addActionListener(e -> cardLayout.show(contentPanel, "CARD_DASHBOARD"));
        btnMenuAset.addActionListener(e -> cardLayout.show(contentPanel, "CARD_ASET"));
        btnMenuTiket.addActionListener(e -> cardLayout.show(contentPanel, "CARD_TIKET"));
        btnMenuSSL.addActionListener(e -> cardLayout.show(contentPanel, "CARD_SSL"));
        btnMenuProfile.addActionListener(e -> cardLayout.show(contentPanel, "CARD_PROFILE"));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Clear data Session
                UserSession.clearSession();
                new FormLogin().setVisible(true);
                this.dispose();
            }
        });



        // Set default ke dashboard lgsng
        cardLayout.show(contentPanel, "CARD_DASHBOARD");

    }



    // Helper Styling Sidebar Button
    private JButton createSidebarButton(String text) {

        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Bikin Font Tebal

        // Matikan IsContentAreaFilled & BorderPainted bawaan Windows Look and Feel
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(new Color(44, 62, 80)); // Dark Blue/Gray (#2C3E50)
        btn.setForeground(Color.WHITE);           // Teks Putih Terang
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));



        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.equals(btnLogout)) {
                    btn.setBackground(new Color(52, 152, 219)); // Highlight Biru (#3498DB) Pas Hover
                    btn.setForeground(Color.WHITE);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.equals(btnLogout)) {
                    btn.setBackground(new Color(44, 62, 80));   // Kembali ke Warna Asli
                    btn.setForeground(Color.WHITE);
                }
            }
        });
        
        return btn;

    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        java.awt.EventQueue.invokeLater(() -> new FormDashboardAdmin().setVisible(true));

    }

    /**

     * This method is called from within the constructor to initialize the form.

     * WARNING: Do NOT modify this code. The content of this method is always

     * regenerated by the Form Editor.

     */

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(

            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

            .addGap(0, 400, Short.MAX_VALUE)

        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        
        pack();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables

    // End of variables declaration//GEN-END:variables

}