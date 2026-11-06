package com.rizzkkonen.inventarizz.view;

import com.rizzkkonen.inventarizzai.UserSession;
import com.rizzkkonen.inventarizz.controller.AsetController;
import com.rizzkkonen.inventarizz.model.AsetIT;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelAsetIT extends JPanel {

    private JTable tblAset;
    private DefaultTableModel modelTabel;
    private JTextField txtKode, txtNama, txtLokasi, txtTgl, txtSearch;
    private JComboBox<String> cbKategori, cbStatus;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear;
    private AsetController controller;
    private boolean isAdmin;

    public PanelAsetIT() {
        controller = new AsetController();
        isAdmin = "admin".equalsIgnoreCase(UserSession.getRole());
        initComponentsCustom();
        loadData();
    }

    private void loadData() {
        modelTabel.setRowCount(0);
        List<AsetIT> list = controller.getAllAset(txtSearch.getText().trim(), 0, 50);
        for (AsetIT a : list) {
            modelTabel.addRow(new Object[]{a.getId(), a.getKodeAset(), a.getNamaAset(), a.getKategori(), a.getLokasi(), a.getStatus(), a.getTglPengadaan()});
        }
    }

    private void initComponentsCustom() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        txtKode = new JTextField(); txtNama = new JTextField(); txtLokasi = new JTextField(); txtTgl = new JTextField("2026-01-01");
        cbKategori = new JComboBox<>(new String[]{"Hardware", "Software", "Network", "Lainnya"});
        cbStatus = new JComboBox<>(new String[]{"Aktif", "Rusak", "Perbaikan", "Disimpan"});

        formPanel.add(new JLabel("Kode Aset:")); formPanel.add(txtKode);
        formPanel.add(new JLabel("Nama Aset:")); formPanel.add(txtNama);
        formPanel.add(new JLabel("Kategori:")); formPanel.add(cbKategori);
        formPanel.add(new JLabel("Lokasi:")); formPanel.add(txtLokasi);
        formPanel.add(new JLabel("Status:")); formPanel.add(cbStatus);
        formPanel.add(new JLabel("Tgl Pengadaan:")); formPanel.add(txtTgl);

        btnSimpan = new JButton("Simpan"); btnEdit = new JButton("Edit"); btnHapus = new JButton("Hapus"); btnClear = new JButton("Clear");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(btnSimpan); btnPanel.add(btnEdit); btnPanel.add(btnHapus); btnPanel.add(btnClear);

        if (!isAdmin) {
            btnSimpan.setVisible(false); btnEdit.setVisible(false); btnHapus.setVisible(false); btnClear.setVisible(false);
            txtKode.setEditable(false); txtNama.setEditable(false); txtLokasi.setEditable(false); txtTgl.setEditable(false);
            cbKategori.setEnabled(false); cbStatus.setEnabled(false);
        }

        JPanel topBox = new JPanel(new BorderLayout());
        topBox.add(formPanel, BorderLayout.CENTER);
        topBox.add(btnPanel, BorderLayout.SOUTH);

        modelTabel = new DefaultTableModel(new String[]{"ID", "Kode", "Nama", "Kategori", "Lokasi", "Status", "Tgl"}, 0);
        tblAset = new JTable(modelTabel);
        txtSearch = new JTextField(15);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search:")); searchPanel.add(txtSearch);

        txtSearch.addActionListener(e -> loadData());

        btnSimpan.addActionListener(e -> {
            AsetIT a = new AsetIT();
            a.setKodeAset(txtKode.getText().trim()); a.setNamaAset(txtNama.getText().trim());
            a.setKategori(cbKategori.getSelectedItem().toString()); a.setLokasi(txtLokasi.getText().trim());
            a.setStatus(cbStatus.getSelectedItem().toString()); a.setTglPengadaan(txtTgl.getText().trim());
            controller.saveAset(a); loadData();
        });

        add(topBox, BorderLayout.NORTH);
        add(new JScrollPane(tblAset), BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }
}
