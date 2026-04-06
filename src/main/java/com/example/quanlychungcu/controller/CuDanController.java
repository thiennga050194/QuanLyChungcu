package com.example.quanlychungcu.controller;

import com.example.quanlychungcu.dto.CuDanDTO;
import com.example.quanlychungcu.service.CuDanService;
import com.example.quanlychungcu.service.KhuCanHoService; // Thêm service này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cudan")
public class CuDanController {

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private KhuCanHoService khuCanHoService; // Inject service kiểm tra khu

    // API cũ - lấy tất cả (trả về List<DTO>)
    @GetMapping
    public List<CuDanDTO> getAll() {
        return cuDanService.getAll();
    }

    // API MỚI - phân trang (trả về Page<DTO>)
    @GetMapping("/paged")
    public ResponseEntity<Page<CuDanDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Page<CuDanDTO> result = cuDanService.getPaged(page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{maCuDan}")
    public ResponseEntity<CuDanDTO> getById(@PathVariable @NonNull String maCuDan) {
        return cuDanService.getById(maCuDan)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lấy cư dân theo vai trò (0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành
    // viên thuê)
    @GetMapping("/vaitro/{vaiTro}")
    public List<CuDanDTO> getByVaiTro(@PathVariable int vaiTro) {
        return cuDanService.getByVaiTro(vaiTro);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @NonNull CuDanDTO cuDanDTO) {
        try {
            // Log dữ liệu nhận được để debug
            System.out.println("=== DỮ LIỆU NHẬN ĐƯỢC ===");
            System.out.println("MaCuDan: " + cuDanDTO.getMaCuDan());
            System.out.println("TenCuDan: " + cuDanDTO.getTenCuDan());
            System.out.println("MaKhu: " + cuDanDTO.getMaKhu());
            System.out.println("MaCanHo: " + cuDanDTO.getMaCanHo());
            System.out.println("VaiTro: " + cuDanDTO.getVaiTro());

            // KIỂM TRA MaKhu CÓ TỒN TẠI KHÔNG
            if (cuDanDTO.getMaKhu() != null && !cuDanDTO.getMaKhu().isEmpty()) {
                boolean khuTonTai = khuCanHoService.existsById(cuDanDTO.getMaKhu());
                System.out.println("MaKhu tồn tại: " + khuTonTai);

                if (!khuTonTai) {
                    return ResponseEntity.badRequest()
                            .body("Mã khu '" + cuDanDTO.getMaKhu() + "' không tồn tại trong hệ thống!");
                }
            } else {
                return ResponseEntity.badRequest().body("Mã khu không được để trống!");
            }

            if (cuDanDTO.getMaCuDan() == null || cuDanDTO.getMaCuDan().isEmpty()) {
                return ResponseEntity.badRequest().body("Mã cư dân không được để trống!");
            }

            if (cuDanDTO.getTenCuDan() == null || cuDanDTO.getTenCuDan().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên cư dân không được để trống!");
            }

            CuDanDTO saved = cuDanService.save(cuDanDTO);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Lỗi khi thêm cư dân: " + e.getMessage());
        }
    }

    @DeleteMapping("/{maCuDan}")
    public ResponseEntity<?> delete(@PathVariable @NonNull String maCuDan) {
        try {
            System.out.println("=== XÓA CƯ DÂN: " + maCuDan + " ===");
            cuDanService.delete(maCuDan);
            System.out.println("=== XÓA THÀNH CÔNG: " + maCuDan + " ===");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("=== LỖI XÓA CƯ DÂN: " + maCuDan + " ===");
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi xóa cư dân: " + e.getMessage());
        }
    }

    @PutMapping("/{maCuDan}")
    public ResponseEntity<?> update(@PathVariable @NonNull String maCuDan, @RequestBody @NonNull CuDanDTO cuDanDTO) {
        try {
            System.out.println("=== CẬP NHẬT CƯ DÂN ===");
            System.out.println("Mã cần cập nhật: " + maCuDan);

            // Kiểm tra cư dân có tồn tại không
            if (!cuDanService.existsById(maCuDan)) {
                return ResponseEntity.notFound().build();
            }

            // KIỂM TRA MaKhu CÓ TỒN TẠI KHÔNG
            if (cuDanDTO.getMaKhu() != null && !cuDanDTO.getMaKhu().isEmpty()) {
                boolean khuTonTai = khuCanHoService.existsById(cuDanDTO.getMaKhu());
                if (!khuTonTai) {
                    return ResponseEntity.badRequest()
                            .body("Mã khu '" + cuDanDTO.getMaKhu() + "' không tồn tại trong hệ thống!");
                }
            } else {
                return ResponseEntity.badRequest().body("Mã khu không được để trống!");
            }

            // Kiểm tra các trường bắt buộc
            if (cuDanDTO.getTenCuDan() == null || cuDanDTO.getTenCuDan().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên cư dân không được để trống!");
            }

            // Set mã cư dân từ path vào DTO
            cuDanDTO.setMaCuDan(maCuDan);

            CuDanDTO updated = cuDanService.save(cuDanDTO);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật cư dân: " + e.getMessage());
        }
    }

}