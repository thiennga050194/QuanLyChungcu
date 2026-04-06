package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.LoaiPhuongTienDTO;
import com.example.quanlychungcu.model.LoaiPhuongTien;
import com.example.quanlychungcu.repository.LoaiPhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoaiPhuongTienService {

    @Autowired
    private LoaiPhuongTienRepository loaiPhuongTienRepository;

    // Chuyển Entity -> DTO
    private LoaiPhuongTienDTO convertToDTO(LoaiPhuongTien loaiPhuongTien) {
        LoaiPhuongTienDTO dto = new LoaiPhuongTienDTO();
        dto.setMaCuDan(loaiPhuongTien.getMaCuDan());
        dto.setLoaiXe(loaiPhuongTien.getLoaiXe());
        dto.setMaDangKy(loaiPhuongTien.getMaDangKy());
        return dto;
    }

    // Chuyển DTO -> Entity
    private LoaiPhuongTien convertToEntity(LoaiPhuongTienDTO dto) {
        LoaiPhuongTien loaiPhuongTien = new LoaiPhuongTien();
        loaiPhuongTien.setMaCuDan(dto.getMaCuDan());
        loaiPhuongTien.setLoaiXe(dto.getLoaiXe());
        loaiPhuongTien.setMaDangKy(dto.getMaDangKy());
        return loaiPhuongTien;
    }

    // Lấy tất cả phương tiện
    public List<LoaiPhuongTienDTO> getAll() {
        return loaiPhuongTienRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<LoaiPhuongTienDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        // Tạo Pageable
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("maDangKy").ascending());
        }

        // Lấy Page<LoaiPhuongTien> từ repository
        Page<LoaiPhuongTien> phuongTienPage;

        if (search != null && !search.trim().isEmpty()) {
            phuongTienPage = loaiPhuongTienRepository.searchByKeyword(search, pageable);
        } else {
            phuongTienPage = loaiPhuongTienRepository.findAll(pageable);
        }

        // Chuyển Page<LoaiPhuongTien> -> Page<LoaiPhuongTienDTO>
        return phuongTienPage.map(this::convertToDTO);
    }

    // Lấy theo ID (maDangKy)
    public Optional<LoaiPhuongTienDTO> getById(String maDangKy) {
        if (maDangKy == null)
            return Optional.empty();
        return loaiPhuongTienRepository.findById(maDangKy)
                .map(this::convertToDTO);
    }

    // Lấy theo mã cư dân
    public List<LoaiPhuongTienDTO> getByMaCuDan(String maCuDan) {
        return loaiPhuongTienRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo loại xe
    public List<LoaiPhuongTienDTO> getByLoaiXe(String loaiXe) {
        return loaiPhuongTienRepository.findByLoaiXe(loaiXe)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới hoặc cập nhật
    public LoaiPhuongTienDTO save(@NonNull LoaiPhuongTienDTO loaiPhuongTienDTO) {
        // Kiểm tra biển số đã tồn tại chưa
        LoaiPhuongTien existing = loaiPhuongTienRepository.findByMaDangKy(loaiPhuongTienDTO.getMaDangKy());
        if (existing != null && !existing.getMaCuDan().equals(loaiPhuongTienDTO.getMaCuDan())) {
            throw new RuntimeException("Biển số đã được đăng ký bởi cư dân khác!");
        }

        LoaiPhuongTien loaiPhuongTien = convertToEntity(loaiPhuongTienDTO);
        if (loaiPhuongTien == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        LoaiPhuongTien saved = loaiPhuongTienRepository.save(loaiPhuongTien);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maDangKy) {
        if (maDangKy != null) {
            loaiPhuongTienRepository.deleteById(maDangKy);
        }
    }

    // Thống kê số lượng theo loại xe
    public Map<String, Long> getStatisticsByLoaiXe() {
        List<Object[]> results = loaiPhuongTienRepository.countByLoaiXeGroup();
        Map<String, Long> statistics = new HashMap<>();

        for (Object[] result : results) {
            String loaiXe = (String) result[0];
            Long count = (Long) result[1];
            statistics.put(loaiXe, count);
        }

        return statistics;
    }

    // Đếm số xe của một cư dân
    public long countByMaCuDan(String maCuDan) {
        return loaiPhuongTienRepository.countByMaCuDan(maCuDan);
    }
}