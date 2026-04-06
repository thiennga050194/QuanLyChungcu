package com.example.quanlychungcu.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CUDAN_CANHO")
@IdClass(CuDanCanHoId.class)
public class CuDanCanHo {

    @Id
    @Column(name = "MaCuDan", length = 6, nullable = false)
    private String maCuDan;

    @Id
    @Column(name = "MaCanHo", length = 6, nullable = false)
    private String maCanHo;

    @Id
    @Column(name = "NgayBatDau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "VaiTro", nullable = false)
    private int vaiTro; // 0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành viên thuê

    @Column(name = "NgayKetThuc")
    private LocalDate ngayKetThuc;

    // Quan hệ với CUDAN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    // Quan hệ với CANHO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    // Constructors
    public CuDanCanHo() {
    }

    public CuDanCanHo(String maCuDan, String maCanHo, int vaiTro) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.vaiTro = vaiTro;
        this.ngayBatDau = LocalDate.now();
    }

    public CuDanCanHo(String maCuDan, String maCanHo, int vaiTro, LocalDate ngayBatDau) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.vaiTro = vaiTro;
        this.ngayBatDau = ngayBatDau;
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

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
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

    public void ketThucQuanHe() {
        this.ngayKetThuc = LocalDate.now();
    }
}