package com.rizzkkonen.inventarizz.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aset_it")
public class AsetIT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "kode_aset", nullable = false)
    private String kodeAset;

    @Column(name = "nama_aset", nullable = false)
    private String namaAset;

    @Column(name = "kategori", nullable = false)
    private String kategori;

    @Column(name = "lokasi")
    private String lokasi;

    @Column(name = "status")
    private String status;

    @Column(name = "tgl_pengadaan")
    private String tglPengadaan;

    public AsetIT() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getKodeAset() { return kodeAset; }
    public void setKodeAset(String kodeAset) { this.kodeAset = kodeAset; }

    public String getNamaAset() { return namaAset; }
    public void setNamaAset(String namaAset) { this.namaAset = namaAset; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTglPengadaan() { return tglPengadaan; }
    public void setTglPengadaan(String tglPengadaan) { this.tglPengadaan = tglPengadaan; }
}
