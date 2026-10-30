/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.inventarizz.inventarizz;

import com.inventarizz.inventarizz.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author user
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Inventarizz {

    public static void main(String[] args) {
        System.out.println("=== Memulai Pengujian Koneksi MySQL ===");

        try (Connection conn = Koneksi.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("BERHASIL: Terhubung ke database MySQL (db_inventarizz)!");
            }
        } catch (SQLException e) {
            System.err.println("GAGAL: Terjadi kesalahan saat menghubungkan ke database.");
            e.printStackTrace();
        }
    }
}
