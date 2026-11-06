/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.rizzkkonen.inventarizzai;

import java.sql.*;

/**
 *
 * @author Farizi
*/

/*
public class Koneksi {
 
    // Konfigurasi Database Laragon Default
    private static final String URL = "jdbc:mysql://localhost:3306/inventarizz-java";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default Laragon tanpa password

    public static void main(String[] args) {
        System.out.println("Mencoba koneksi ke database MySQL...");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("KONEKSI BERHASIL! Terhubung ke database inventarizz-java.");
            }
        } catch (SQLException e) {
            System.err.println("KONEKSI GAGAL!");
            System.err.println("Pesan Error: " + e.getMessage());
        }
    }
    
}
*/

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/inventarizz-java";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
