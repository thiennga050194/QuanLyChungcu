package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienPhiDuyTriDTO;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.model.TienPhiDuyTri;
import com.example.quanlychungcu.repository.TienPhiDuyTriRepository;
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
public class TienPhiDuyTriService {

    @Autowired
    private TienPhiDuyTriRepository tienPhiDuyTriRepository;

    // Chuyển Entity -> DTO
    private TienPhiDuyTriDTO convertToDTO(TienPhiDuyTri entity) {
        TienPhiDuyTriDTO dto = new TienPhiDuyTriDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setSoNguoi(entity.getSoNguoi() != null ? entity.getSoNguoi() : 0);
        dto.setTienDuyTri(entity.getTienDuyTri() != null ? entity.getTienDuyTri() : 0.0f);
        dto.setTrangThai(entity.isTrangThai() != null ? entity.isTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienPhiDuyTri convertToEntity(TienPhiDuyTriDTO dto) {
        TienPhiDuyTri entity = new TienPhiDuyTri();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienDuyTri(dto.getTienDuyTri());
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienPhiDuyTriDTO> getAll() {
        return tienPhiDuyTriRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienPhiDuyTriDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienPhiDuyTri> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienPhiDuyTriRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienPhiDuyTriRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienPhiDuyTriDTO> getByMaCuDan(String maCuDan) {
        return tienPhiDuyTriRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienPhiDuyTriDTO> getByNgayThu(LocalDate ngayThu) {
        return tienPhiDuyTriRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienPhiDuyTriDTO> getByMonth(int year, int month) {
        return tienPhiDuyTriRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienPhiDuyTriDTO> getByTrangThai(boolean trangThai) {
        return tienPhiDuyTriRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienPhiDuyTriDTO create(@NonNull TienPhiDuyTriDTO dto) {
        // Kiểm tra trùng lặp
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienPhiDuyTriRepository.existsById(id)) {
            throw new RuntimeException("Đã có phí duy trì cho cư dân này tại căn hộ này trong tháng!");
        }

        TienPhiDuyTri entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienPhiDuyTri saved = tienPhiDuyTriRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienPhiDuyTriDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienPhiDuyTriDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienPhiDuyTri entity = tienPhiDuyTriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phí duy trì!"));

        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienDuyTri(dto.getTienDuyTri());
        entity.setTrangThai(dto.isTrangThai());

        TienPhiDuyTri saved = tienPhiDuyTriRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienPhiDuyTriRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phí duy trì!");
        }
        tienPhiDuyTriRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienPhiDuyTri> list = tienPhiDuyTriRepository.findByMonth(year, month);

        float totalAmount = 0;
        int totalPeople = 0;
        long paid = 0;
        long unpaid = 0;

        for (TienPhiDuyTri item : list) {
            totalAmount += item.getTienDuyTri();
            totalPeople += item.getSoNguoi();
            if (item.isTrangThai()) {
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
                .filter(TienPhiDuyTri::isTrangThai)
                .mapToDouble(TienPhiDuyTri::getTienDuyTri)
                .sum());
        stats.put("paidPeople", list.stream()
                .filter(TienPhiDuyTri::isTrangThai)
                .mapToInt(TienPhiDuyTri::getSoNguoi)
                .sum());
        stats.put("avgAmount", list.size() > 0 ? totalAmount / list.size() : 0);

        return stats;
    }

    // ❌ XÓA HOÀN TOÀN method getStatisticsByKhu (dòng 190-206)
    /*
     * public Map<String, Object> getStatisticsByKhu(int year, int month) {
     * Map<String, Object> result = new HashMap<>();
     * 
     * List<Map<String, Object>> khuStats = stats.stream()
     * .map(row -> {
     * Map<String, Object> item = new HashMap<>();
     * item.put("maKhu", row[0]);
     * item.put("soHo", row[1]);
     * item.put("tongTien", row[2]);
     * return item;
     * })
     * .collect(Collectors.toList());
     * 
     * result.put("khuStats", khuStats);
     * result.put("totalKhu", khuStats.size());
     * 
     * return result;
     * }
     */

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
            Double monthTotal = tienPhiDuyTriRepository.sumByMonth(currentYear, m);
            Long monthPeople = tienPhiDuyTriRepository.sumPeopleByMonth(currentYear, m);
            if (monthTotal != null)
                yearTotal += monthTotal.floatValue();
            if (monthPeople != null)
                yearPeople += monthPeople;
        }
        overview.put("yearTotal", yearTotal);
        overview.put("yearPeople", yearPeople);

        return overview;
    }

    // Tính phí duy trì tự động
    public float calculateFee(int soNguoi, float donGia) {
        return soNguoi * donGia;
    }
}