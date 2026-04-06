package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.TienVeSinhChungDTO;
import com.example.quanlychungcu.service.TienVeSinhChungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tien/ve-sinh-chung")
public class TienVeSinhChungController {

    @Autowired
    private TienVeSinhChungService service;

    @GetMapping
    public List<TienVeSinhChungDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TienVeSinhChungDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        return ResponseEntity.ok(service.getPaged(page, size, search, sortBy, sortDirection));
    }

    @GetMapping("/cudan/{maCuDan}")
    public List<TienVeSinhChungDTO> getByMaCuDan(@PathVariable String maCuDan) {
        return service.getByMaCuDan(maCuDan);
    }

    @GetMapping("/ngay/{ngayThu}")
    public List<TienVeSinhChungDTO> getByNgayThu(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayThu) {
        return service.getByNgayThu(ngayThu);
    }

    @GetMapping("/thang/{year}/{month}")
    public List<TienVeSinhChungDTO> getByMonth(
            @PathVariable int year,
            @PathVariable int month) {
        return service.getByMonth(year, month);
    }

    @GetMapping("/trangthai/{trangThai}")
    public List<TienVeSinhChungDTO> getByTrangThai(@PathVariable boolean trangThai) {
        return service.getByTrangThai(trangThai);
    }

    @GetMapping("/top/{year}/{month}")
    public List<TienVeSinhChungDTO> getTopByMonth(
            @PathVariable int year,
            @PathVariable int month,
            @RequestParam(defaultValue = "3") int limit) {
        return service.getTopByMonth(year, month, limit);
    }

    @GetMapping("/thongke/thang/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(service.getMonthlyStatistics(year, month));
    }

    @GetMapping("/thongke/khu/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getStatisticsByKhu(
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(service.getStatisticsByKhu(year, month));
    }

    @GetMapping("/thongke/tongquan")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(service.getOverview());
    }

    @GetMapping("/tinh-phi")
    public ResponseEntity<Float> calculateFee(
            @RequestParam int soNguoi,
            @RequestParam float donGia) {
        return ResponseEntity.ok(service.calculateFee(soNguoi, donGia));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NonNull TienVeSinhChungDTO dto) {
        try {
            TienVeSinhChungDTO created = service.create(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{maCuDan}/{maCanHo}/{ngayThu}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull String maCuDan,
            @PathVariable @NonNull String maCanHo,
            @PathVariable @NonNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayThu,
            @RequestBody @NonNull TienVeSinhChungDTO dto) {
        try {
            TienVeSinhChungDTO updated = service.update(maCuDan, maCanHo, ngayThu, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{maCuDan}/{maCanHo}/{ngayThu}")
    public ResponseEntity<?> delete(
            @PathVariable @NonNull String maCuDan,
            @PathVariable @NonNull String maCanHo,
            @PathVariable @NonNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayThu) {
        try {
            service.delete(maCuDan, maCanHo, ngayThu);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}