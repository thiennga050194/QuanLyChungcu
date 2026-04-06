package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.KhuCanHoDTO;
import com.example.quanlychungcu.service.KhuCanHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/khu")
public class KhuCanHoController {

    @Autowired
    private KhuCanHoService khuCanHoService;

    // API cũ - lấy tất cả
    @GetMapping
    public List<KhuCanHoDTO> getAll() {
        return khuCanHoService.getAll();
    }

    // API MỚI - phân trang
    @GetMapping("/paged")
    public ResponseEntity<Page<KhuCanHoDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Page<KhuCanHoDTO> result = khuCanHoService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy top khu
    @GetMapping("/top")
    public List<KhuCanHoDTO> getTopKhu(@RequestParam(defaultValue = "5") int limit) {
        return khuCanHoService.getTopKhuBySoCan(limit);
    }

    // API lấy tổng số căn hộ
    @GetMapping("/total-apartments")
    public ResponseEntity<Map<String, Long>> getTotalApartments() {
        Map<String, Long> response = new HashMap<>();
        response.put("total", khuCanHoService.getTotalApartments());
        return ResponseEntity.ok(response);
    }

    // API lấy theo tên
    @GetMapping("/ten/{tenKhu}")
    public ResponseEntity<KhuCanHoDTO> getByTenKhu(@PathVariable String tenKhu) {
        KhuCanHoDTO result = khuCanHoService.getByTenKhu(tenKhu);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{maKhu}")
    public ResponseEntity<KhuCanHoDTO> getById(@PathVariable @NonNull String maKhu) {
        return khuCanHoService.getById(maKhu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @NonNull KhuCanHoDTO khuCanHoDTO) {
        try {
            KhuCanHoDTO saved = khuCanHoService.save(khuCanHoDTO);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add-complex")
    public ResponseEntity<?> addComplex(@RequestBody Map<String, Object> payload) {
        try {
            KhuCanHoDTO khuDTO = new KhuCanHoDTO();
            khuDTO.setTenKhu(payload.get("tenKhu") != null ? payload.get("tenKhu").toString() : "");
            khuDTO.setSoTang(payload.get("soTang") != null ? Integer.parseInt(payload.get("soTang").toString()) : 0);
            khuDTO.setSoCanTT(payload.get("soCanTT") != null ? Integer.parseInt(payload.get("soCanTT").toString()) : 0);
            khuDTO.setDiaChi(payload.get("diaChi") != null ? payload.get("diaChi").toString() : "");

            float dienTich = Float.parseFloat(payload.get("dienTich").toString());
            int soPhong = Integer.parseInt(payload.get("soPhong").toString());

            KhuCanHoDTO saved = khuCanHoService.addComplex(khuDTO, dienTich, soPhong);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{maKhu}")
    public ResponseEntity<?> update(
            @PathVariable @NonNull String maKhu,
            @RequestBody @NonNull KhuCanHoDTO khuCanHoDTO) {
        try {
            khuCanHoDTO.setMaKhu(maKhu);
            KhuCanHoDTO updated = khuCanHoService.save(khuCanHoDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{maKhu}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull String maKhu) {
        khuCanHoService.delete(maKhu);
        return ResponseEntity.ok().build();
    }
}