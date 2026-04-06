package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TienDien")
@IdClass(FeeId.class)
public class TienDien {

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

    @Column(name = "TongSoDienSuDung")
    private Float tongSoDienSuDung;

    @Column(name = "TongSoTienPhaiTra")
    private Float tongSoTienPhaiTra;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    // Constructor
    public TienDien() {}

    public TienDien(String maCuDan, String maCanHo, LocalDate ngayThu,
                    Float tongSoDienSuDung, Float tongSoTienPhaiTra, Boolean trangThai) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.tongSoDienSuDung = tongSoDienSuDung;
        this.tongSoTienPhaiTra = tongSoTienPhaiTra;
        this.trangThai = trangThai;
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

    public Float getTongSoDienSuDung() {
        return tongSoDienSuDung;
    }

    public void setTongSoDienSuDung(Float tongSoDienSuDung) {
        this.tongSoDienSuDung = tongSoDienSuDung;
    }

    public Float getTongSoTienPhaiTra() {
        return tongSoTienPhaiTra;
    }

    public void setTongSoTienPhaiTra(Float tongSoTienPhaiTra) {
        this.tongSoTienPhaiTra = tongSoTienPhaiTra;
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

    // ===== Helper methods =====
    public String getTrangThaiText() {
        if (trangThai == null) return "Chưa xác định";
        return trangThai ? "Đã thanh toán" : "Chưa thanh toán";
    }

    @Override
    public String toString() {
        return "TienDien{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                ", tongSoDienSuDung=" + tongSoDienSuDung +
                ", tongSoTienPhaiTra=" + tongSoTienPhaiTra +
                ", trangThai=" + trangThai +
                '}';
    }
}