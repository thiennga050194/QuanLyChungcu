package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.HopDongDTO;
import com.example.quanlychungcu.model.HopDong;
import com.example.quanlychungcu.repository.HopDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HopDongService {

    @Autowired
    private HopDongRepository hopDongRepository;

    // Chuyển Entity -> DTO
    private HopDongDTO convertToDTO(HopDong hopDong) {
        HopDongDTO dto = new HopDongDTO();
        dto.setMaHopDong(hopDong.getMaHopDong());
        dto.setTenKH(hopDong.getTenKH());
        dto.setMaCanHo(hopDong.getMaCanHo());
        dto.setMaCuDan(hopDong.getMaCuDan());
        dto.setDiaChiKhachHang(hopDong.getDiaChiKhachHang());
        dto.setNgayGiaoDich(hopDong.getNgayGiaoDich());
        return dto;
    }

    // Chuyển DTO -> Entity
    private HopDong convertToEntity(HopDongDTO dto) {
        HopDong hopDong = new HopDong();
        hopDong.setMaHopDong(dto.getMaHopDong());
        hopDong.setTenKH(dto.getTenKH());
        hopDong.setMaCanHo(dto.getMaCanHo());
        hopDong.setMaCuDan(dto.getMaCuDan());
        hopDong.setDiaChiKhachHang(dto.getDiaChiKhachHang());
        hopDong.setNgayGiaoDich(dto.getNgayGiaoDich());
        return hopDong;
    }

    // Lấy tất cả hợp đồng
    public List<HopDongDTO> getAll() {
        return hopDongRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<HopDongDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        // Tạo Pageable
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("ngayGiaoDich").descending());
        }

        // Lấy Page<HopDong> từ repository
        Page<HopDong> hopDongPage;

        if (search != null && !search.trim().isEmpty()) {
            hopDongPage = hopDongRepository.searchByKeyword(search, pageable);
        } else {
            hopDongPage = hopDongRepository.findAll(pageable);
        }

        // Chuyển Page<HopDong> -> Page<HopDongDTO>
        return hopDongPage.map(this::convertToDTO);
    }

    // Lấy theo ID
    public Optional<HopDongDTO> getById(String maHopDong) {
        if (maHopDong == null)
            return Optional.empty();
        return hopDongRepository.findById(maHopDong)
                .map(this::convertToDTO);
    }

    // Thêm mới hoặc cập nhật
    public HopDongDTO save(@NonNull HopDongDTO hopDongDTO) {
        HopDong hopDong = convertToEntity(hopDongDTO);
        if (hopDong == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        HopDong saved = hopDongRepository.save(hopDong);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(String maHopDong) {
        if (maHopDong != null) {
            hopDongRepository.deleteById(maHopDong);
        }
    }

    // Lấy hợp đồng theo căn hộ
    public List<HopDongDTO> getByMaCanHo(String maCanHo) {
        return hopDongRepository.findByMaCanHo(maCanHo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy hợp đồng theo cư dân
    public List<HopDongDTO> getByMaCuDan(String maCuDan) {
        return hopDongRepository.findByMaCuDan(maCuDan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy hợp đồng theo ngày
    public List<HopDongDTO> getByNgayGiaoDich(String ngayGiaoDich) {
        return hopDongRepository.findByNgayGiaoDichContaining(ngayGiaoDich)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}