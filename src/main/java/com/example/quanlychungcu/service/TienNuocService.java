package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienNuocDTO;
import com.example.quanlychungcu.model.TienNuoc;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.repository.TienNuocRepository;
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
public class TienNuocService {

    @Autowired
    private TienNuocRepository tienNuocRepository;

    // Chuyển Entity -> DTO
    private TienNuocDTO convertToDTO(TienNuoc entity) {
        TienNuocDTO dto = new TienNuocDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setSoKhoiNuocSuDung(entity.getSoKhoiNuocSuDung() != null ? entity.getSoKhoiNuocSuDung() : 0.0f);
        dto.setSoTienNuocPhaiTra(entity.getSoTienNuocPhaiTra() != null ? entity.getSoTienNuocPhaiTra() : 0.0f);
        dto.setTrangThai(entity.getTrangThai() != null ? entity.getTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienNuoc convertToEntity(TienNuocDTO dto) {
        TienNuoc entity = new TienNuoc();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setSoKhoiNuocSuDung(dto.getSoKhoiNuocSuDung());
        entity.setSoTienNuocPhaiTra(dto.getSoTienNuocPhaiTra());
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienNuocDTO> getAll() {
        return tienNuocRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienNuocDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienNuoc> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienNuocRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienNuocRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienNuocDTO> getByMaCuDan(String maCuDan) {
        return tienNuocRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienNuocDTO> getByNgayThu(LocalDate ngayThu) {
        return tienNuocRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienNuocDTO> getByMonth(int year, int month) {
        return tienNuocRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienNuocDTO> getByTrangThai(boolean trangThai) {
        return tienNuocRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy top hộ dùng nhiều nước
    public List<TienNuocDTO> getTopByMonth(int year, int month, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return tienNuocRepository.findTopByMonth(year, month, pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienNuocDTO create(@NonNull TienNuocDTO dto) {
        // Kiểm tra trùng lặp dựa trên FeeId
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienNuocRepository.existsById(id)) {
            throw new RuntimeException("Đã có hóa đơn tiền nước cho cư dân này tại căn hộ này trong ngày này!");
        }

        // Đảm bảo soKhoiNuocSuDung không null
        if (dto.getSoKhoiNuocSuDung() == null) {
            dto.setSoKhoiNuocSuDung(0f);
        }

        TienNuoc entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienNuoc saved = tienNuocRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienNuocDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienNuocDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienNuoc entity = tienNuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn!"));

        entity.setSoKhoiNuocSuDung(dto.getSoKhoiNuocSuDung());
        entity.setSoTienNuocPhaiTra(dto.getSoTienNuocPhaiTra());
        entity.setTrangThai(dto.isTrangThai());

        TienNuoc saved = tienNuocRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienNuocRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy hóa đơn!");
        }
        tienNuocRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienNuoc> list = tienNuocRepository.findByMonth(year, month);

        float totalAmount = 0;
        float totalKhoi = 0;
        long paid = 0;
        long unpaid = 0;

        for (TienNuoc item : list) {
            totalAmount += item.getSoTienNuocPhaiTra() != null ? item.getSoTienNuocPhaiTra() : 0;
            totalKhoi += item.getSoKhoiNuocSuDung() != null ? item.getSoKhoiNuocSuDung() : 0;
            if (item.getTrangThai() != null && item.getTrangThai()) {
                paid++;
            } else {
                unpaid++;
            }
        }

        stats.put("total", list.size());
        stats.put("totalAmount", totalAmount);
        stats.put("totalKhoi", totalKhoi);
        stats.put("paid", paid);
        stats.put("unpaid", unpaid);
        stats.put("paidAmount", list.stream()
                .filter(item -> item.getTrangThai() != null && item.getTrangThai())
                .mapToDouble(item -> item.getSoTienNuocPhaiTra() != null ? item.getSoTienNuocPhaiTra() : 0)
                .sum());
        stats.put("avgKhoi", list.size() > 0 ? totalKhoi / list.size() : 0);
        stats.put("avgAmount", list.size() > 0 ? totalAmount / list.size() : 0);

        // Top 3 hộ dùng nhiều nhất
        stats.put("topUsers", getTopByMonth(year, month, 3));

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
        float yearKhoi = 0;
        for (int m = 1; m <= 12; m++) {
            Double monthTotal = tienNuocRepository.sumByMonth(currentYear, m);
            Double monthKhoi = tienNuocRepository.sumKhoiByMonth(currentYear, m);
            if (monthTotal != null)
                yearTotal += monthTotal.floatValue();
            if (monthKhoi != null)
                yearKhoi += monthKhoi.floatValue();
        }
        overview.put("yearTotal", yearTotal);
        overview.put("yearKhoi", yearKhoi);

        return overview;
    }
}