package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.ThongBaoDTO;
import com.example.quanlychungcu.model.ThongBao;
import com.example.quanlychungcu.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    // Chuyển Entity -> DTO
    private ThongBaoDTO convertToDTO(ThongBao thongBao) {
        ThongBaoDTO dto = new ThongBaoDTO();
        dto.setMaThongBao(thongBao.getMaThongBao());
        dto.setTieuDe(thongBao.getTieuDe());
        dto.setNoiDung(thongBao.getNoiDung());
        dto.setThoiGian(thongBao.getThoiGian());
        return dto;
    }

    // Chuyển DTO -> Entity
    private ThongBao convertToEntity(ThongBaoDTO dto) {
        ThongBao thongBao = new ThongBao();
        thongBao.setMaThongBao(dto.getMaThongBao());
        thongBao.setTieuDe(dto.getTieuDe());
        thongBao.setNoiDung(dto.getNoiDung());
        thongBao.setThoiGian(dto.getThoiGian());
        return thongBao;
    }

    // Lấy tất cả thông báo
    public List<ThongBaoDTO> getAll() {
        return thongBaoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<ThongBaoDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
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

        // Lấy Page<ThongBao> từ repository
        Page<ThongBao> thongBaoPage;

        if (search != null && !search.trim().isEmpty()) {
            thongBaoPage = thongBaoRepository.searchByKeyword(search, pageable);
        } else {
            thongBaoPage = thongBaoRepository.findAll(pageable);
        }

        // Chuyển Page<ThongBao> -> Page<ThongBaoDTO>
        return thongBaoPage.map(this::convertToDTO);
    }

    // Lấy theo ID
    public Optional<ThongBaoDTO> getById(Integer maThongBao) {
        if (maThongBao == null)
            return Optional.empty();
        return thongBaoRepository.findById(maThongBao)
                .map(this::convertToDTO);
    }

    // Lấy thông báo mới nhất
    public List<ThongBaoDTO> getLatest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("thoiGian").descending());
        return thongBaoRepository.findAll(pageable)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo khoảng thời gian
    public List<ThongBaoDTO> getByThoiGianBetween(Timestamp start, Timestamp end) {
        return thongBaoRepository.findByThoiGianBetween(start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy theo năm
    public List<ThongBaoDTO> getByYear(int year) {
        return thongBaoRepository.findByYear(year)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Thêm mới thông báo
    public ThongBaoDTO create(@NonNull ThongBaoDTO thongBaoDTO) {
        // Tự động gán thời gian nếu chưa có
        if (thongBaoDTO.getThoiGian() == null) {
            thongBaoDTO.setThoiGian(Timestamp.valueOf(LocalDateTime.now()));
        }

        ThongBao thongBao = convertToEntity(thongBaoDTO);
        if (thongBao == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        ThongBao saved = thongBaoRepository.save(thongBao);
        return convertToDTO(saved);
    }

    // Cập nhật thông báo
    public ThongBaoDTO update(Integer maThongBao, @NonNull ThongBaoDTO thongBaoDTO) {
        if (maThongBao == null || !thongBaoRepository.existsById(maThongBao)) {
            throw new RuntimeException("Không tìm thấy thông báo!");
        }

        thongBaoDTO.setMaThongBao(maThongBao);
        ThongBao thongBao = convertToEntity(thongBaoDTO);
        if (thongBao == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        ThongBao saved = thongBaoRepository.save(thongBao);
        return convertToDTO(saved);
    }

    // Xóa thông báo
    public void delete(Integer maThongBao) {
        if (maThongBao != null) {
            thongBaoRepository.deleteById(maThongBao);
        }
    }

    // Thống kê thông báo
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số thông báo
        stats.put("total", thongBaoRepository.count());

        // Thông báo tháng hiện tại
        LocalDateTime now = LocalDateTime.now();
        long thisMonth = thongBaoRepository.countByMonth(now.getYear(), now.getMonthValue());
        stats.put("thisMonth", thisMonth);

        // Thông báo tháng trước
        LocalDateTime lastMonth = now.minusMonths(1);
        long lastMonthCount = thongBaoRepository.countByMonth(
                lastMonth.getYear(), lastMonth.getMonthValue());
        stats.put("lastMonth", lastMonthCount);

        // 5 thông báo mới nhất
        stats.put("latest", getLatest(5));

        return stats;
    }
}