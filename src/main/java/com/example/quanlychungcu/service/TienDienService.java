package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienDienDTO;
import com.example.quanlychungcu.model.TienDien;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.repository.TienDienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TienDienService {

    @Autowired
    private TienDienRepository tienDienRepository;

    // Chuyển Entity -> DTO
    private TienDienDTO convertToDTO(TienDien entity) {
        TienDienDTO dto = new TienDienDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setTongSoDienSuDung(entity.getTongSoDienSuDung() != null ? entity.getTongSoDienSuDung() : 0.0f);
        dto.setTongSoTienPhaiTra(entity.getTongSoTienPhaiTra() != null ? entity.getTongSoTienPhaiTra() : 0.0f);
        dto.setTrangThai(entity.getTrangThai() != null ? entity.getTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienDien convertToEntity(TienDienDTO dto) {
        TienDien entity = new TienDien();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setTongSoDienSuDung(dto.getTongSoDienSuDung());
        entity.setTongSoTienPhaiTra(dto.getTongSoTienPhaiTra());
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienDienDTO> getAll() {
        return tienDienRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienDienDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienDien> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienDienRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienDienRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienDienDTO> getByMaCuDan(String maCuDan) {
        return tienDienRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienDienDTO> getByNgayThu(LocalDate ngayThu) {
        return tienDienRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienDienDTO> getByMonth(int year, int month) {
        return tienDienRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienDienDTO> getByTrangThai(boolean trangThai) {
        return tienDienRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienDienDTO create(@NonNull TienDienDTO dto) {
        // Kiểm tra trùng lặp dựa trên FeeId
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienDienRepository.existsById(id)) {
            throw new RuntimeException("Đã có hóa đơn tiền điện cho cư dân này tại căn hộ này trong ngày này!");
        }

        TienDien entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienDien saved = tienDienRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienDienDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienDienDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienDien entity = tienDienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn!"));

        entity.setTongSoDienSuDung(dto.getTongSoDienSuDung());
        entity.setTongSoTienPhaiTra(dto.getTongSoTienPhaiTra());
        entity.setTrangThai(dto.isTrangThai());

        TienDien saved = tienDienRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienDienRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy hóa đơn!");
        }
        tienDienRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienDien> list = tienDienRepository.findByMonth(year, month);

        float totalAmount = 0;
        float totalDien = 0;
        long paid = 0;
        long unpaid = 0;

        for (TienDien item : list) {
            totalAmount += item.getTongSoTienPhaiTra();
            totalDien += item.getTongSoDienSuDung();
            if (item.getTrangThai() != null && item.getTrangThai()) {
                paid++;
            } else {
                unpaid++;
            }
        }

        stats.put("total", list.size());
        stats.put("totalAmount", totalAmount);
        stats.put("totalDien", totalDien);
        stats.put("paid", paid);
        stats.put("unpaid", unpaid);
        stats.put("paidAmount", list.stream()
                .filter(item -> item.getTrangThai() != null && item.getTrangThai())
                .mapToDouble(TienDien::getTongSoTienPhaiTra)
                .sum());
        stats.put("avgDien", list.size() > 0 ? totalDien / list.size() : 0);
        stats.put("avgAmount", list.size() > 0 ? totalAmount / list.size() : 0);

        return stats;
    }

    // Lấy tổng quan
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new HashMap<>();

        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Tháng hiện tại
        overview.put("currentMonth", getMonthlyStatistics(currentYear, currentMonth));

        // Tháng trước
        LocalDate lastMonth = now.minusMonths(1);
        overview.put("lastMonth", getMonthlyStatistics(
                lastMonth.getYear(), lastMonth.getMonthValue()));

        // Cả năm
        float yearTotal = 0;
        for (int m = 1; m <= 12; m++) {
            Double monthTotal = tienDienRepository.sumByMonth(currentYear, m);
            if (monthTotal != null) {
                yearTotal += monthTotal.floatValue();
            }
        }
        overview.put("yearTotal", yearTotal);

        return overview;
    }
}