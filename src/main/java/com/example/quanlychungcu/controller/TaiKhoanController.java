package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.TaiKhoanDTO;
import com.example.quanlychungcu.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    // API lấy thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<TaiKhoanDTO> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            TaiKhoanDTO user = taiKhoanService.getCurrentUser(username);
            return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(401).build();
    }

    // API lấy tất cả tài khoản (chỉ admin)
    @GetMapping("/all")
    public List<TaiKhoanDTO> getAll() {
        return taiKhoanService.getAll();
    }

    // API PHÂN TRANG
    @GetMapping("/paged")
    public ResponseEntity<Page<TaiKhoanDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Page<TaiKhoanDTO> result = taiKhoanService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy theo vai trò
    @GetMapping("/vaitro/{vaiTro}")
    public List<TaiKhoanDTO> getByVaiTro(@PathVariable int vaiTro) {
        return taiKhoanService.getByVaiTro(vaiTro);
    }

    // API thống kê
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(taiKhoanService.getStatistics());
    }

    @GetMapping("/{username}")
    public ResponseEntity<TaiKhoanDTO> getByUsername(@PathVariable @NonNull String username) {
        return taiKhoanService.getByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NonNull TaiKhoanDTO taiKhoanDTO) {
        try {
            TaiKhoanDTO created = taiKhoanService.create(taiKhoanDTO);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull String username,
            @RequestBody @NonNull TaiKhoanDTO taiKhoanDTO) {
        try {
            TaiKhoanDTO updated = taiKhoanService.update(username, taiKhoanDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> delete(@PathVariable @NonNull String username) {
        try {
            taiKhoanService.delete(username);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}