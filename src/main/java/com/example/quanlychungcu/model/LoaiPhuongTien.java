package com.example.quanlychungcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LoaiPhuongTien")
public class LoaiPhuongTien {
    @Column(name = "MaCuDan")
    private String maCuDan;

    @Column(name = "LoaiXe")
    private String loaiXe;

    @Id
    @Column(name = "MaDangKy")
    private String maDangKy;

    public LoaiPhuongTien() {
    }

    public LoaiPhuongTien(String maCuDan, String loaiXe, String maDangKy) {
        this.maCuDan = maCuDan;
        this.loaiXe = loaiXe;
        this.maDangKy = maDangKy;
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getMaDangKy() {
        return maDangKy;
    }

    public void setMaDangKy(String maDangKy) {
        this.maDangKy = maDangKy;
    }

    @Override
    public String toString() {
        return "LoaiPhuongTien{" + "maCuDan=" + maCuDan + ", loaiXe=" + loaiXe + ", maDangKy=" + maDangKy + '}';
    }
}
