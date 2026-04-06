package com.example.quanlychungcu.dto;

import java.time.LocalDate;

public class TienDienDTO {
    private String maCuDan;
    private  String maCanHo;
    private LocalDate ngayThu;
    private float tongSoDienSuDung;
    private float tongSoTienPhaiTra;
    private boolean trangThai;

    public TienDienDTO() {
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

    public float getTongSoDienSuDung() {
        return tongSoDienSuDung;
    }

    public void setTongSoDienSuDung(float tongSoDienSuDung) {
        this.tongSoDienSuDung = tongSoDienSuDung;
    }

    public float getTongSoTienPhaiTra() {
        return tongSoTienPhaiTra;
    }

    public void setTongSoTienPhaiTra(float tongSoTienPhaiTra) {
        this.tongSoTienPhaiTra = tongSoTienPhaiTra;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
