package com.rizzkkonen.inventarizz;

import com.rizzkkonen.inventarizz.view.FormLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new FormLogin().setVisible(true);
        });
    }
}
