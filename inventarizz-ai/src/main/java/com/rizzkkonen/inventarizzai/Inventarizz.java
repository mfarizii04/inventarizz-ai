package com.rizzkkonen.inventarizzai;

import javax.swing.UIManager;

public class Inventarizz {

    public static void main(String[] args) {
        // Set Look & Feel Native OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            // Check Router Session
            if (UserSession.isLoggedIn()) {
                new FormDashboardAdmin().setVisible(true);
            } else {
                new FormLogin().setVisible(true);
            }
        });
    }
}