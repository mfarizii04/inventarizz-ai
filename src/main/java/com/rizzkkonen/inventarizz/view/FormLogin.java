package com.rizzkkonen.inventarizz.view;

import com.rizzkkonen.inventarizzai.UserSession;
import com.rizzkkonen.inventarizz.controller.UserController;
import com.rizzkkonen.inventarizz.model.User;
import javax.swing.*;
import java.awt.*;

public class FormLogin extends javax.swing.JFrame {

    private JTextField txtUser;
    private JPasswordField txtPwd;
    private JButton btnLogin;
    private UserController userController;

    public FormLogin() {
        userController = new UserController();
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setTitle("Inventarizz - Login System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(244, 246, 249));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("INVENTARIZZ LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JLabel lblUser = new JLabel("Username:");
        txtUser = new JTextField(15);

        JLabel lblPwd = new JLabel("Password:");
        txtPwd = new JPasswordField(15);

        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblUser, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblPwd, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(txtPwd, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        add(panel);

        btnLogin.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPwd.getPassword()).trim();

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userController.login(u, p);
        if (user != null) {
            UserSession.setSession(user.getUserId(), user.getUser(), user.getRole());
            JOptionPane.showMessageDialog(this, "Login Berhasil! Selamat Datang " + user.getUser());
            new FormDashboardAdmin().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
