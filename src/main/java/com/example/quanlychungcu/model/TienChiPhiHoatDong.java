package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TienChiPhiHoatDong")
@IdClass(FeeId.class)  // Tạo IdClass mới
public class TienChiPhiHoatDong {

    @Id
    @Column(name = "MaCuDan", length = 6, nullable = false)
    private String maCuDan;

    @Id
    @Column(name = "MaCanHo", length = 6, nullable = false)  // THÊM CỘT NÀY
    private String maCanHo;

    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "NgayThu", nullable = false)
    private LocalDate ngayThu;

    @Column(name = "SoNguoi")
    private Integer soNguoi;

    @Column(name = "TienChiPhiHoatDong")
    private Float tienChiPhiHoatDong;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    // Quan hệ với CUDAN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    // Quan hệ với CANHO (THÊM MỚI)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    public TienChiPhiHoatDong() {}

    public TienChiPhiHoatDong(String maCuDan, String maCanHo, LocalDate ngayThu,
                              Integer soNguoi, Float tienChiPhiHoatDong, Boolean trangThai) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.soNguoi = soNguoi;
        this.tienChiPhiHoatDong = tienChiPhiHoatDong;
        this.trangThai = trangThai;
    }

    // ===== Getter & Setter =====
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

    public Integer getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(Integer soNguoi) {
        this.soNguoi = soNguoi;
    }

    public Float getTienChiPhiHoatDong() {
        return tienChiPhiHoatDong;
    }

    public void setTienChiPhiHoatDong(Float tienChiPhiHoatDong) {
        this.tienChiPhiHoatDong = tienChiPhiHoatDong;
    }

    public Boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public CuDan getCuDan() {
        return cuDan;
    }

    public void setCuDan(CuDan cuDan) {
        this.cuDan = cuDan;
    }

    public CanHo getCanHo() {
        return canHo;
    }

    public void setCanHo(CanHo canHo) {
        this.canHo = canHo;
    }

    public String getTrangThaiText() {
        if (trangThai == null) return "Chưa xác định";
        return trangThai ? "Đã thanh toán" : "Chưa thanh toán";
    }
}