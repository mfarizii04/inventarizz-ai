package com.rizzkkonen.inventarizz.view;

import com.rizzkkonen.inventarizz.config.HibernateUtil;
import org.hibernate.Session;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PanelDashboard extends JPanel {

    private JLabel lblValAset, lblValTiket, lblValSSL, lblValUser;

    public PanelDashboard() {
        initComponentsCustom();
        loadStatistikData();
    }

    private void loadStatistikData() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long countAset = session.createQuery("SELECT COUNT(a) FROM AsetIT a", Long.class).uniqueResult();
            lblValAset.setText(String.valueOf(countAset != null ? countAset : 0));

            Long countTiket = session.createQuery("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'OPEN'", Long.class).uniqueResult();
            lblValTiket.setText(String.valueOf(countTiket != null ? countTiket : 0));

            Long countSSL = session.createQuery("SELECT COUNT(s) FROM SSLCertificate s", Long.class).uniqueResult();
            lblValSSL.setText(String.valueOf(countSSL != null ? countSSL : 0));

            Long countUser = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();
            lblValUser.setText(String.valueOf(countUser != null ? countUser : 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createStatCard(String title, JLabel valLabel, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setPreferredSize(new Dimension(220, 110));
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(Color.WHITE);

        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valLabel.setForeground(Color.WHITE);
        valLabel.setHorizontalAlignment(SwingConstants.LEFT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valLabel, BorderLayout.CENTER);
        return card;
    }

    private void initComponentsCustom() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        setBackground(new Color(244, 246, 249));

        lblValAset = new JLabel("0");
        lblValTiket = new JLabel("0");
        lblValSSL = new JLabel("0");
        lblValUser = new JLabel("0");

        add(createStatCard("TOTAL ASET IT", lblValAset, new Color(52, 152, 219)));
        add(createStatCard("TIKET OPEN", lblValTiket, new Color(231, 76, 60)));
        add(createStatCard("<html>SSL EXPIRED /<br>WARNING (&lt;90 HARI)</html>", lblValSSL, new Color(241, 196, 15)));
        add(createStatCard("TOTAL USER TERDAFTAR", lblValUser, new Color(46, 204, 113)));
    }
}
