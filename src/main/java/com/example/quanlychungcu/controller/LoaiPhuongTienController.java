package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.LoaiPhuongTienDTO;
import com.example.quanlychungcu.service.LoaiPhuongTienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/phuongtien")
public class LoaiPhuongTienController {

    @Autowired
    private LoaiPhuongTienService loaiPhuongTienService;

    // API cũ - lấy tất cả
    @GetMapping
    public List<LoaiPhuongTienDTO> getAll() {
        return loaiPhuongTienService.getAll();
    }

    // API MỚI - phân trang
    @GetMapping("/paged")
    public ResponseEntity<Page<LoaiPhuongTienDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Page<LoaiPhuongTienDTO> result = loaiPhuongTienService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy theo mã cư dân
    @GetMapping("/cudan/{maCuDan}")
    public List<LoaiPhuongTienDTO> getByMaCuDan(@PathVariable String maCuDan) {
        return loaiPhuongTienService.getByMaCuDan(maCuDan);
    }

    // API lấy theo loại xe
    @GetMapping("/loaixe/{loaiXe}")
    public List<LoaiPhuongTienDTO> getByLoaiXe(@PathVariable String loaiXe) {
        return loaiPhuongTienService.getByLoaiXe(loaiXe);
    }

    // API thống kê
    @GetMapping("/thongke")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        return ResponseEntity.ok(loaiPhuongTienService.getStatisticsByLoaiXe());
    }

    // API đếm xe theo cư dân
    @GetMapping("/count/{maCuDan}")
    public ResponseEntity<Long> countByMaCuDan(@PathVariable String maCuDan) {
        return ResponseEntity.ok(loaiPhuongTienService.countByMaCuDan(maCuDan));
    }

    @GetMapping("/{maDangKy}")
    public ResponseEntity<LoaiPhuongTienDTO> getById(@PathVariable @NonNull String maDangKy) {
        return loaiPhuongTienService.getById(maDangKy)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @NonNull LoaiPhuongTienDTO loaiPhuongTienDTO) {
        try {
            LoaiPhuongTienDTO saved = loaiPhuongTienService.save(loaiPhuongTienDTO);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{maDangKy}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull String maDangKy,
            @RequestBody @NonNull LoaiPhuongTienDTO loaiPhuongTienDTO) {
        try {
            loaiPhuongTienDTO.setMaDangKy(maDangKy);
            LoaiPhuongTienDTO updated = loaiPhuongTienService.save(loaiPhuongTienDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{maDangKy}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull String maDangKy) {
        loaiPhuongTienService.delete(maDangKy);
        return ResponseEntity.ok().build();
    }
}