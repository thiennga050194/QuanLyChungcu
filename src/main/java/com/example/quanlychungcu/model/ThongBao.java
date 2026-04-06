package com.example.quanlychungcu.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ThongBao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThongBao")
    private int maThongBao;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "ThoiGian")
    private Timestamp thoiGian;

    public ThongBao() {
    }

    public ThongBao(int maThongBao, String tieuDe, String noiDung, Timestamp thoiGian) {
        this.maThongBao = maThongBao;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.thoiGian = thoiGian;
    }

    public int getMaThongBao() {
        return maThongBao;
    }

    public void setMaThongBao(int maThongBao) {
        this.maThongBao = maThongBao;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Timestamp getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Timestamp thoiGian) {
        this.thoiGian = thoiGian;
    }
}
