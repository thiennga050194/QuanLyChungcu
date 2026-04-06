package com.example.quanlychungcu.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CuDanCanHoId implements Serializable {

    private String maCuDan;
    private String maCanHo;
    private LocalDate ngayBatDau;

    public CuDanCanHoId() {}

    public CuDanCanHoId(String maCuDan, String maCanHo, LocalDate ngayBatDau) {
        this.maCuDan = maCuDan;
        this.maCanHo = maCanHo;
        this.ngayBatDau = ngayBatDau;
    }

    public String getMaCuDan() { return maCuDan; }
    public void setMaCuDan(String maCuDan) { this.maCuDan = maCuDan; }

    public String getMaCanHo() { return maCanHo; }
    public void setMaCanHo(String maCanHo) { this.maCanHo = maCanHo; }

    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CuDanCanHoId that = (CuDanCanHoId) o;
        return Objects.equals(maCuDan, that.maCuDan) &&
                Objects.equals(maCanHo, that.maCanHo) &&
                Objects.equals(ngayBatDau, that.ngayBatDau);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maCuDan, maCanHo, ngayBatDau);
    }
}