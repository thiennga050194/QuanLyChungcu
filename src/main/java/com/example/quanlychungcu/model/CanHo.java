package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "CANHO")
public class CanHo {

    @Id
    @Column(name = "MaCanHo", length = 6)
    private String maCanHo;

    @Column(name = "DienTich")
    private float dienTich;

    @Column(name = "SoPhong")
    private int soPhong;

    @Column(name = "TrangThai")
    private boolean trangThai;

    @Column(name = "MaKhu", length = 2, nullable = false)
    private String maKhu;

    // Quan hệ ManyToOne với KhuCanHo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhu", referencedColumnName = "MaKhu", insertable = false, updatable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private KhuCanHo khuCanHo;

    // Quan hệ OneToMany với bảng trung gian
    @OneToMany(mappedBy = "canHo", fetch = FetchType.LAZY)
    private List<CuDanCanHo> danhSachQuanHe;

    public CanHo() {
    }

    public CanHo(String maCanHo, float dienTich, int soPhong, boolean trangThai, String maKhu) {
        this.maCanHo = maCanHo;
        this.dienTich = dienTich;
        this.soPhong = soPhong;
        this.trangThai = trangThai;
        this.maKhu = maKhu;
    }

    // Getters và Setters
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

    public KhuCanHo getKhuCanHo() {
        return khuCanHo;
    }

    public void setKhuCanHo(KhuCanHo khuCanHo) {
        this.khuCanHo = khuCanHo;
    }

    public List<CuDanCanHo> getDanhSachQuanHe() {
        return danhSachQuanHe;
    }

    public void setDanhSachQuanHe(List<CuDanCanHo> danhSachQuanHe) {
        this.danhSachQuanHe = danhSachQuanHe;
    }
}