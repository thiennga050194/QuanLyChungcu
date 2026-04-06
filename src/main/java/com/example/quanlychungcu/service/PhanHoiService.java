package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.PhanHoiDTO;
import com.example.quanlychungcu.model.PhanHoi;
import com.example.quanlychungcu.repository.PhanHoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhanHoiService {

    @Autowired
    private PhanHoiRepository phanHoiRepository;

    // Chuyển Entity -> DTO
    private PhanHoiDTO convertToDTO(PhanHoi phanHoi) {
        PhanHoiDTO dto = new PhanHoiDTO();
        dto.setMaPhanHoi(phanHoi.getMaPhanHoi());
        dto.setTaiKhoan(phanHoi.getTaiKhoan());
        dto.setPhanHoi(phanHoi.getPhanHoi());
        dto.setImage(phanHoi.getImage());
        dto.setThoiGian(phanHoi.getThoiGian());
        dto.setBanquanly(phanHoi.getBanquanly());
        return dto;
    }

    // Chuyển DTO -> Entity
    private PhanHoi convertToEntity(PhanHoiDTO dto) {
        PhanHoi phanHoi = new PhanHoi();
        phanHoi.setMaPhanHoi(dto.getMaPhanHoi());
        phanHoi.setTaiKhoan(dto.getTaiKhoan());
        phanHoi.setPhanHoi(dto.getPhanHoi());
        phanHoi.setImage(dto.getImage());
        phanHoi.setThoiGian(dto.getThoiGian());
        phanHoi.setBanquanly(dto.getBanquanly());
        return phanHoi;
    }

    // Lấy tất cả phản hồi
    public List<PhanHoiDTO> getAll() {
        return phanHoiRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<PhanHoiDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        // Tạo Pageable
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            // Mặc định sắp xếp theo thời gian mới nhất
            pageable = PageRequest.of(page, size, Sort.by("thoiGian").descending());
        }

        // Lấy Page<PhanHoi> từ repository
        Page<PhanHoi> phanHoiPage;

        if (search != null && !search.trim().isEmpty()) {
            phanHoiPage = phanHoiRepository.searchByKeyword(search, pageable);
        } else {
            phanHoiPage = phanHoiRepository.findAll(pageable);
        }

        // Chuyển Page<PhanHoi> -> Page<PhanHoiDTO>
        return phanHoiPage.map(this::convertToDTO);
    }

    // Lấy theo ID
    public Optional<PhanHoiDTO> getById(Integer maPhanHoi) {
        if (maPhanHoi == null)
            return Optional.empty();
        return phanHoiRepository.findById(maPhanHoi)
                .map(this::convertToDTO);
    }

    // Lấy theo tài khoản
    public List<PhanHoiDTO> getByTaiKhoan(String taiKhoan) {
        return phanHoiRepository.findByTaiKhoan(taiKhoan)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo ban quản lý
    public List<PhanHoiDTO> getByBanquanly(int banquanly) {
        return phanHoiRepository.findByBanquanly(banquanly)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy phản hồi có hình ảnh
    public List<PhanHoiDTO> getCoHinhAnh() {
        return phanHoiRepository.findByCoHinhAnh()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy phản hồi trong khoảng thời gian
    public List<PhanHoiDTO> getByThoiGianBetween(Timestamp start, Timestamp end) {
        return phanHoiRepository.findByThoiGianBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới phản hồi
    public PhanHoiDTO save(@NonNull PhanHoiDTO phanHoiDTO) {
        // Nếu là thêm mới, tự động gán thời gian
        if (phanHoiDTO.getMaPhanHoi() == 0) {
            phanHoiDTO.setThoiGian(Timestamp.valueOf(LocalDateTime.now()));
        }

        PhanHoi phanHoi = convertToEntity(phanHoiDTO);
        if (phanHoi == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        PhanHoi saved = phanHoiRepository.save(phanHoi);
        return convertToDTO(saved);
    }

    // Xóa
    public void delete(Integer maPhanHoi) {
        if (maPhanHoi != null) {
            phanHoiRepository.deleteById(maPhanHoi);
        }
    }

    // Đếm số phản hồi của tài khoản
    public long countByTaiKhoan(String taiKhoan) {
        return phanHoiRepository.countByTaiKhoan(taiKhoan);
    }

    // Lấy phản hồi mới nhất
    public List<PhanHoiDTO> getLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("thoiGian").descending());
        return phanHoiRepository.findAll(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}