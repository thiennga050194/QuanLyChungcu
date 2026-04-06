package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.CanHoDTO;
import com.example.quanlychungcu.service.CanHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canho")
public class CanHoController {

    @Autowired
    private CanHoService canHoService;

    // API cũ - lấy tất cả
    @GetMapping
    public List<CanHoDTO> getAll() {
        return canHoService.getAll();
    }

    // LEGACY: Một số trang frontend gọi /with-tenkhu
    @GetMapping("/with-tenkhu")
    public List<CanHoDTO> getAllWithTenKhu() {
        return canHoService.getAll();
    }

    // API MỚI - phân trang
    @GetMapping("/paged")
    public ResponseEntity<Page<CanHoDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Page<CanHoDTO> result = canHoService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    // API lấy căn hộ trống
    @GetMapping("/empty")
    public List<CanHoDTO> getEmptyApartments() {
        return canHoService.getEmptyApartments();
    }

    // API lấy theo khu
    @GetMapping("/khu/{maKhu}")
    public List<CanHoDTO> getByMaKhu(@PathVariable String maKhu) {
        return canHoService.getByMaKhu(maKhu);
    }

    @GetMapping("/{maCanHo}")
    public ResponseEntity<CanHoDTO> getById(@PathVariable @NonNull String maCanHo) {
        return canHoService.getById(maCanHo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CanHoDTO save(@RequestBody @NonNull CanHoDTO canHoDTO) {
        return canHoService.save(canHoDTO);
    }

    @PutMapping("/{maCanHo}")
    public ResponseEntity<CanHoDTO> update(@PathVariable @NonNull String maCanHo,
            @RequestBody @NonNull CanHoDTO canHoDTO) {
        canHoDTO.setMaCanHo(maCanHo);
        return ResponseEntity.ok(canHoService.save(canHoDTO));
    }

    @DeleteMapping("/{maCanHo}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull String maCanHo) {
        canHoService.delete(maCanHo);
        return ResponseEntity.ok().build();
    }
}