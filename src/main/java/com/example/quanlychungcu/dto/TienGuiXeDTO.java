package com.example.quanlychungcu.dto;

import java.time.LocalDate;

public class TienGuiXeDTO {
    private String maCuDan;
    private String maCanHo;
    private LocalDate ngayThu;
    private int xeOTo;
    private float tienGuiOTo;
    private int xeMay;
    private float tienGuiXeMay;
    private int xeDap;
    private float tienGuiXeDap;
    private float tongTienGuiXe;
    private boolean trangThai;

    public TienGuiXeDTO() {
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

    public int getXeOTo() {
        return xeOTo;
    }

    public void setXeOTo(int xeOTo) {
        this.xeOTo = xeOTo;
    }

    public float getTienGuiOTo() {
        return tienGuiOTo;
    }

    public void setTienGuiOTo(float tienGuiOTo) {
        this.tienGuiOTo = tienGuiOTo;
    }

    public int getXeMay() {
        return xeMay;
    }

    public void setXeMay(int xeMay) {
        this.xeMay = xeMay;
    }

    public float getTienGuiXeMay() {
        return tienGuiXeMay;
    }

    public void setTienGuiXeMay(float tienGuiXeMay) {
        this.tienGuiXeMay = tienGuiXeMay;
    }

    public int getXeDap() {
        return xeDap;
    }

    public void setXeDap(int xeDap) {
        this.xeDap = xeDap;
    }

    public float getTienGuiXeDap() {
        return tienGuiXeDap;
    }

    public void setTienGuiXeDap(float tienGuiXeDap) {
        this.tienGuiXeDap = tienGuiXeDap;
    }

    public float getTongTienGuiXe() {
        return tongTienGuiXe;
    }

    public void setTongTienGuiXe(float tongTienGuiXe) {
        this.tongTienGuiXe = tongTienGuiXe;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
