package com.example.quanlychungcu.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "KHUCANHO")
public class KhuCanHo {

    @Id
    @Column(name = "MaKhu", length = 2)
    private String maKhu;

    @Column(name = "TenKhu", nullable = false)
    private String tenKhu;

    @Column(name = "SoTang", nullable = false)
    private int soTang;

    @Column(name = "SoCanTT", nullable = false)
    private int soCanTT;

    @Column(name = "DiaChi", nullable = false)
    private String diaChi;

    // Quan hệ OneToMany với CANHO
    @OneToMany(mappedBy = "khuCanHo", fetch = FetchType.LAZY)
    private List<CanHo> danhSachCanHo;

    public KhuCanHo() {}

    public KhuCanHo(String maKhu, String tenKhu, int soTang, int soCanTT, String diaChi) {
        this.maKhu = maKhu;
        this.tenKhu = tenKhu;
        this.soTang = soTang;
        this.soCanTT = soCanTT;
        this.diaChi = diaChi;
    }

    // Getters và Setters
    public String getMaKhu() { return maKhu; }
    public void setMaKhu(String maKhu) { this.maKhu = maKhu; }

    public String getTenKhu() { return tenKhu; }
    public void setTenKhu(String tenKhu) { this.tenKhu = tenKhu; }

    public int getSoTang() { return soTang; }
    public void setSoTang(int soTang) { this.soTang = soTang; }

    public int getSoCanTT() { return soCanTT; }
    public void setSoCanTT(int soCanTT) { this.soCanTT = soCanTT; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public List<CanHo> getDanhSachCanHo() { return danhSachCanHo; }
    public void setDanhSachCanHo(List<CanHo> danhSachCanHo) { this.danhSachCanHo = danhSachCanHo; }
}