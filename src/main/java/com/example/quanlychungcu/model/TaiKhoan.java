package com.example.quanlychungcu.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan implements UserDetails {

    @Id
    @Column(name = "TenTaiKhoan")
    private String tenTaiKhoan;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "VaiTro")
    private int vaiTro; // 0: Admin, 1: Nhân viên

    public TaiKhoan() {
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau, int vaiTro) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    public TaiKhoan(String tenTaiKhoan, String matKhau) {
        this.tenTaiKhoan = tenTaiKhoan;
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" + "tenTaiKhoan=" + tenTaiKhoan + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro + '}';
    }

    // Implement các phương thức của UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Chuyển đổi vaiTro (int) thành GrantedAuthority
        String roleName;
        if (vaiTro == 0) {
            roleName = "ROLE_ADMIN";
        } else if (vaiTro == 1) {
            roleName = "ROLE_STAFF";
        } else {
            roleName = "ROLE_USER"; // fallback
        }
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return matKhau;
    }

    @Override
    public String getUsername() {
        return tenTaiKhoan;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters và Setters
    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public int getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(int vaiTro) {
        this.vaiTro = vaiTro;
    }
}