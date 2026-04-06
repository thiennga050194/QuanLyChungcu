package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.CuDanCanHoDTO;
import com.example.quanlychungcu.model.CuDanCanHo;
import com.example.quanlychungcu.model.CuDanCanHoId;
import com.example.quanlychungcu.repository.CuDanCanHoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuDanCanHoService {

    @Autowired
    private CuDanCanHoRepository repository;

    private CuDanCanHoDTO convertToDTO(CuDanCanHo entity) {
        CuDanCanHoDTO dto = new CuDanCanHoDTO();
        dto.setMaCuDan(entity.getMaCuDan());
        dto.setMaCanHo(entity.getMaCanHo());
        dto.setVaiTro(entity.getVaiTro());
        dto.setNgayBatDau(entity.getNgayBatDau());
        dto.setNgayKetThuc(entity.getNgayKetThuc());

        if (entity.getCuDan() != null) {
            dto.setTenCuDan(entity.getCuDan().getTenCuDan());
        }
        if (entity.getCanHo() != null) {

            if (entity.getCanHo().getKhuCanHo() != null) {
                dto.setTenKhu(entity.getCanHo().getKhuCanHo().getTenKhu());
            }
            dto.setDienTich(entity.getCanHo().getDienTich());
        }
        return dto;
    }

    private CuDanCanHo convertToEntity(CuDanCanHoDTO dto) {
        CuDanCanHo entity = new CuDanCanHo();
        entity.setMaCuDan(dto.getMaCuDan());
        entity.setMaCanHo(dto.getMaCanHo());
        entity.setVaiTro(dto.getVaiTro());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        return entity;
    }

    public List<CuDanCanHoDTO> getAll() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Page<CuDanCanHoDTO> search(String maCuDan, String maCanHo, Integer vaiTro, String trangThai,
            int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.search(maCuDan, maCanHo, vaiTro, trangThai, pageable).map(this::convertToDTO);
    }

    public CuDanCanHoDTO create(@NonNull CuDanCanHoDTO dto) {
        // Kiểm tra thông tin bắt buộc
        if (dto.getMaCuDan() == null || dto.getMaCanHo() == null || dto.getNgayBatDau() == null) {
            throw new RuntimeException("Thiếu thông tin bắt buộc (maCuDan, maCanHo, ngayBatDau)!");
        }

        // Kiểm tra xem đã tồn tại quan hệ này chưa (đang hiệu lực)
        if (repository.findByMaCuDanAndMaCanHoAndNgayKetThucIsNull(dto.getMaCuDan(), dto.getMaCanHo()).isPresent()) {
            throw new RuntimeException("Cư dân đã có quan hệ với căn hộ này và đang còn hiệu lực!");
        }

        // Nếu là chủ hộ, kiểm tra xem căn hộ đã có chủ hộ chưa
        if (dto.getVaiTro() == 0) {
            if (repository.findChuHoByCanHo(dto.getMaCanHo()).isPresent()) {
                throw new RuntimeException("Căn hộ đã có chủ hộ!");
            }
        }

        CuDanCanHo entity = convertToEntity(dto);
        if (entity == null)
            throw new RuntimeException("Chuyển đổi thực thể thất bại!");
        CuDanCanHo saved = repository.save(entity);
        return convertToDTO(saved);
    }

    public CuDanCanHoDTO update(String maCuDan, String maCanHo,
            java.time.LocalDate ngayBatDau, @NonNull CuDanCanHoDTO dto) {
        if (maCuDan == null || maCanHo == null || ngayBatDau == null) {
            throw new RuntimeException("Thiếu thông tin nhận diện quan hệ!");
        }

        CuDanCanHoId id = new CuDanCanHoId(maCuDan, maCanHo, ngayBatDau);
        CuDanCanHo entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quan hệ cư dân - căn hộ!"));

        // Nếu thay đổi vai trò thành chủ hộ, kiểm tra xem đã có chủ hộ khác chưa
        if (dto.getVaiTro() == 0 && entity.getVaiTro() != 0) {
            if (repository.findChuHoByCanHo(maCanHo).isPresent()) {
                throw new RuntimeException("Căn hộ đã có chủ hộ!");
            }
        }

        entity.setVaiTro(dto.getVaiTro());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());

        return convertToDTO(repository.save(entity));
    }

    public void delete(String maCuDan, String maCanHo, java.time.LocalDate ngayBatDau) {
        if (maCuDan == null || maCanHo == null || ngayBatDau == null)
            return;

        CuDanCanHoId id = new CuDanCanHoId(maCuDan, maCanHo, ngayBatDau);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy quan hệ cư dân - căn hộ!");
        }
        repository.deleteById(id);
    }

    public List<CuDanCanHoDTO> getByMaCuDan(String maCuDan) {
        if (maCuDan == null)
            return java.util.Collections.emptyList();
        return repository.findByMaCuDanOrderByNgayBatDauDesc(maCuDan).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<CuDanCanHoDTO> getByMaCanHo(String maCanHo) {
        if (maCanHo == null)
            return java.util.Collections.emptyList();
        return repository.findByMaCanHoOrderByNgayBatDauDesc(maCanHo).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public CuDanCanHoDTO terminate(String maCuDan, String maCanHo) {
        if (maCuDan == null || maCanHo == null) {
            throw new RuntimeException("Thiếu thông tin cư dân hoặc căn hộ!");
        }

        CuDanCanHo entity = repository.findByMaCuDanAndMaCanHoAndNgayKetThucIsNull(maCuDan, maCanHo)
                .orElseThrow(() -> new RuntimeException(
                        "Không tìm thấy quan hệ đang hiệu lực của cư dân này tại căn hộ này!"));

        entity.setNgayKetThuc(java.time.LocalDate.now());
        return convertToDTO(repository.save(entity));
    }
}
