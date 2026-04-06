package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.CuDanCanHoDTO;
import com.example.quanlychungcu.service.CuDanCanHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cu-dan-can-ho")
public class CuDanCanHoController {

    @Autowired
    private CuDanCanHoService service;

    @GetMapping
    public List<CuDanCanHoDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CuDanCanHoDTO>> search(
            @RequestParam(required = false) String maCuDan,
            @RequestParam(required = false) String maCanHo,
            @RequestParam(required = false) Integer vaiTro,
            @RequestParam(defaultValue = "all") String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngayBatDau") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        return ResponseEntity.ok(service.search(maCuDan, maCanHo, vaiTro, trangThai, page, size, sortBy, sortDir));
    }

    @GetMapping("/cudan/{maCuDan}")
    public List<CuDanCanHoDTO> getByMaCuDan(@PathVariable String maCuDan) {
        return service.getByMaCuDan(maCuDan);
    }

    @GetMapping("/canho/{maCanHo}")
    public List<CuDanCanHoDTO> getByMaCanHo(@PathVariable String maCanHo) {
        return service.getByMaCanHo(maCanHo);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NonNull CuDanCanHoDTO dto) {
        try {
            return ResponseEntity.ok(service.create(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{maCuDan}/{maCanHo}/{ngayBatDau}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull String maCuDan,
            @PathVariable @NonNull String maCanHo,
            @PathVariable @NonNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayBatDau,
            @RequestBody @NonNull CuDanCanHoDTO dto) {
        try {
            return ResponseEntity.ok(service.update(maCuDan, maCanHo, ngayBatDau, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{maCuDan}/{maCanHo}/{ngayBatDau}")
    public ResponseEntity<?> delete(
            @PathVariable @NonNull String maCuDan,
            @PathVariable @NonNull String maCanHo,
            @PathVariable @NonNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ngayBatDau) {
        try {
            service.delete(maCuDan, maCanHo, ngayBatDau);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/terminate/{maCuDan}/{maCanHo}")
    public ResponseEntity<?> terminate(
            @PathVariable @NonNull String maCuDan,
            @PathVariable @NonNull String maCanHo) {
        try {
            return ResponseEntity.ok(service.terminate(maCuDan, maCanHo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
