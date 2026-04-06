package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TienPhiDuyTri")
@IdClass(FeeId.class)
public class TienPhiDuyTri {

    @Id
    @Column(name = "MaCuDan", length = 6, nullable = false)
    private String maCuDan;

    @Id
    @Column(name = "MaCanHo", length = 6, nullable = false)  // THÊM MaCanHo
    private String maCanHo;

    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "NgayThu", nullable = false)
    private LocalDate ngayThu;

    @Column(name = "SoNguoi")
    private Integer soNguoi;  // Đổi từ int sang Integer

    @Column(name = "TienDuyTri")
    private Float tienDuyTri;  // Đổi từ float sang Float

    @Column(name = "TrangThai")
    private Boolean trangThai;  // Đổi từ boolean sang Boolean

    // Quan hệ với CUDAN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    // Quan hệ với CANHO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    public TienPhiDuyTri() {
    }

    public TienPhiDuyTri(String maCuDan, String maCanHo, LocalDate ngayThu,
                         Integer soNguoi, Float tienDuyTri, Boolean trangThai) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.soNguoi = soNguoi;
        this.tienDuyTri = tienDuyTri;
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

    public Float getTienDuyTri() {
        return tienDuyTri;
    }

    public void setTienDuyTri(Float tienDuyTri) {
        this.tienDuyTri = tienDuyTri;
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

    // ===== Helper methods =====
    public String getTrangThaiText() {
        if (trangThai == null) return "Chưa xác định";
        return trangThai ? "Đã thanh toán" : "Chưa thanh toán";
    }

    @Override
    public String toString() {
        return "TienPhiDuyTri{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                ", soNguoi=" + soNguoi +
                ", tienDuyTri=" + tienDuyTri +
                ", trangThai=" + trangThai +
                '}';
    }
}