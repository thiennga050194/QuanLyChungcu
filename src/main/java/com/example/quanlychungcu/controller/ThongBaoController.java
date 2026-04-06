package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.ThongBaoDTO;
import com.example.quanlychungcu.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thongbao")
public class ThongBaoController {

    @Autowired
    private ThongBaoService thongBaoService;

    // API lấy tất cả thông báo
    @GetMapping
    public List<ThongBaoDTO> getAll() {
        return thongBaoService.getAll();
    }

    // API PHÂN TRANG
    @GetMapping("/paged")
    public ResponseEntity<Page<ThongBaoDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Page<ThongBaoDTO> result = thongBaoService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy thông báo mới nhất
    @GetMapping("/latest")
    public List<ThongBaoDTO> getLatest(@RequestParam(defaultValue = "5") int limit) {
        return thongBaoService.getLatest(limit);
    }

    // API lấy theo khoảng thời gian
    @GetMapping("/thoigian")
    public List<ThongBaoDTO> getByThoiGianBetween(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        return thongBaoService.getByThoiGianBetween(
                Timestamp.valueOf(start),
                Timestamp.valueOf(end)
        );
    }

    // API lấy theo năm
    @GetMapping("/nam/{year}")
    public List<ThongBaoDTO> getByYear(@PathVariable int year) {
        return thongBaoService.getByYear(year);
    }

    // API thống kê
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(thongBaoService.getStatistics());
    }

    @GetMapping("/{maThongBao}")
    public ResponseEntity<ThongBaoDTO> getById(@PathVariable @NonNull Integer maThongBao) {
        return thongBaoService.getById(maThongBao)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ThongBaoDTO create(@RequestBody @NonNull ThongBaoDTO thongBaoDTO) {
        return thongBaoService.create(thongBaoDTO);
    }

    @PutMapping("/{maThongBao}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull Integer maThongBao,
            @RequestBody @NonNull ThongBaoDTO thongBaoDTO) {
        try {
            ThongBaoDTO updated = thongBaoService.update(maThongBao, thongBaoDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{maThongBao}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Integer maThongBao) {
        thongBaoService.delete(maThongBao);
        return ResponseEntity.ok().build();
    }
}