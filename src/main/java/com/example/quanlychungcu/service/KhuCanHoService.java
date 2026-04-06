package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.CanHoDTO;
import com.example.quanlychungcu.dto.KhuCanHoDTO;
import com.example.quanlychungcu.model.KhuCanHo;
import com.example.quanlychungcu.repository.KhuCanHoRepository;
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
public class KhuCanHoService {

    @Autowired
    private KhuCanHoRepository khuCanHoRepository;

    @Autowired
    private CanHoService canHoService;

    public boolean existsById(String maKhu) {
        if (maKhu == null)
            return false;
        return khuCanHoRepository.existsById(maKhu);
    }

    // Chuyển Entity -> DTO
    private KhuCanHoDTO convertToDTO(KhuCanHo khuCanHo) {
        KhuCanHoDTO dto = new KhuCanHoDTO();
        dto.setMaKhu(khuCanHo.getMaKhu());
        dto.setTenKhu(khuCanHo.getTenKhu());
        dto.setSoTang(khuCanHo.getSoTang());
        dto.setSoCanTT(khuCanHo.getSoCanTT());
        dto.setDiaChi(khuCanHo.getDiaChi());
        return dto;
    }

    // Chuyển DTO -> Entity
    private KhuCanHo convertToEntity(KhuCanHoDTO dto) {
        KhuCanHo khuCanHo = new KhuCanHo();
        khuCanHo.setMaKhu(dto.getMaKhu());
        khuCanHo.setTenKhu(dto.getTenKhu());
        khuCanHo.setSoTang(dto.getSoTang());
        khuCanHo.setSoCanTT(dto.getSoCanTT());
        khuCanHo.setDiaChi(dto.getDiaChi());
        return khuCanHo;
    }

    // Lấy tất cả khu căn hộ
    public List<KhuCanHoDTO> getAll() {
        return khuCanHoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG
    public Page<KhuCanHoDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("maKhu").ascending());
        }

        Page<KhuCanHo> khuPage;

        if (search != null && !search.trim().isEmpty()) {
            khuPage = khuCanHoRepository.searchByKeyword(search, pageable);
        } else {
            khuPage = khuCanHoRepository.findAll(pageable);
        }

        return khuPage.map(this::convertToDTO);
    }

    // Lấy theo ID
    public Optional<KhuCanHoDTO> getById(String maKhu) {
        if (maKhu == null)
            return Optional.empty();
        return khuCanHoRepository.findById(maKhu)
                .map(this::convertToDTO);
    }

    // Lấy theo tên khu
    public KhuCanHoDTO getByTenKhu(String tenKhu) {
        if (tenKhu == null)
            return null;
        KhuCanHo khuCanHo = khuCanHoRepository.findByTenKhu(tenKhu);
        return khuCanHo != null ? convertToDTO(khuCanHo) : null;
    }

    // === CẬP NHẬT PHƯƠNG THỨC SAVE VỚI MÃ DẠNG AA, AB, ... ZZ ===
    public KhuCanHoDTO save(@NonNull KhuCanHoDTO khuCanHoDTO) {
        // Tự động sinh mã khu dạng AA, AB,... nếu chưa có
        if (khuCanHoDTO.getMaKhu() == null || khuCanHoDTO.getMaKhu().trim().isEmpty()) {
            String nextMa = generateNextMaKhu();
            khuCanHoDTO.setMaKhu(nextMa);
        }

        KhuCanHo khuCanHo = convertToEntity(khuCanHoDTO);
        if (khuCanHo == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        KhuCanHo saved = khuCanHoRepository.save(khuCanHo);
        return convertToDTO(saved);
    }

    /**
     * Sinh mã khu tiếp theo dạng AA, AB, AC,... ZZ
     * Dựa trên mã khu lớn nhất hiện tại trong database
     */
    private String generateNextMaKhu() {
        List<KhuCanHo> allKhu = khuCanHoRepository.findAll();

        if (allKhu.isEmpty()) {
            return "AA";
        }

        String maxMa = allKhu.stream()
                .map(KhuCanHo::getMaKhu)
                .max(String::compareTo)
                .orElse("AA");

        return incrementMaKhu(maxMa);
    }

    /**
     * Tăng mã khu lên 1 đơn vị theo thứ tự:
     * AA -> AB -> AC -> ... -> AZ -> BA -> BB -> ... -> ZZ
     */
    private String incrementMaKhu(String currentMa) {
        if (currentMa == null || currentMa.length() != 2) {
            return "AA";
        }

        char firstChar = currentMa.charAt(0);
        char secondChar = currentMa.charAt(1);

        // Tăng ký tự thứ hai
        if (secondChar < 'Z') {
            secondChar++;
        } else {
            // Nếu ký tự thứ hai là Z, tăng ký tự đầu và reset ký tự hai về A
            if (firstChar < 'Z') {
                firstChar++;
                secondChar = 'A';
            } else {
                // Nếu đã đến ZZ thì không thể tăng thêm
                throw new RuntimeException(
                        "Đã đạt đến giới hạn mã khu (ZZ). Vui lòng xóa bớt khu hoặc sử dụng mã khác!");
            }
        }

        return "" + firstChar + secondChar;
    }

    /**
     * PHƯƠNG THỨC DỰ PHÒNG: Sinh mã bằng cách tìm mã trống đầu tiên
     * (Sử dụng nếu muốn đảm bảo không có khoảng trống)
     */

    // Xóa
    public void delete(String maKhu) {
        if (maKhu != null) {
            khuCanHoRepository.deleteById(maKhu);
        }
    }

    // Lấy top khu có nhiều căn nhất
    public List<KhuCanHoDTO> getTopKhuBySoCan(int limit) {
        return khuCanHoRepository.findTopBySoCanTT(PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy tổng số căn hộ
    public Long getTotalApartments() {
        return khuCanHoRepository.getTotalApartments();
    }

    // Thêm khu phức hợp (tạo cả căn hộ tự động)
    public KhuCanHoDTO addComplex(@NonNull KhuCanHoDTO khuCanHoDTO, float dienTich, int soPhong) {
        // 1. Lưu Khu trước để có maKhu
        KhuCanHoDTO savedKhu = save(khuCanHoDTO);
        if (savedKhu == null)
            throw new RuntimeException("Lưu khu thất bại!");
        int soTang = savedKhu.getSoTang();
        int soCanTT = savedKhu.getSoCanTT();

        for (int t = 1; t <= soTang; t++) {
            for (int c = 1; c <= soCanTT; c++) {
                CanHoDTO canHoDTO = new CanHoDTO();

                // Format mã căn hộ: KHU_TANG_CAN (VD: AA0101)
                String maCanHo = String.format("%s%02d%02d", savedKhu.getMaKhu(), t, c);

                canHoDTO.setMaCanHo(maCanHo);
                canHoDTO.setDienTich(dienTich);
                canHoDTO.setSoPhong(soPhong);
                canHoDTO.setTrangThai(false);
                canHoDTO.setMaKhu(savedKhu.getMaKhu());

                canHoService.save(canHoDTO);
            }
        }

        return savedKhu;
    }
}