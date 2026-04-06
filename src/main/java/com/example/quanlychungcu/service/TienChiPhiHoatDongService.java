package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TienChiPhiHoatDongDTO;
import com.example.quanlychungcu.model.FeeId;
import com.example.quanlychungcu.model.TienChiPhiHoatDong;
import com.example.quanlychungcu.repository.TienChiPhiHoatDongRepository;
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
public class TienChiPhiHoatDongService {

    @Autowired
    private TienChiPhiHoatDongRepository tienChiPhiHoatDongRepository;

    // Chuyển Entity -> DTO
    private TienChiPhiHoatDongDTO convertToDTO(TienChiPhiHoatDong entity) {
        TienChiPhiHoatDongDTO dto = new TienChiPhiHoatDongDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setNgayThu(entity.getNgayThu());
        dto.setSoNguoi(entity.getSoNguoi() != null ? entity.getSoNguoi() : 0);
        dto.setTienChiPhiHoatDong(entity.getTienChiPhiHoatDong() != null ? entity.getTienChiPhiHoatDong() : 0.0f);
        dto.setTrangThai(entity.isTrangThai() != null ? entity.isTrangThai() : false);
        return dto;
    }

    // Chuyển DTO -> Entity
    private TienChiPhiHoatDong convertToEntity(TienChiPhiHoatDongDTO dto) {
        TienChiPhiHoatDong entity = new TienChiPhiHoatDong();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setNgayThu(dto.getNgayThu());
        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienChiPhiHoatDong(dto.getTienChiPhiHoatDong());
        entity.setTrangThai(dto.isTrangThai());
        return entity;
    }

    // Lấy tất cả
    public List<TienChiPhiHoatDongDTO> getAll() {
        return tienChiPhiHoatDongRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TienChiPhiHoatDongDTO> getPaged(int page, int size, String search, String sortBy,
            String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayThu").descending());
        }

        Page<TienChiPhiHoatDong> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = tienChiPhiHoatDongRepository.searchByKeyword(search, pageable);
        } else {
            entityPage = tienChiPhiHoatDongRepository.findAll(pageable);
        }

        return entityPage.map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<TienChiPhiHoatDongDTO> getByMaCuDan(String maCuDan) {
        return tienChiPhiHoatDongRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ngày
    public List<TienChiPhiHoatDongDTO> getByNgayThu(LocalDate ngayThu) {
        return tienChiPhiHoatDongRepository.findByNgayThu(ngayThu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo tháng
    public List<TienChiPhiHoatDongDTO> getByMonth(int year, int month) {
        return tienChiPhiHoatDongRepository.findByMonth(year, month)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo trạng thái
    public List<TienChiPhiHoatDongDTO> getByTrangThai(boolean trangThai) {
        return tienChiPhiHoatDongRepository.findByTrangThai(trangThai)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới
    public TienChiPhiHoatDongDTO create(@NonNull TienChiPhiHoatDongDTO dto) {
        // Kiểm tra trùng lặp
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayThu() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayThu)!");
        }

        FeeId id = new FeeId(dto.getMaCuDan(), dto.getMaCanHo(), dto.getNgayThu());
        if (tienChiPhiHoatDongRepository.existsById(id)) {
            throw new RuntimeException("Đã có bản ghi cho cư dân này tại căn hộ này trong tháng!");
        }

        TienChiPhiHoatDong entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TienChiPhiHoatDong saved = tienChiPhiHoatDongRepository.save(entity);
        return convertToDTO(saved);
    }

    // Cập nhật
    public TienChiPhiHoatDongDTO update(@NonNull String maCuDan, @NonNull String maCanHo, @NonNull LocalDate ngayThu,
            @NonNull TienChiPhiHoatDongDTO dto) {
        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        TienChiPhiHoatDong entity = tienChiPhiHoatDongRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi!"));

        entity.setSoNguoi(dto.getSoNguoi());
        entity.setTienChiPhiHoatDong(dto.getTienChiPhiHoatDong());
        entity.setTrangThai(dto.isTrangThai());

        TienChiPhiHoatDong saved = tienChiPhiHoatDongRepository.save(entity);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maCuDan, String maCanHo, LocalDate ngayThu) {
        if (maCuDan == null || maCanHo == null || ngayThu == null)
            return;

        FeeId id = new FeeId(maCuDan, maCanHo, ngayThu);
        if (!tienChiPhiHoatDongRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy bản ghi!");
        }
        tienChiPhiHoatDongRepository.deleteById(id);
    }

    // Thống kê theo tháng
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        Map<String, Object> stats = new HashMap<>();

        List<TienChiPhiHoatDong> list = tienChiPhiHoatDongRepository.findByMonth(year, month);

        float total = 0;
        long paid = 0;
        long unpaid = 0;

        for (TienChiPhiHoatDong item : list) {
            total += item.getTienChiPhiHoatDong();
            if (item.isTrangThai()) {
                paid++;
            } else {
                unpaid++;
            }
        }

        stats.put("total", list.size());
        stats.put("totalAmount", total);
        stats.put("paid", paid);
        stats.put("unpaid", unpaid);
        stats.put("paidAmount", list.stream()
                .filter(TienChiPhiHoatDong::isTrangThai)
                .mapToDouble(TienChiPhiHoatDong::getTienChiPhiHoatDong)
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