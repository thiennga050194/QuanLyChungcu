package com.example.quanlychungcu.dto;

public class KhuCanHoDTO {
    private String maKhu;
    private String tenKhu;
    private int soTang;
    private int soCanTT;
    private String diaChi;

    public KhuCanHoDTO() {
    }

    public String getMaKhu() {
        return maKhu;
    }

    public void setMaKhu(String maKhu) {
        this.maKhu = maKhu;
    }

    public String getTenKhu() {
        return tenKhu;
    }

    public void setTenKhu(String tenKhu) {
        this.tenKhu = tenKhu;
    }

    public int getSoTang() {
        return soTang;
    }

    public void setSoTang(int soTang) {
        this.soTang = soTang;
    }

    public int getSoCanTT() {
        return soCanTT;
    }

    public void setSoCanTT(int soCanTT) {
        this.soCanTT = soCanTT;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
