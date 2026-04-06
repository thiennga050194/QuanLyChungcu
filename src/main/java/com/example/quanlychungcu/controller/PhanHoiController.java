package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.PhanHoiDTO;
import com.example.quanlychungcu.service.PhanHoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/phanhoi")
public class PhanHoiController {

    @Autowired
    private PhanHoiService phanHoiService;

    // API cũ - lấy tất cả
    @GetMapping
    public List<PhanHoiDTO> getAll() {
        return phanHoiService.getAll();
    }

    // API MỚI - phân trang
    @GetMapping("/paged")
    public ResponseEntity<Page<PhanHoiDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Page<PhanHoiDTO> result = phanHoiService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy theo tài khoản
    @GetMapping("/taikhoan/{taiKhoan}")
    public List<PhanHoiDTO> getByTaiKhoan(@PathVariable String taiKhoan) {
        return phanHoiService.getByTaiKhoan(taiKhoan);
    }

    // API lấy theo ban quản lý
    @GetMapping("/banquanly/{banquanly}")
    public List<PhanHoiDTO> getByBanquanly(@PathVariable int banquanly) {
        return phanHoiService.getByBanquanly(banquanly);
    }

    // API lấy phản hồi có hình ảnh
    @GetMapping("/cohinhanh")
    public List<PhanHoiDTO> getCoHinhAnh() {
        return phanHoiService.getCoHinhAnh();
    }

    // API lấy phản hồi mới nhất
    @GetMapping("/latest")
    public List<PhanHoiDTO> getLatest(@RequestParam(defaultValue = "10") int limit) {
        return phanHoiService.getLatest(limit);
    }

    // API lấy theo khoảng thời gian
    @GetMapping("/thoigian")
    public List<PhanHoiDTO> getByThoiGianBetween(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end) {
        return phanHoiService.getByThoiGianBetween(
                Timestamp.valueOf(start),
                Timestamp.valueOf(end)
        );
    }

    // API đếm theo tài khoản
    @GetMapping("/count/{taiKhoan}")
    public ResponseEntity<Long> countByTaiKhoan(@PathVariable String taiKhoan) {
        return ResponseEntity.ok(phanHoiService.countByTaiKhoan(taiKhoan));
    }

    @GetMapping("/{maPhanHoi}")
    public ResponseEntity<PhanHoiDTO> getById(@PathVariable @NonNull Integer maPhanHoi) {
        return phanHoiService.getById(maPhanHoi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PhanHoiDTO save(@RequestBody @NonNull PhanHoiDTO phanHoiDTO) {
        return phanHoiService.save(phanHoiDTO);
    }

    @PutMapping("/{maPhanHoi}")
    public ResponseEntity<PhanHoiDTO> update(
            @PathVariable @NonNull Integer maPhanHoi,
            @RequestBody @NonNull PhanHoiDTO phanHoiDTO) {
        phanHoiDTO.setMaPhanHoi(maPhanHoi);
        PhanHoiDTO updated = phanHoiService.save(phanHoiDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{maPhanHoi}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Integer maPhanHoi) {
        phanHoiService.delete(maPhanHoi);
        return ResponseEntity.ok().build();
    }
}