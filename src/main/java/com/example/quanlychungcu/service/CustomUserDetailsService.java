package com.example.quanlychungcu.service;

import com.example.quanlychungcu.model.TaiKhoan;
import com.example.quanlychungcu.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenTaiKhoan(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        // 0: Admin, 1: Nhân viên (Staff), 2: Cư dân (User)
        String role;
        switch (taiKhoan.getVaiTro()) {
            case 0:
                role = "ROLE_ADMIN";
                break;
            case 1:
                role = "ROLE_STAFF";
                break;
            case 2:
                role = "ROLE_USER";
                break;
            default:
                role = "ROLE_USER";
        }

        return new org.springframework.security.core.userdetails.User(
                taiKhoan.getTenTaiKhoan(),
                taiKhoan.getMatKhau(),
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
