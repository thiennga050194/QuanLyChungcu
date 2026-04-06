package com.example.quanlychungcu.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CUDAN")
public class CuDan {

    @Id
    @Column(name = "MaCuDan", length = 6)
    private String maCuDan;

    @Column(name = "TenCuDan", nullable = false)
    private String tenCuDan;

    @Column(name = "NgaySinh", nullable = false)
    private LocalDate ngaySinh;

    @Column(name = "GioiTinh")
    private boolean gioiTinh;

    @Column(name = "SoDT")
    private String soDT;

    @Column(name = "SoCMT")
    private String soCMT;

    @Column(name = "QueQuan", nullable = false)
    private String queQuan;

    // ĐÃ XÓA maCanHo và maKhu

    // Quan hệ OneToMany với bảng trung gian
    @OneToMany(mappedBy = "cuDan", fetch = FetchType.LAZY)
    private List<CuDanCanHo> danhSachQuanHe;

    public CuDan() {}

    public CuDan(String maCuDan, String tenCuDan, LocalDate ngaySinh, boolean gioiTinh,
                 String soDT, String soCMT, String queQuan) {
        this.maCuDan = maCuDan;
        this.tenCuDan = tenCuDan;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDT = soDT;
        this.soCMT = soCMT;
        this.queQuan = queQuan;
    }

    // Getters và Setters
    public String getMaCuDan() { return maCuDan; }
    public void setMaCuDan(String maCuDan) { this.maCuDan = maCuDan; }

    public String getTenCuDan() { return tenCuDan; }
    public void setTenCuDan(String tenCuDan) { this.tenCuDan = tenCuDan; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public boolean isGioiTinh() { return gioiTinh; }
    public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getSoDT() { return soDT; }
    public void setSoDT(String soDT) { this.soDT = soDT; }

    public String getSoCMT() { return soCMT; }
    public void setSoCMT(String soCMT) { this.soCMT = soCMT; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public List<CuDanCanHo> getDanhSachQuanHe() { return danhSachQuanHe; }
    public void setDanhSachQuanHe(List<CuDanCanHo> danhSachQuanHe) { this.danhSachQuanHe = danhSachQuanHe; }
}