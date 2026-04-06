package com.example.quanlychungcu.dto;

import java.time.LocalDate;

public class TienNuocDTO {
    private String maCuDan;
    private String maCanHo;
    private LocalDate ngayThu;
    private Float soKhoiNuocSuDung;
    private float soTienNuocPhaiTra;
    private boolean trangThai;

    public TienNuocDTO() {
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getMaCanHo(){
        return maCanHo;

    }
    public void setMaCanHo(String maCanHo){
             this.maCanHo=maCanHo;
    }


    public LocalDate getNgayThu() {
        return ngayThu;
    }

    public void setNgayThu(LocalDate ngayThu) {
        this.ngayThu = ngayThu;
    }

    public Float getSoKhoiNuocSuDung() {
        return soKhoiNuocSuDung;
    }

    public void setSoKhoiNuocSuDung(Float soKhoiNuocSuDung) {
        this.soKhoiNuocSuDung = soKhoiNuocSuDung;
    }

    public float getSoTienNuocPhaiTra() {
        return soTienNuocPhaiTra;
    }

    public void setSoTienNuocPhaiTra(float soTienNuocPhaiTra) {
        this.soTienNuocPhaiTra = soTienNuocPhaiTra;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
