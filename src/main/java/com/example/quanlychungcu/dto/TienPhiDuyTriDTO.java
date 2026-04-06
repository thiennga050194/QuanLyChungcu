package com.example.quanlychungcu.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TienPhiDuyTriDTO {
    private String maCuDan;
    private String maCanHo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayThu;

    private int soNguoi;
    private float tienDuyTri;
    private boolean trangThai;

    public TienPhiDuyTriDTO() {
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

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

    public float getTienDuyTri() {
        return tienDuyTri;
    }

    public void setTienDuyTri(float tienDuyTri) {
        this.tienDuyTri = tienDuyTri;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
