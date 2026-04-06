package com.example.quanlychungcu.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class FeeId implements Serializable {

    private String maCuDan;
    private String maCanHo;
    private LocalDate ngayThu;

    public FeeId() {}

    public FeeId(String maCuDan, String maCanHo, LocalDate ngayThu) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayThu = ngayThu;
    }

    // ===== Getters và Setters =====
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

    // ===== equals và hashCode (RẤT QUAN TRỌNG) =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeeId feeId = (FeeId) o;
        return Objects.equals(maCuDan, feeId.maCuDan) &&
                Objects.equals(maCanHo, feeId.maCanHo) &&
                Objects.equals(ngayThu, feeId.ngayThu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCuDan, maCanHo, ngayThu);
    }

    // ===== toString cho debug =====
    @Override
    public String toString() {
        return "FeeId{" +
                "maCuDan='" + maCuDan + '\'' +
                ", maCanHo='" + maCanHo + '\'' +
                ", ngayThu=" + ngayThu +
                '}';
    }
}