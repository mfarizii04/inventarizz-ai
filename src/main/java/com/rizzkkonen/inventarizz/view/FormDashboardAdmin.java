package com.rizzkkonen.inventarizz.view;

import com.rizzkkonen.inventarizzai.UserSession;
import java.awt.*;
import javax.swing.*;

public class FormDashboardAdmin extends javax.swing.JFrame {

    private JPanel navbarPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JButton btnMenuDashboard;
    private JButton btnMenuAset;
    private JButton btnMenuTiket;
    private JButton btnMenuSSL;
    private JButton btnMenuProfile;
    private JButton btnLogout;

    public FormDashboardAdmin() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Inventarizz - IT Asset & Ticketing System");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean isAdmin = "admin".equalsIgnoreCase(UserSession.getRole());

        // Header / Navbar
        navbarPanel = new JPanel(new BorderLayout());
        navbarPanel.setBackground(new Color(52, 58, 64));
        navbarPanel.setPreferredSize(new Dimension(getWidth(), 50));
        navbarPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel lblBrand = new JLabel("INVENTARIZZ-AI  |  IT Asset & Ticketing System");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBrand.setForeground(Color.WHITE);

        String userLogged = UserSession.getUser() != null ? UserSession.getUser() : "User";
        String roleLogged = UserSession.getRole() != null ? UserSession.getRole().toUpperCase() : "USER";

        JLabel lblUserInfo = new JLabel("Login sebagai " + userLogged + " (" + roleLogged + ")");
        lblUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUserInfo.setForeground(new Color(241, 196, 15));

        navbarPanel.add(lblBrand, BorderLayout.WEST);
        navbarPanel.add(lblUserInfo, BorderLayout.EAST);

        // Sidebar
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(34, 45, 50));
        sidebarPanel.setPreferredSize(new Dimension(220, getHeight()));
        sidebarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        btnMenuDashboard = createSidebarButton("Dashboard Overview");
        btnMenuAset = createSidebarButton("Manajemen Aset IT");
        btnMenuTiket = createSidebarButton("Manajemen Tiket");
        btnMenuSSL = createSidebarButton("SSL Aplikasi");
        btnMenuProfile = createSidebarButton("Profile & Auth");
        btnLogout = createSidebarButton("Logout");
        btnLogout.setBackground(new Color(180, 40, 40));

        sidebarPanel.add(btnMenuDashboard);
        sidebarPanel.add(btnMenuAset);
        sidebarPanel.add(btnMenuTiket);
        if (isAdmin) {
            sidebarPanel.add(btnMenuSSL);
        }
        sidebarPanel.add(btnMenuProfile);
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(btnLogout);

        // Content Area (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(244, 246, 249));

        if (isAdmin) {
            contentPanel.add(new PanelDashboard(), "CARD_DASHBOARD");
        } else {
            contentPanel.add(new PanelDashboardUser(), "CARD_DASHBOARD");
        }

        contentPanel.add(new PanelAsetIT(), "CARD_ASET");
        contentPanel.add(new PanelTiket(), "CARD_TIKET");
        contentPanel.add(new PanelSSL(), "CARD_SSL");
        contentPanel.add(new PanelProfile(), "CARD_PROFILE");

        add(navbarPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        btnMenuDashboard.addActionListener(e -> cardLayout.show(contentPanel, "CARD_DASHBOARD"));
        btnMenuAset.addActionListener(e -> cardLayout.show(contentPanel, "CARD_ASET"));
        btnMenuTiket.addActionListener(e -> cardLayout.show(contentPanel, "CARD_TIKET"));
        btnMenuSSL.addActionListener(e -> cardLayout.show(contentPanel, "CARD_SSL"));
        btnMenuProfile.addActionListener(e -> cardLayout.show(contentPanel, "CARD_PROFILE"));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin mau keluar?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                UserSession.clearSession();
                new FormLogin().setVisible(true);
                this.dispose();
            }
        });

        cardLayout.show(contentPanel, "CARD_DASHBOARD");
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBackground(new Color(44, 62, 80));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.equals(btnLogout)) {
                    btn.setBackground(new Color(52, 152, 219));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.equals(btnLogout)) {
                    btn.setBackground(new Color(44, 62, 80));
                }
            }
        });

        return btn;
    }
}
