package com.example.quanlychungcu.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Entity
@Table(name = "TienNuoc")
@IdClass(FeeId.class)
public class TienNuoc {

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

    @Column(name = "SoKhoiNuocSuDung")
    private Float soKhoiNuocSuDung;

    @Column(name = "SoTienNuocPhaiTra")
    private Float soTienNuocPhaiTra;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuDan", referencedColumnName = "MaCuDan", insertable = false, updatable = false)
    private CuDan cuDan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCanHo", referencedColumnName = "MaCanHo", insertable = false, updatable = false)
    private CanHo canHo;

    // Constructors
    public TienNuoc() {}

    public TienNuoc(String maCuDan, String maCanHo, LocalDate ngayThu) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.trangThai = false;
    }

    public TienNuoc(String maCuDan, String maCanHo, LocalDate ngayThu,
                    Float soKhoiNuocSuDung, Float soTienNuocPhaiTra, Boolean trangThai) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
        this.soKhoiNuocSuDung = soKhoiNuocSuDung;
        this.soTienNuocPhaiTra = soTienNuocPhaiTra;
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

    public Float getSoKhoiNuocSuDung() {
        return soKhoiNuocSuDung;
    }

    public void setSoKhoiNuocSuDung(Float soKhoiNuocSuDung) {
        this.soKhoiNuocSuDung = soKhoiNuocSuDung;
        // Có thể tự động tính tiền nước theo đơn giá
        if (soKhoiNuocSuDung != null) {
            // Giả sử đơn giá nước là 15,000đ/m3
            this.soTienNuocPhaiTra = soKhoiNuocSuDung * 15000;
        }
    }

    public Float getSoTienNuocPhaiTra() {
        return soTienNuocPhaiTra;
    }

    public void setSoTienNuocPhaiTra(Float soTienNuocPhaiTra) {
        this.soTienNuocPhaiTra = soTienNuocPhaiTra;
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

    public void tinhTienNuoc() {
        if (soKhoiNuocSuDung != null) {
            // Đơn giá nước có thể lấy từ bảng cấu hình
            final float DON_GIA_NUOC = 15000; // 15,000đ/m3
            this.soTienNuocPhaiTra = soKhoiNuocSuDung * DON_GIA_NUOC;
        }
    }

    @Override
    public String toString() {
        return "TienNuoc{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                ", soKhoiNuocSuDung=" + soKhoiNuocSuDung +
                ", soTienNuocPhaiTra=" + soTienNuocPhaiTra +
                ", trangThai=" + trangThai +
                '}';
    }
}