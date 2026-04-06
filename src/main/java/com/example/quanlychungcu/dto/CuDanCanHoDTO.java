package com.example.quanlychungcu.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CuDanCanHoDTO {
    private String maCuDan;
    private String tenCuDan;
    private String maCanHo;
    // 0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành viên thuê

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayKetThuc;

    // Thông tin thêm để hiển thị
    private String tenKhu;
    private float dienTich;
    private int vaiTro; // 0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành viên thuê

    public CuDanCanHoDTO() {
        this.ngayBatDau = LocalDate.now();
    }

    public CuDanCanHoDTO(String maCuDan, String maCanHo, int vaiTro) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.vaiTro = vaiTro;
        this.ngayBatDau = LocalDate.now();
    }

    // ===== Getters và Setters =====
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

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getTenCuDan() {
        return tenCuDan;
    }

    public void setTenCuDan(String tenCuDan) {
        this.tenCuDan = tenCuDan;
    }

    public String getTenKhu() {
        return tenKhu;
    }

    public void setTenKhu(String tenKhu) {
        this.tenKhu = tenKhu;
    }

    public float getDienTich() {
        return dienTich;
    }

    public void setDienTich(float dienTich) {
        this.dienTich = dienTich;
    }

    // ===== Helper methods =====
    public String getVaiTroText() {
        switch (vaiTro) {
            case 0:
                return "Chủ hộ";
            case 1:
                return "Thành viên";
            case 2:
                return "Chủ hộ thuê";
            case 3:
                return "Thành viên thuê";
            default:
                return "Không xác định";
        }
    }

    public boolean isDangOHienTai() {
        return ngayKetThuc == null;
    }
}