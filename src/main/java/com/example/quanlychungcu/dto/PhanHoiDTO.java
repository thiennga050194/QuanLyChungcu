package com.example.quanlychungcu.dto;

import java.sql.Timestamp;

public class PhanHoiDTO {
    private int maPhanHoi;
    private String taiKhoan;
    private String phanHoi;
    private String image;
    private Timestamp thoiGian;
    private int banquanly;

    public PhanHoiDTO() {
    }

    public int getMaPhanHoi() {
        return maPhanHoi;
    }

    public void setMaPhanHoi(int maPhanHoi) {
        this.maPhanHoi = maPhanHoi;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getPhanHoi() {
        return phanHoi;
    }

    public void setPhanHoi(String phanHoi) {
        this.phanHoi = phanHoi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }

    public int getBanquanly() {
        return banquanly;
    }

    public void setBanquanly(int banquanly) {
        this.banquanly = banquanly;
    }
}
