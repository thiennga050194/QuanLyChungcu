package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.TienGuiXeDTO;
import com.example.quanlychungcu.service.TienGuiXeService;
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
@RequestMapping("/api/tien/gui-xe")
public class TienGuiXeController {

    @Autowired
    private TienGuiXeService service;

    @GetMapping
    public List<TienGuiXeDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<TienGuiXeDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        return ResponseEntity.ok(service.getPaged(page, size, search, sortBy, sortDirection));
    }

    @GetMapping("/cudan/{maCuDan}")
    public List<TienGuiXeDTO> getByMaCuDan(@PathVariable String maCuDan) {
        return service.getByMaCuDan(maCuDan);
    }

    @GetMapping("/ngay/{ngayThu}")
    public List<TienGuiXeDTO> getByNgayThu(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayThu) {
        return service.getByNgayThu(ngayThu);
    }

    @GetMapping("/thang/{year}/{month}")
    public List<TienGuiXeDTO> getByMonth(
            @PathVariable int year,
            @PathVariable int month) {
        return service.getByMonth(year, month);
    }

    @GetMapping("/trangthai/{trangThai}")
    public List<TienGuiXeDTO> getByTrangThai(@PathVariable boolean trangThai) {
        return service.getByTrangThai(trangThai);
    }

    @GetMapping("/thongke/thang/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyStatistics(
            @PathVariable int year,
            @PathVariable int month) {
        return ResponseEntity.ok(service.getMonthlyStatistics(year, month));
    }

    @GetMapping("/thongke/tongquan")
    public ResponseEntity<Map<String, Object>> getOverview() {
        return ResponseEntity.ok(service.getOverview());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NonNull TienGuiXeDTO dto) {
        try {
            TienGuiXeDTO created = service.create(dto);
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
            @RequestBody @NonNull TienGuiXeDTO dto) {
        try {
            TienGuiXeDTO updated = service.update(maCuDan, maCanHo, ngayThu, dto);
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