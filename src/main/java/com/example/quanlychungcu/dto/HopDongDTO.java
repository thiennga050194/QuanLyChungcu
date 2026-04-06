package com.example.quanlychungcu.dto;

public class HopDongDTO {
    private String maHopDong;
    private String tenKH;
    private String maCanHo;
    private String maCuDan;
    private String diaChiKhachHang;
    private String ngayGiaoDich;

    public HopDongDTO() {
    }

    // SỬA: Thêm maCuDan vào constructor
    public HopDongDTO(String maHopDong, String tenKH, String maCanHo, String maCuDan,
                      String diaChiKhachHang, String ngayGiaoDich) {
        this.maHopDong = maHopDong;
        this.tenKH = tenKH;
        this.maCanHo = maCanHo;
        this.maCuDan = maCuDan;  // Đã thêm dòng này
        this.diaChiKhachHang = diaChiKhachHang;
        this.ngayGiaoDich = ngayGiaoDich;
    }

    // Các getter/setter giữ nguyên
    public String getMaHopDong() {
        return maHopDong;
    }

    public void setMaHopDong(String maHopDong) {
        this.maHopDong = maHopDong;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getMaCanHo() {
        return maCanHo;
    }

    public void setMaCanHo(String maCanHo) {
        this.maCanHo = maCanHo;
    }

    public String getMaCuDan() {
        return maCuDan;
    }

    public void setMaCuDan(String maCuDan) {
        this.maCuDan = maCuDan;
    }

    public String getDiaChiKhachHang() {
        return diaChiKhachHang;
    }

    public void setDiaChiKhachHang(String diaChiKhachHang) {
        this.diaChiKhachHang = diaChiKhachHang;
    }

    public String getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public void setNgayGiaoDich(String ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }
}