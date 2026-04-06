package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienVeSinhChungDTO;
import com.example.quanlychungcu.model.TienVeSinhChung;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.repository.TienVeSinhChungRepository;
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
public class TienVeSinhChungService {

    @Autowired
    private TienVeSinhChungRepository tienVeSinhChungRepository;

    // Chuyển Entity -> DTO
    private TienVeSinhChungDTO convertToDTO(TienVeSinhChung entity) {
        TienVeSinhChungDTO dto = new TienVeSinhChungDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setSoNguoi(entity.getSoNguoi() != null ? entity.getSoNguoi() : 0);
        dto.setTienVeSinhChung(entity.getTienVeSinhChung() != null ? entity.getTienVeSinhChung() : 0.0f);
        dto.setTrangThai(entity.getTrangThai() != null ? entity.getTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienVeSinhChung convertToEntity(TienVeSinhChungDTO dto) {
        TienVeSinhChung entity = new TienVeSinhChung();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienVeSinhChung(dto.getTienVeSinhChung());
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienVeSinhChungDTO> getAll() {
        return tienVeSinhChungRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienVeSinhChungDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienVeSinhChung> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienVeSinhChungRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienVeSinhChungRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienVeSinhChungDTO> getByMaCuDan(String maCuDan) {
        return tienVeSinhChungRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienVeSinhChungDTO> getByNgayThu(LocalDate ngayThu) {
        return tienVeSinhChungRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienVeSinhChungDTO> getByMonth(int year, int month) {
        return tienVeSinhChungRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienVeSinhChungDTO> getByTrangThai(boolean trangThai) {
        return tienVeSinhChungRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy top hộ đóng nhiều nhất
    public List<TienVeSinhChungDTO> getTopByMonth(int year, int month, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return tienVeSinhChungRepository.findTopByMonth(year, month, pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienVeSinhChungDTO create(@NonNull TienVeSinhChungDTO dto) {
        // Kiểm tra trùng lặp dựa trên FeeId (MaCuDan, MaCanHo, NgayThu)
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienVeSinhChungRepository.existsById(id)) {
            throw new RuntimeException("Đã có phí vệ sinh cho cư dân này tại căn hộ này trong ngày này!");
        }

        TienVeSinhChung entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienVeSinhChung saved = tienVeSinhChungRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienVeSinhChungDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienVeSinhChungDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienVeSinhChung entity = tienVeSinhChungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phí vệ sinh!"));

        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienVeSinhChung(dto.getTienVeSinhChung());
        entity.setTrangThai(dto.isTrangThai());

        TienVeSinhChung saved = tienVeSinhChungRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienVeSinhChungRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phí vệ sinh!");
        }
        tienVeSinhChungRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienVeSinhChung> list = tienVeSinhChungRepository.findByMonth(year, month);

        float totalAmount = 0;
        int totalPeople = 0;
        long paid = 0;
        long unpaid = 0;

        for (TienVeSinhChung item : list) {
            totalAmount += (item.getTienVeSinhChung() != null ? item.getTienVeSinhChung() : 0);
            totalPeople += (item.getSoNguoi() != null ? item.getSoNguoi() : 0);
            if (item.getTrangThai() != null && item.getTrangThai()) {
                paid++;
            } else {
                unpaid++;
            }
        }

        stats.put("total", list.size());
        stats.put("totalAmount", totalAmount);
        stats.put("totalPeople", totalPeople);
        stats.put("paid", paid);
        stats.put("unpaid", unpaid);

        stats.put("paidAmount", list.stream()
                .filter(t -> t.getTrangThai() != null && t.getTrangThai())
                .mapToDouble(t -> t.getTienVeSinhChung() != null ? t.getTienVeSinhChung() : 0)
                .sum());

        stats.put("paidPeople", list.stream()
                .filter(t -> t.getTrangThai() != null && t.getTrangThai())
                .mapToInt(t -> t.getSoNguoi() != null ? t.getSoNguoi() : 0)
                .sum());

        stats.put("avgAmount", list.size() > 0 ? totalAmount / list.size() : 0);
        stats.put("avgPerPerson", totalPeople > 0 ? totalAmount / totalPeople : 0);

        // Top 3 hộ đóng nhiều nhất
        stats.put("topPayers", getTopByMonth(year, month, 3));

        return stats;
    }

    // Lấy thống kê theo khu vực
    public Map<String, Object> getStatisticsByKhu(int year, int month) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalKhu", 0);
        stats.put("khuStats", new java.util.ArrayList<>());
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
        long yearPeople = 0;
        for (int m = 1; m <= 12; m++) {
            Object monthTotalObj = tienVeSinhChungRepository.sumByMonth(currentYear, m);
            Object monthPeopleObj = tienVeSinhChungRepository.sumPeopleByMonth(currentYear, m);
            if (monthTotalObj instanceof Number)
                yearTotal += ((Number) monthTotalObj).floatValue();
            if (monthPeopleObj instanceof Number)
                yearPeople += ((Number) monthPeopleObj).longValue();
        }
        overview.put("yearTotal", yearTotal);
        overview.put("yearPeople", yearPeople);

        return overview;
    }

    // Tính phí vệ sinh tự động
    public float calculateFee(int soNguoi, float donGia) {
        return soNguoi * donGia;
    }
}