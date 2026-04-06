package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienGuiXeDTO;
import com.example.quanlychungcu.model.TienGuiXe;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.repository.TienGuiXeRepository;
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
public class TienGuiXeService {

    @Autowired
    private TienGuiXeRepository tienGuiXeRepository;

    // Chuyển Entity -> DTO
    private TienGuiXeDTO convertToDTO(TienGuiXe entity) {
        TienGuiXeDTO dto = new TienGuiXeDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setXeOTo(entity.getXeOTo() != null ? entity.getXeOTo() : 0);
        dto.setTienGuiOTo(entity.getTienGuiOTo() != null ? entity.getTienGuiOTo() : 0.0f);
        dto.setXeMay(entity.getXeMay() != null ? entity.getXeMay() : 0);
        dto.setTienGuiXeMay(entity.getTienGuiXeMay() != null ? entity.getTienGuiXeMay() : 0.0f);
        dto.setXeDap(entity.getXeDap() != null ? entity.getXeDap() : 0);
        dto.setTienGuiXeDap(entity.getTienGuiXeDap() != null ? entity.getTienGuiXeDap() : 0.0f);
        dto.setTongTienGuiXe(entity.getTongTienGuiXe() != null ? entity.getTongTienGuiXe() : 0.0f);
        dto.setTrangThai(entity.getTrangThai() != null ? entity.getTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienGuiXe convertToEntity(TienGuiXeDTO dto) {
        TienGuiXe entity = new TienGuiXe();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setXeOTo(dto.getXeOTo());
        entity.setTienGuiOTo(dto.getTienGuiOTo());
        entity.setXeMay(dto.getXeMay());
        entity.setTienGuiXeMay(dto.getTienGuiXeMay());
        entity.setXeDap(dto.getXeDap());
        entity.setTienGuiXeDap(dto.getTienGuiXeDap());
        entity.setTongTienGuiXe(dto.getTongTienGuiXe());

        // entity.setTongTienGuiXe(dto.getTongTienGuiXe()); // Bỏ vì entity tự tính
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienGuiXeDTO> getAll() {
        return tienGuiXeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienGuiXeDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienGuiXe> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienGuiXeRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienGuiXeRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienGuiXeDTO> getByMaCuDan(String maCuDan) {
        return tienGuiXeRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienGuiXeDTO> getByNgayThu(LocalDate ngayThu) {
        return tienGuiXeRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienGuiXeDTO> getByMonth(int year, int month) {
        return tienGuiXeRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienGuiXeDTO> getByTrangThai(boolean trangThai) {
        return tienGuiXeRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienGuiXeDTO create(@NonNull TienGuiXeDTO dto) {
        // Kiểm tra trùng lặp dựa trên FeeId
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienGuiXeRepository.existsById(id)) {
            throw new RuntimeException("Đã có bản ghi tiền gửi xe cho cư dân này tại căn hộ này trong tháng!");
        }

        TienGuiXe entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienGuiXe saved = tienGuiXeRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienGuiXeDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienGuiXeDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienGuiXe entity = tienGuiXeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi!"));

        entity.setXeOTo(dto.getXeOTo());
        entity.setTienGuiOTo(dto.getTienGuiOTo());
        entity.setXeMay(dto.getXeMay());
        entity.setTienGuiXeMay(dto.getTienGuiXeMay());
        entity.setXeDap(dto.getXeDap());
        entity.setTienGuiXeDap(dto.getTienGuiXeDap());
        entity.setTongTienGuiXe(dto.getTongTienGuiXe());

        // Tổng tiền tự động tính qua các setter trên

        entity.setTrangThai(dto.isTrangThai());

        TienGuiXe saved = tienGuiXeRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienGuiXeRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bản ghi!");
        }
        tienGuiXeRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienGuiXe> list = tienGuiXeRepository.findByMonth(year, month);

        float totalAmount = 0;
        long paid = 0;
        long unpaid = 0;
        int totalOTo = 0, totalXeMay = 0, totalXeDap = 0;

        for (TienGuiXe item : list) {
            totalAmount += item.getTongTienGuiXe() != null ? item.getTongTienGuiXe() : 0;
            totalOTo += item.getXeOTo() != null ? item.getXeOTo() : 0;
            totalXeMay += item.getXeMay() != null ? item.getXeMay() : 0;
            totalXeDap += item.getXeDap() != null ? item.getXeDap() : 0;

            if (item.getTrangThai() != null && item.getTrangThai()) {
                paid++;
            } else {
                unpaid++;
            }
        }

        stats.put("total", list.size());
        stats.put("totalAmount", totalAmount);
        stats.put("totalOTo", totalOTo);
        stats.put("totalXeMay", totalXeMay);
        stats.put("totalXeDap", totalXeDap);
        stats.put("paid", paid);
        stats.put("unpaid", unpaid);
        stats.put("paidAmount", list.stream()
                .filter(item -> item.getTrangThai() != null && item.getTrangThai())
                .mapToDouble(item -> item.getTongTienGuiXe() != null ? item.getTongTienGuiXe() : 0)
                .sum());

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

        return overview;
    }
}