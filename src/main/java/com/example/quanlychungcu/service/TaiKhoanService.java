package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.TaiKhoanDTO;
import com.example.quanlychungcu.model.TaiKhoan;
import com.example.quanlychungcu.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Chuyển Entity -> DTO (ẩn mật khẩu)
    private TaiKhoanDTO convertToDTO(TaiKhoan taiKhoan) {
        TaiKhoanDTO dto = new TaiKhoanDTO();
        dto.setTenTaiKhoan(taiKhoan.getTenTaiKhoan());
        dto.setUsername(taiKhoan.getTenTaiKhoan()); // Legacy support
        dto.setVaiTro(taiKhoan.getVaiTro());

        // Ánh xạ vaiTro (int) sang ROLE_ (String) cho frontend
        String roleName;
        switch (taiKhoan.getVaiTro()) {
            case 0:
                roleName = "ROLE_ADMIN";
                break;
            case 1:
                roleName = "ROLE_STAFF";
                break;
            case 2:
                roleName = "ROLE_USER";
                break;
            default:
                roleName = "ROLE_USER";
        }
        dto.setRoles(java.util.Collections.singletonList(roleName));

        // Không set mật khẩu khi trả về
        return dto;
    }

    // Chuyển DTO -> Entity (mã hóa mật khẩu)
    private TaiKhoan convertToEntity(TaiKhoanDTO dto) {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenTaiKhoan(dto.getTenTaiKhoan());
        taiKhoan.setVaiTro(dto.getVaiTro());

        // Mã hóa mật khẩu nếu có
        if (dto.getMatKhau() != null && !dto.getMatKhau().trim().isEmpty()) {
            taiKhoan.setMatKhau(passwordEncoder.encode(dto.getMatKhau()));
        }

        return taiKhoan;
    }

    // Lấy tất cả tài khoản
    public List<TaiKhoanDTO> getAll() {
        return taiKhoanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<TaiKhoanDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        // Tạo Pageable
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("tenTaiKhoan").ascending());
        }

        // Lấy Page<TaiKhoan> từ repository
        Page<TaiKhoan> taiKhoanPage;

        if (search != null && !search.trim().isEmpty()) {
            taiKhoanPage = taiKhoanRepository.searchByKeyword(search, pageable);
        } else {
            taiKhoanPage = taiKhoanRepository.findAll(pageable);
        }

        // Chuyển Page<TaiKhoan> -> Page<TaiKhoanDTO>
        return taiKhoanPage.map(this::convertToDTO);
    }

    // Lấy theo username
    public Optional<TaiKhoanDTO> getByUsername(String username) {
        if (username == null)
            return Optional.empty();
        return taiKhoanRepository.findById(username)
                .map(this::convertToDTO);
    }

    // Lấy theo vai trò
    public List<TaiKhoanDTO> getByVaiTro(int vaiTro) {
        return taiKhoanRepository.findByVaiTro(vaiTro)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới tài khoản
    public TaiKhoanDTO create(@NonNull TaiKhoanDTO taiKhoanDTO) {
        // Kiểm tra username đã tồn tại
        if (taiKhoanDTO.getTenTaiKhoan() == null) {
            throw new RuntimeException("Tên tài khoản không được để trống!");
        }

        if (taiKhoanRepository.existsByTenTaiKhoan(taiKhoanDTO.getTenTaiKhoan())) {
            throw new RuntimeException("Tên tài khoản đã tồn tại!");
        }

        // Kiểm tra mật khẩu
        if (taiKhoanDTO.getMatKhau() == null || taiKhoanDTO.getMatKhau().trim().isEmpty()) {
            throw new RuntimeException("Mật khẩu không được để trống!");
        }

        TaiKhoan taiKhoan = convertToEntity(taiKhoanDTO);
        if (taiKhoan == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        TaiKhoan saved = taiKhoanRepository.save(taiKhoan);
        return convertToDTO(saved);
    }

    // Cập nhật tài khoản
    public TaiKhoanDTO update(@NonNull String username, @NonNull TaiKhoanDTO taiKhoanDTO) {
        TaiKhoan existing = taiKhoanRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        // Cập nhật thông tin
        existing.setVaiTro(taiKhoanDTO.getVaiTro());

        // Cập nhật mật khẩu nếu có
        if (taiKhoanDTO.getMatKhau() != null && !taiKhoanDTO.getMatKhau().trim().isEmpty()) {
            existing.setMatKhau(passwordEncoder.encode(taiKhoanDTO.getMatKhau()));
        }

        TaiKhoan saved = taiKhoanRepository.save(existing);
        return convertToDTO(saved);
    }

    // Xóa tài khoản
    public void delete(String username) {
        if (username == null)
            return;

        // Không cho xóa tài khoản Admin cuối cùng
        long adminCount = taiKhoanRepository.countByVaiTro(0);
        TaiKhoan taiKhoan = taiKhoanRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        if (taiKhoan.getVaiTro() == 0 && adminCount <= 1) {
            throw new RuntimeException("Không thể xóa tài khoản Admin cuối cùng!");
        }

        taiKhoanRepository.deleteById(username);
    }

    // Thống kê tài khoản
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", taiKhoanRepository.count());
        stats.put("adminCount", taiKhoanRepository.countByVaiTro(0));
        stats.put("staffCount", taiKhoanRepository.countByVaiTro(1));
        return stats;
    }

    // Lấy thông tin user hiện tại
    public TaiKhoanDTO getCurrentUser(String username) {
        if (username == null)
            return null;
        return taiKhoanRepository.findById(username)
                .map(this::convertToDTO)
                .orElse(null);
    }
}