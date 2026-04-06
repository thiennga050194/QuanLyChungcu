package com.example.quanlychungcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HopDong")
public class HopDong {
    @Id
    @Column(name = "MaHopDong")
    private String maHopDong;

    @Column(name = "TenKH")
    private String tenKH;

    @Column(name = "MaCanHo")
    private String maCanHo;

    @Column(name = "MaCuDan")
    private String maCuDan;

    @Column(name = "DiaChiKhachHang")
    private String diaChiKhachHang;

    @Column(name = "NgayGiaoDich")
    private String ngayGiaoDich;

    public HopDong() {
    }

    public HopDong(String maHopDong, String tenKH, String maCanHo, String maCuDan, String diaChiKhachHang,
            String ngayGiaoDich) {
        this.maHopDong = maHopDong;
        this.tenKH = tenKH;
        this.maCanHo = maCanHo;
        this.maCuDan = maCuDan;
        this.diaChiKhachHang = diaChiKhachHang;
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public String getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(String maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getDiaChiKhachHang() {
        return diaChiKhachHang;
    }

    public void setDiaChiKhachHang(String diaChiKhachHang) {
        this.diaChiKhachHang = diaChiKhachHang;
    }

    public String getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public void setNgayGiaoDich(String ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }
}
