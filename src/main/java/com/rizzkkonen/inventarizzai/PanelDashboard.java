package com.rizzkkonen.inventarizzai;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class PanelDashboard extends javax.swing.JPanel {

    private JLabel lblWelcome;
    private JLabel lblValAset;
    private JLabel lblValTiket;
    private JLabel lblValSSL;
    private JLabel lblValUser;

    public PanelDashboard() {
        initComponents();
        initComponentsCustom();
        loadStatistikData();
    }



    public void loadStatistikData() {
        try (Connection conn = Koneksi.getConnection()) {
            String sqlAset = "SELECT COUNT(*) FROM aset_it";
            try (PreparedStatement ps = conn.prepareStatement(sqlAset);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblValAset.setText(String.valueOf(rs.getInt(1)));
            }

            String sqlTiket = "SELECT COUNT(*) FROM tickets WHERE status = 'OPEN'";

            try (PreparedStatement ps = conn.prepareStatement(sqlTiket);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblValTiket.setText(String.valueOf(rs.getInt(1)));
            }

            String sqlSSL = "SELECT tgl_expired FROM ssl_certificates";
            int countWarningSSL = 0;

            LocalDate hariIni = LocalDate.now();

            try (PreparedStatement ps = conn.prepareStatement(sqlSSL);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate exp = LocalDate.parse(rs.getString("tgl_expired"));
                    if (exp.isBefore(hariIni.plusDays(90))) {
                        countWarningSSL++;
                    }
                }

            }
            lblValSSL.setText(String.valueOf(countWarningSSL));

            String sqlUser = "SELECT COUNT(*) FROM users";

            try (PreparedStatement ps = conn.prepareStatement(sqlUser);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblValUser.setText(String.valueOf(rs.getInt(1)));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load statistik: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private JPanel createStatCard(String title, JLabel valLabel, Color bg) {
        JPanel card = new JPanel(new java.awt.BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new java.awt.Dimension(220, 110));
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.WHITE);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLabel.setForeground(Color.WHITE);
        valLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(lblTitle, java.awt.BorderLayout.NORTH);
        card.add(valLabel, java.awt.BorderLayout.CENTER);

        return card;

    }

    private void initComponentsCustom() {
        lblWelcome = new JLabel("SYSTEM OVERVIEW & QUICK SUMMARY");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblValAset = new JLabel("0");
        lblValTiket = new JLabel("0");
        lblValSSL = new JLabel("0");
        lblValUser = new JLabel("0");

        JPanel cardAset = createStatCard("TOTAL ASET IT", lblValAset, new Color(52, 152, 219));
        JPanel cardTiket = createStatCard("TIKET PERLU RESPON (OPEN)", lblValTiket, new Color(231, 76, 60));
        JPanel cardSSL = createStatCard("<html>SSL EXPIRED /<br>WARNING (&lt;90 HARI)</html>", lblValSSL, new Color(241, 196, 15));
        JPanel cardUser = createStatCard("TOTAL USER TERDAFTAR", lblValUser, new Color(46, 204, 113));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblWelcome)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cardAset, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(cardTiket, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(cardSSL, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(cardUser, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblWelcome)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cardAset, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardTiket, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardSSL, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardUser, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(400, Short.MAX_VALUE))
        );
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents

    private void initComponents() {



        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(

            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

            .addGap(0, 400, Short.MAX_VALUE)

        );

        layout.setVerticalGroup(

            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

            .addGap(0, 300, Short.MAX_VALUE)

        );

    }// </editor-fold>//GEN-END:initComponents

}