package com.example.quanlychungcu.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "PhanHoi")
public class PhanHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhanHoi")
    private int maPhanHoi;

    @Column(name = "TaiKhoan")
    private String taiKhoan;

    @Column(name = "PhanHoi")
    private String phanHoi;

    @Column(name = "Image")
    private String image;

    @Column(name = "ThoiGian")
    private Timestamp thoiGian;

    @Column(name = "BanQuanLy")
    private int banquanly;

    public PhanHoi() {
    }

    public PhanHoi(int maPhanHoi, String taiKhoan, String phanHoi, String image, Timestamp thoiGian, int banquanly) {
        this.maPhanHoi = maPhanHoi;
        this.taiKhoan = taiKhoan;
        this.phanHoi = phanHoi;
        this.image = image;
        this.thoiGian = thoiGian;
        this.banquanly = banquanly;
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
