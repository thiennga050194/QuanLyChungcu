package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TIENVESINHCHUNG")
@IdClass(FeeId.class)
public class TienVeSinhChung {

    @Id
    @Column(name = "MaCuDan", length = 6, nullable = false)
    private String maCuDan;

    @Id
    @Column(name = "MaCanHo", length = 6, nullable = false) // THIẾU cột này
    private String maCanHo;

    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "NgayThu", nullable = false)
    private LocalDate ngayThu;

    @Column(name = "SoNguoi")
    private Integer soNguoi;

    @Column(name = "TienVeSinhChung")
    private Float tienVeSinhChung;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    // Quan hệ với CUDAN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    // Quan hệ với CANHO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    public TienVeSinhChung() {
    }

    public TienVeSinhChung(String maCuDan, String maCanHo, LocalDate ngayThu,
            Integer soNguoi, Float tienVeSinhChung, Boolean trangThai) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.soNguoi = soNguoi;
        this.tienVeSinhChung = tienVeSinhChung;
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

    public Float getTienVeSinhChung() {
        return tienVeSinhChung;
    }

    public void setTienVeSinhChung(Float tienVeSinhChung) {
        this.tienVeSinhChung = tienVeSinhChung;
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
        if (trangThai == null)
            return "Chưa xác định";
        return trangThai ? "Đã thanh toán" : "Chưa thanh toán";
    }

    @Override
    public String toString() {
        return "TienVeSinhChung{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                ", soNguoi=" + soNguoi +
                ", tienVeSinhChung=" + tienVeSinhChung +
                ", trangThai=" + trangThai +
                '}';
    }
}