package com.example.quanlychungcu.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CuDanDTO {
    private String maCuDan;
    private String tenCuDan;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String soDT;
    private String soCMT;
    private String queQuan;

    // Quan hệ với căn hộ
    private List<CuDanCanHoDTO> danhSachQuanHe = new ArrayList<>();

    // Các trường hỗ trợ cho frontend cũ hoặc khi thêm nhanh
    private String maCanHo;
    private String maKhu;
    private int vaiTro; // 0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành viên thuê

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayKetThuc;

    public CuDanDTO() {
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

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getSoCMT() {
        return soCMT;
    }

    public void setSoCMT(String soCMT) {
        this.soCMT = soCMT;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public List<CuDanCanHoDTO> getDanhSachQuanHe() {
        return danhSachQuanHe;
    }

    public void setDanhSachQuanHe(List<CuDanCanHoDTO> danhSachQuanHe) {
        this.danhSachQuanHe = danhSachQuanHe;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public String getMaKhu() {
        return maKhu;
    }

    public void setMaKhu(String maKhu) {
        this.maKhu = maKhu;
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
}