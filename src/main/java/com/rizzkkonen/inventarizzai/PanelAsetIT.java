package com.rizzkkonen.inventarizzai;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



public class PanelAsetIT extends javax.swing.JPanel {

    private DefaultTableModel modelTabel;
    private int currentPage = 1;
    private int limitPerPage = 10;
    private int totalData = 0;
    private int totalPages = 1;
    private boolean isAdmin;

    public PanelAsetIT() {
        initComponents();
        initComponentsCustom();
        this.isAdmin = "admin".equalsIgnoreCase(UserSession.getRole());
        adjustUIByRole();
        initTabel();
        loadDataTabel();
    }



    private void adjustUIByRole() {
        if (!isAdmin) {
            // klo user biasa, sembunyikan elemen Input Form dan Tombol Operasi Data
            lblKode.setVisible(false); txtKode.setVisible(false);
            lblNama.setVisible(false); txtNama.setVisible(false);
            lblKategori.setVisible(false); cbKategori.setVisible(false);
            lblLokasi.setVisible(false); txtLokasi.setVisible(false);
            lblStatus.setVisible(false); cbStatus.setVisible(false);
            lblTgl.setVisible(false); txtTgl.setVisible(false);

            btnSimpan.setVisible(false);
            btnEdit.setVisible(false);
            btnHapus.setVisible(false);
            btnClear.setVisible(false);
        }
    }



    private void initTabel() {
        modelTabel = new DefaultTableModel();
        modelTabel.addColumn("ID");
        modelTabel.addColumn("Kode Aset");
        modelTabel.addColumn("Nama Aset");
        modelTabel.addColumn("Kategori");
        modelTabel.addColumn("Lokasi");
        modelTabel.addColumn("Status");
        modelTabel.addColumn("Tgl Pengadaan");
        tblAset.setModel(modelTabel);
    }



    private void loadDataTabel() {
        modelTabel.setRowCount(0);
        String keyword = txtSearch.getText().trim();
        int offset = (currentPage - 1) * limitPerPage;

        String sqlCount = "SELECT COUNT(*) FROM aset_it WHERE kode_aset LIKE ? OR nama_aset LIKE ? OR kategori LIKE ? OR lokasi LIKE ?";
        String sqlData = "SELECT * FROM aset_it WHERE kode_aset LIKE ? OR nama_aset LIKE ? OR kategori LIKE ? OR lokasi LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = Koneksi.getConnection()) {
            try (PreparedStatement psCount = conn.prepareStatement(sqlCount)) {
                String searchPattern = "%" + keyword + "%";
                psCount.setString(1, searchPattern);
                psCount.setString(2, searchPattern);
                psCount.setString(3, searchPattern);
                psCount.setString(4, searchPattern);

                try (ResultSet rsCount = psCount.executeQuery()) {
                    if (rsCount.next()) {
                        totalData = rsCount.getInt(1);
                    }
                }
            }

            //buat pagination tabel
            totalPages = (int) Math.ceil((double) totalData / limitPerPage);
            if (totalPages == 0) totalPages = 1;
            if (currentPage > totalPages) currentPage = totalPages;

            try (PreparedStatement psData = conn.prepareStatement(sqlData)) {
                String searchPattern = "%" + keyword + "%";
                psData.setString(1, searchPattern);
                psData.setString(2, searchPattern);
                psData.setString(3, searchPattern);
                psData.setString(4, searchPattern);
                psData.setInt(5, limitPerPage);
                psData.setInt(6, offset);

                try (ResultSet rs = psData.executeQuery()) {
                    while (rs.next()) {
                        modelTabel.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("kode_aset"),
                            rs.getString("nama_aset"),
                            rs.getString("kategori"),
                            rs.getString("lokasi"),
                            rs.getString("status"),
                            rs.getString("tgl_pengadaan")
                        });
                    }
                }
            }

            updatePaginationUI(offset);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }



    private void updatePaginationUI(int offset) {
        int start = (totalData == 0) ? 0 : offset + 1;
        int end = Math.min(offset + limitPerPage, totalData);
        
        lblPaginationInfo.setText("Showing " + start + " to " + end + " of " + totalData + " entries (Page " + currentPage + "/" + totalPages + ")");

        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);

    }

    private void btnSimpanActionPerformed() {
        if (!validasiInput()) return;
        String sql = "INSERT INTO aset_it (kode_aset, nama_aset, kategori, lokasi, status, tgl_pengadaan) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, txtKode.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setString(3, cbKategori.getSelectedItem().toString());
                ps.setString(4, txtLokasi.getText().trim());
                ps.setString(5, cbStatus.getSelectedItem().toString());
                ps.setString(6, txtTgl.getText().trim());
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Aset IT Berhasil Disimpan!");

                loadDataTabel();
                resetForm();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Simpan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }



    private void btnEditActionPerformed() {
        int selectedRow = tblAset.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih aset di tabel yang mau diedit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasiInput()) return;

        int id = (int) modelTabel.getValueAt(selectedRow, 0);
        String sql = "UPDATE aset_it SET kode_aset = ?, nama_aset = ?, kategori = ?, lokasi = ?, status = ?, tgl_pengadaan = ? WHERE id = ?";

        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, txtKode.getText().trim());
                ps.setString(2, txtNama.getText().trim());
                ps.setString(3, cbKategori.getSelectedItem().toString());
                ps.setString(4, txtLokasi.getText().trim());
                ps.setString(5, cbStatus.getSelectedItem().toString());
                ps.setString(6, txtTgl.getText().trim());
                ps.setInt(7, id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Aset Berhasil Diupdate!");
                loadDataTabel();
                resetForm();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Update: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void btnHapusActionPerformed() {
        int selectedRow = tblAset.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih aset di tabel yang mau dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin mau hapus data aset ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            int id = (int) modelTabel.getValueAt(selectedRow, 0);
            String sql = "DELETE FROM aset_it WHERE id = ?";

            try (Connection conn = Koneksi.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);

                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Data Aset Berhasil Dihapus!");

                    loadDataTabel();

                    resetForm();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal Hapus: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

    private void tblAsetMouseClicked() {
        if (!isAdmin) return; // KLO user biasa, tidak perlu auto-fill form

        int row = tblAset.getSelectedRow();

        if (row != -1) {
            txtKode.setText(modelTabel.getValueAt(row, 1).toString());
            txtNama.setText(modelTabel.getValueAt(row, 2).toString());
            cbKategori.setSelectedItem(modelTabel.getValueAt(row, 3).toString());
            txtLokasi.setText(modelTabel.getValueAt(row, 4).toString());
            cbStatus.setSelectedItem(modelTabel.getValueAt(row, 5).toString());
            txtTgl.setText(modelTabel.getValueAt(row, 6).toString());

        }

    }

    private void resetForm() {
        txtKode.setText("");
        txtNama.setText("");
        cbKategori.setSelectedIndex(0);
        txtLokasi.setText("");
        cbStatus.setSelectedIndex(0);
        txtTgl.setText("2026-01-01");
        tblAset.clearSelection();
        txtKode.requestFocus();
    }

    private boolean validasiInput() {
        if (txtKode.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty() ||
            txtLokasi.getText().trim().isEmpty() || txtTgl.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua inputan wajib diisi!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;

    }

    private void initComponentsCustom() {
        lblKode = new javax.swing.JLabel("Kode Aset:");
        lblNama = new javax.swing.JLabel("Nama Aset:");
        lblKategori = new javax.swing.JLabel("Kategori:");
        lblLokasi = new javax.swing.JLabel("Lokasi:");
        lblStatus = new javax.swing.JLabel("Status:");
        lblTgl = new javax.swing.JLabel("Tgl (YYYY-MM-DD):");
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        cbKategori = new javax.swing.JComboBox<>(new String[] { "Server", "PC Desktop", "Laptop", "Monitor", "Keyboard", "Mouse", "Network", "Lainnya" });
        txtLokasi = new javax.swing.JTextField();
        cbStatus = new javax.swing.JComboBox<>(new String[] { "Aktif", "Rusak", "Perbaikan", "Disimpan" });
        txtTgl = new javax.swing.JTextField("2026-01-01");
        
        btnSimpan = new javax.swing.JButton("Simpan");
        btnEdit = new javax.swing.JButton("Edit");
        btnHapus = new javax.swing.JButton("Hapus");
        btnClear = new javax.swing.JButton("Clear");

        lblShow = new javax.swing.JLabel("Show:");

        cbEntries = new javax.swing.JComboBox<>(new String[] { "10", "20", "50" });

        lblSearch = new javax.swing.JLabel("Search:");

        txtSearch = new javax.swing.JTextField();

        jScrollPane1 = new javax.swing.JScrollPane();

        tblAset = new javax.swing.JTable();

        lblPaginationInfo = new javax.swing.JLabel("Showing 0 to 0 of 0 entries");

        btnPrev = new javax.swing.JButton("Previous");

        btnNext = new javax.swing.JButton("Next");

        txtSearch.addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {

                currentPage = 1;

                loadDataTabel();

            }

        });
        
        cbEntries.addActionListener(e -> {

            limitPerPage = Integer.parseInt(cbEntries.getSelectedItem().toString());

            currentPage = 1;

            loadDataTabel();

        });

        btnPrev.addActionListener(e -> {

            if (currentPage > 1) {

                currentPage--;

                loadDataTabel();

            }

        });

        btnNext.addActionListener(e -> {

            if (currentPage < totalPages) {

                currentPage++;

                loadDataTabel();

            }

        });

        btnSimpan.addActionListener(e -> btnSimpanActionPerformed());
        btnEdit.addActionListener(e -> btnEditActionPerformed());
        btnHapus.addActionListener(e -> btnHapusActionPerformed());
        btnClear.addActionListener(e -> resetForm());

        tblAset.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                tblAsetMouseClicked();

            }

        });

        jScrollPane1.setViewportView(tblAset);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);

        this.setLayout(layout);

        layout.setHorizontalGroup(

            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

            .addGroup(layout.createSequentialGroup()

                .addGap(20, 20, 20)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)

                    .addGroup(layout.createSequentialGroup()

                        .addComponent(lblShow)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(cbEntries, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                        .addComponent(lblSearch)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))

                    .addGroup(layout.createSequentialGroup()

                        .addComponent(lblPaginationInfo)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                        .addComponent(btnPrev)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(btnNext))

                    .addGroup(layout.createSequentialGroup()

                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                            .addComponent(lblKode)

                            .addComponent(lblNama)

                            .addComponent(lblKategori))

                        .addGap(20, 20, 20)

                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)

                            .addComponent(txtKode)

                            .addComponent(txtNama)

                            .addComponent(cbKategori, 0, 180, Short.MAX_VALUE))

                        .addGap(40, 40, 40)

                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                            .addComponent(lblLokasi)

                            .addComponent(lblStatus)

                            .addComponent(lblTgl))

                        .addGap(20, 20, 20)

                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)

                            .addComponent(txtLokasi)

                            .addComponent(cbStatus, 0, 180, Short.MAX_VALUE)

                            .addComponent(txtTgl)))

                    .addGroup(layout.createSequentialGroup()

                        .addComponent(btnSimpan)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(btnEdit)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(btnHapus)

                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                        .addComponent(btnClear)))

                .addGap(20, 20, 20))

        );

        layout.setVerticalGroup(

            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

            .addGroup(layout.createSequentialGroup()

                .addGap(20, 20, 20)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(lblKode)

                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(lblLokasi)

                    .addComponent(txtLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(lblNama)

                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(lblStatus)

                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(lblKategori)

                    .addComponent(cbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(lblTgl)

                    .addComponent(txtTgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addGap(18, 18, 18)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(btnSimpan)

                    .addComponent(btnEdit)

                    .addComponent(btnHapus)

                    .addComponent(btnClear))

                .addGap(20, 20, 20)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(lblShow)

                    .addComponent(cbEntries, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                    .addComponent(lblSearch)

                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))

                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)

                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)

                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)

                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)

                    .addComponent(lblPaginationInfo)

                    .addComponent(btnPrev)

                    .addComponent(btnNext))

                .addContainerGap(20, Short.MAX_VALUE))

        );

    }
    
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbEntries;
    private javax.swing.JComboBox<String> cbKategori;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKategori;
    private javax.swing.JLabel lblKode;
    private javax.swing.JLabel lblLokasi;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblPaginationInfo;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblShow;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTgl;
    private javax.swing.JTable tblAset;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtLokasi;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTgl;
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