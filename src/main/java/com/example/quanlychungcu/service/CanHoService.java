package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.CanHoDTO;
import com.example.quanlychungcu.model.CanHo;
import com.example.quanlychungcu.model.CuDanCanHo;
import com.example.quanlychungcu.repository.CanHoRepository;
import com.example.quanlychungcu.repository.CuDanCanHoRepository;
import com.example.quanlychungcu.repository.KhuCanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CanHoService {

    @Autowired
    private CanHoRepository canHoRepository;

    @Autowired
    private KhuCanHoRepository khuCanHoRepository;

    @Autowired
    private CuDanCanHoRepository cuDanCanHoRepository;

    // Chuyển Entity -> DTO
    private CanHoDTO convertToDTO(CanHo canHo) {
        CanHoDTO dto = new CanHoDTO();
        dto.setMaCanHo(canHo.getMaCanHo());
        dto.setDienTich(canHo.getDienTich());
        dto.setSoPhong(canHo.getSoPhong());
        dto.setTrangThai(canHo.isTrangThai());
        dto.setMaKhu(canHo.getMaKhu());

        // Lấy đại diện (chủ hộ hoặc chủ hộ thuê) từ quan hệ nếu có
        if (canHo.getDanhSachQuanHe() != null) {
            canHo.getDanhSachQuanHe().stream()
                    .filter(q -> (q.getVaiTro() == 0 || q.getVaiTro() == 2) && q.getNgayKetThuc() == null)
                    .sorted((q1, q2) -> Integer.compare(q1.getVaiTro(), q2.getVaiTro())) // Ưu tiên Chủ hộ (0) trước Chủ
                                                                                         // hộ thuê (2)
                    .findFirst()
                    .ifPresent(q -> {
                        dto.setMaCuDan(q.getMaCuDan());
                        if (q.getCuDan() != null) {
                            dto.setTenCuDan(q.getCuDan().getTenCuDan());
                        }
                    });
        }

        // Lấy tên khu từ quan hệ
        if (canHo.getKhuCanHo() != null) {
            dto.setTenKhu(canHo.getKhuCanHo().getTenKhu());
        }

        return dto;
    }

    // Chuyển DTO -> Entity
    private CanHo convertToEntity(CanHoDTO dto) {
        CanHo canHo = new CanHo();
        canHo.setMaCanHo(dto.getMaCanHo());
        canHo.setDienTich(dto.getDienTich());
        canHo.setSoPhong(dto.getSoPhong());
        canHo.setTrangThai(dto.isTrangThai());
        canHo.setMaKhu(dto.getMaKhu());

        // Thiết lập quan hệ với KhuCanHo nếu cần
        String maKhu = dto.getMaKhu();
        if (maKhu != null) {
            khuCanHoRepository.findById(maKhu).ifPresent(canHo::setKhuCanHo);
        }

        return canHo;
    }

    // Lấy tất cả căn hộ (trả về DTO)
    public List<CanHoDTO> getAll() {
        return canHoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG - trả về Page<DTO>
    public Page<CanHoDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        // Tạo Pageable
        Pageable pageable;

        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size, Sort.by("maCanHo").ascending());
        }

        // Lấy Page<CanHo> từ repository
        Page<CanHo> canHoPage;

        if (search != null && !search.trim().isEmpty()) {
            canHoPage = canHoRepository.searchByKeyword(search, pageable);
        } else {
            canHoPage = canHoRepository.findAll(pageable);
        }

        // Chuyển Page<CanHo> -> Page<CanHoDTO>
        return canHoPage.map(this::convertToDTO);
    }

    // Lấy theo ID
    public Optional<CanHoDTO> getById(String maCanHo) {
        if (maCanHo == null)
            return Optional.empty();
        return canHoRepository.findById(maCanHo)
                .map(this::convertToDTO);
    }

    // Thêm mới hoặc cập nhật
    public CanHoDTO save(@NonNull CanHoDTO canHoDTO) {
        if (canHoDTO.getMaCanHo() == null) {
            throw new RuntimeException("Mã căn hộ không được để trống!");
        }

        CanHo canHo = convertToEntity(canHoDTO);
        if (canHo == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");

        CanHo saved = canHoRepository.save(canHo);

        // Xử lý cập nhật mã cư dân (nếu có trong DTO)
        String newMaCuDan = canHoDTO.getMaCuDan();
        // Tìm chủ hộ hiện tại (vaiTro = 0) đang sống tại căn hộ
        Optional<CuDanCanHo> currentOwnerOpt = cuDanCanHoRepository.findChuHoByCanHo(saved.getMaCanHo());

        if (newMaCuDan != null && !newMaCuDan.trim().isEmpty()) {
            if (currentOwnerOpt.isPresent()) {
                CuDanCanHo currentOwner = currentOwnerOpt.get();
                // Nếu chủ hộ mới khác chủ hộ đang có
                if (!currentOwner.getMaCuDan().equals(newMaCuDan)) {
                    // Kết thúc quan hệ chủ hộ cũ
                    currentOwner.setNgayKetThuc(java.time.LocalDate.now());
                    cuDanCanHoRepository.save(currentOwner);

                    // Tạo quan hệ chủ hộ mới
                    CuDanCanHo newOwner = new CuDanCanHo(
                            newMaCuDan, saved.getMaCanHo(), 0, java.time.LocalDate.now());
                    cuDanCanHoRepository.save(newOwner);
                }
            } else {
                // Chưa có chủ hộ nào, tạo mới quan hệ chủ hộ
                CuDanCanHo newOwner = new CuDanCanHo(
                        newMaCuDan, saved.getMaCanHo(), 0, java.time.LocalDate.now());
                cuDanCanHoRepository.save(newOwner);
            }
        } else {
            // Nếu gửi lên mã cư dân trống (clear) -> kết thúc quan hệ chủ hộ hiện tại nếu
            // có
            if (currentOwnerOpt.isPresent()) {
                CuDanCanHo currentOwner = currentOwnerOpt.get();
                currentOwner.setNgayKetThuc(java.time.LocalDate.now());
                cuDanCanHoRepository.save(currentOwner);
            }
        }

        CanHoDTO result = convertToDTO(saved);
        if (newMaCuDan != null && !newMaCuDan.trim().isEmpty()) {
            result.setMaCuDan(newMaCuDan);
        }
        return result;
    }

    // Xóa
    public void delete(String maCanHo) {
        if (maCanHo != null) {
            canHoRepository.deleteById(maCanHo);
        }
    }

    // Lấy căn hộ theo khu
    public List<CanHoDTO> getByMaKhu(String maKhu) {
        return canHoRepository.findByMaKhu(maKhu)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Lấy căn hộ trống
    public List<CanHoDTO> getEmptyApartments() {
        return canHoRepository.findEmptyApartments()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}