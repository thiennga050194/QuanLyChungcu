package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.HopDongDTO;
import com.example.quanlychungcu.service.HopDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hopdong")
public class HopDongController {

    @Autowired
    private HopDongService hopDongService;

    // API cũ - lấy tất cả
    @GetMapping
    public List<HopDongDTO> getAll() {
        return hopDongService.getAll();
    }

    // API MỚI - phân trang
    @GetMapping("/paged")
    public ResponseEntity<Page<HopDongDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Page<HopDongDTO> result = hopDongService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy theo căn hộ
    @GetMapping("/canho/{maCanHo}")
    public List<HopDongDTO> getByMaCanHo(@PathVariable String maCanHo) {
        return hopDongService.getByMaCanHo(maCanHo);
    }

    // API lấy theo cư dân
    @GetMapping("/cudan/{maCuDan}")
    public List<HopDongDTO> getByMaCuDan(@PathVariable String maCuDan) {
        return hopDongService.getByMaCuDan(maCuDan);
    }

    // API lấy theo ngày
    @GetMapping("/ngay/{ngayGiaoDich}")
    public List<HopDongDTO> getByNgayGiaoDich(@PathVariable String ngayGiaoDich) {
        return hopDongService.getByNgayGiaoDich(ngayGiaoDich);
    }

    @GetMapping("/{maHopDong}")
    public ResponseEntity<HopDongDTO> getById(@PathVariable @NonNull String maHopDong) {
        return hopDongService.getById(maHopDong)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HopDongDTO save(@RequestBody @NonNull HopDongDTO hopDongDTO) {
        return hopDongService.save(hopDongDTO);
    }

    @DeleteMapping("/{maHopDong}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull String maHopDong) {
        hopDongService.delete(maHopDong);
        return ResponseEntity.ok().build();
    }
}