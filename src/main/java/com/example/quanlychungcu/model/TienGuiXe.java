package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TienGuiXe")
@IdClass(FeeId.class)
public class TienGuiXe {

    @Id
    @Column(name = "MaCuDan", length = 6, nullable = false)
    private String maCuDan;

    @Id
    @Column(name = "MaCanHo", length = 6, nullable = false)
    private String maCanHo;

    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "NgayThu", nullable = false)
    private LocalDate ngayThu;

    @Column(name = "XeOTo")
    private Integer xeOTo;

    @Column(name = "TienGuiOTo")
    private Float tienGuiOTo;

    @Column(name = "XeMay")
    private Integer xeMay;

    @Column(name = "TienGuiXeMay")
    private Float tienGuiXeMay;

    @Column(name = "XeDap")
    private Integer xeDap;

    @Column(name = "TienGuiXeDap")
    private Float tienGuiXeDap;

    @Column(name = "TongTienGuiXe")
    private Float tongTienGuiXe;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    // Constructors
    public TienGuiXe() {
    }

    public TienGuiXe(String maCuDan, String maCanHo, LocalDate ngayThu) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.trangThai = false;
    }

    // ===== Getters & Setters =====
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

    public Integer getXeOTo() {
        return xeOTo;
    }

    public void setXeOTo(Integer xeOTo) {
        this.xeOTo = xeOTo;

    }

    public Float getTienGuiOTo() {
        return tienGuiOTo;
    }

    public void setTienGuiOTo(Float tienGuiOTo) {
        this.tienGuiOTo = tienGuiOTo;

    }

    public Integer getXeMay() {
        return xeMay;
    }

    public void setXeMay(Integer xeMay) {
        this.xeMay = xeMay;

    }

    public Float getTienGuiXeMay() {
        return tienGuiXeMay;
    }

    public void setTienGuiXeMay(Float tienGuiXeMay) {
        this.tienGuiXeMay = tienGuiXeMay;

    }

    public Integer getXeDap() {
        return xeDap;
    }

    public void setXeDap(Integer xeDap) {
        this.xeDap = xeDap;

    }

    public Float getTienGuiXeDap() {
        return tienGuiXeDap;
    }

    public void setTienGuiXeDap(Float tienGuiXeDap) {
        this.tienGuiXeDap = tienGuiXeDap;

    }

    public Float getTongTienGuiXe() {
        return tongTienGuiXe;
    }

    public Boolean getTrangThai() {
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

    public void setTongTienGuiXe(Float tongTienGuiXe) {
        this.tongTienGuiXe = tongTienGuiXe;
    }
    // ===== Helper methods =====

    public String getTrangThaiText() {
        if (trangThai == null)
            return "Chưa xác định";
        return trangThai ? "Đã thanh toán" : "Chưa thanh toán";
    }

    @Override
    public String toString() {
        return "TienGuiXe{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                ", xeOTo=" + xeOTo +
                ", tienGuiOTo=" + tienGuiOTo +
                ", xeMay=" + xeMay +
                ", tienGuiXeMay=" + tienGuiXeMay +
                ", xeDap=" + xeDap +
                ", tienGuiXeDap=" + tienGuiXeDap +
                ", tongTienGuiXe=" + tongTienGuiXe +
                ", trangThai=" + trangThai +
                '}';
    }
}