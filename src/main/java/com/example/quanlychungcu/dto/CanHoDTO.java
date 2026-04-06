package com.example.quanlychungcu.dto;

public class CanHoDTO {
    private String maCanHo;
    private float dienTich;
    private int soPhong;
    private boolean trangThai;
    private String maKhu;
    private String tenKhu;
    private String maCuDan; // Thêm lại để hiển thị chủ hộ/người đại diện
    private String tenCuDan; // Thêm để hiển thị tên chủ hộ

    public CanHoDTO() {
    }

    // Constructor with wrapper types to be safer for JPQL mapping
    public CanHoDTO(String maCanHo, Float dienTich, Integer soPhong, Boolean trangThai, String maKhu,
            String tenKhu, String maCuDan) {
        this.maCanHo = maCanHo;
        this.dienTich = (dienTich != null) ? dienTich : 0.0f;
        this.soPhong = (soPhong != null) ? soPhong : 0;
        this.trangThai = (trangThai != null) ? trangThai : false;
        this.maKhu = maKhu;
        this.tenKhu = tenKhu;
        this.maCuDan = maCuDan;
    }

    // Overloaded constructor for Double
    public CanHoDTO(String maCanHo, Double dienTich, Integer soPhong, Boolean trangThai, String maKhu,
            String tenKhu, String maCuDan) {
        this(maCanHo, (dienTich != null ? dienTich.floatValue() : 0.0f), soPhong, trangThai, maKhu, tenKhu, maCuDan);
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public float getDienTich() {
        return dienTich;
    }

    public void setDienTich(float dienTich) {
        this.dienTich = dienTich;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
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

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getTenCuDan() {
        return tenCuDan;
    }

    public void setTenCuDan(String tenCuDan) {
        this.tenCuDan = tenCuDan;
    }
}
