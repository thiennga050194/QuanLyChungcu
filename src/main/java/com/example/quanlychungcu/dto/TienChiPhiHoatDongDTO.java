package com.example.quanlychungcu.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TienChiPhiHoatDongDTO {
    private String maCuDan;
    private String maCanHo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayThu;
    private int soNguoi;
    private float tienChiPhiHoatDong;
    private boolean trangThai;

    public TienChiPhiHoatDongDTO() {
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getMaCanHo() {return  maCanHo;};

    public void setMaCanHo(String maCanHo) {this.maCanHo = maCanHo;};

    public LocalDate getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(LocalDate ngayThu) {
        this.ngayThu = ngayThu;
    }

    public int getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(int soNguoi) {
        this.soNguoi = soNguoi;
    }

    public float getTienChiPhiHoatDong() {
        return tienChiPhiHoatDong;
    }

    public void setTienChiPhiHoatDong(float tienChiPhiHoatDong) {
        this.tienChiPhiHoatDong = tienChiPhiHoatDong;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
