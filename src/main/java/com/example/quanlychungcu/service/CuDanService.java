package com.example.quanlychungcu.service;

import com.example.quanlychungcu.dto.CuDanDTO;
import com.example.quanlychungcu.dto.CuDanCanHoDTO;
import com.example.quanlychungcu.model.CuDan;
import com.example.quanlychungcu.model.CuDanCanHo;
import com.example.quanlychungcu.repository.CuDanRepository;
import com.example.quanlychungcu.repository.CuDanCanHoRepository;
import com.example.quanlychungcu.repository.HopDongRepository;
import com.example.quanlychungcu.repository.TienDienRepository;
import com.example.quanlychungcu.repository.TienNuocRepository;
import com.example.quanlychungcu.repository.TienGuiXeRepository;
import com.example.quanlychungcu.repository.TienVeSinhChungRepository;
import com.example.quanlychungcu.repository.TienPhiDuyTriRepository;
import com.example.quanlychungcu.repository.TienChiPhiHoatDongRepository;
import com.example.quanlychungcu.repository.LoaiPhuongTienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CuDanService {

    @Autowired
    private CuDanRepository cuDanRepo;

    @Autowired
    private CuDanCanHoRepository cuDanCanHoRepo;

    @Autowired
    private HopDongRepository hopDongRepo;

    @Autowired
    private TienDienRepository tienDienRepo;

    @Autowired
    private TienNuocRepository tienNuocRepo;

    @Autowired
    private TienGuiXeRepository tienGuiXeRepo;

    @Autowired
    private TienVeSinhChungRepository tienVeSinhChungRepo;

    @Autowired
    private TienPhiDuyTriRepository tienPhiDuyTriRepo;

    @Autowired
    private TienChiPhiHoatDongRepository tienChiPhiHoatDongRepo;

    @Autowired
    private LoaiPhuongTienRepository loaiPhuongTienRepo;

    // Chuyển Entity -> DTO
    private CuDanDTO convertToDTO(CuDan cuDan) {
        CuDanDTO dto = new CuDanDTO();
        dto.setMaCuDan(cuDan.getMaCuDan());
        dto.setTenCuDan(cuDan.getTenCuDan());
        dto.setNgaySinh(cuDan.getNgaySinh());
        dto.setGioiTinh(cuDan.isGioiTinh());
        dto.setSoDT(cuDan.getSoDT());
        dto.setSoCMT(cuDan.getSoCMT());
        dto.setQueQuan(cuDan.getQueQuan());

        // Chuyển danh sách quan hệ sang DTO
        if (cuDan.getDanhSachQuanHe() != null) {
            List<CuDanCanHoDTO> qhDTOs = cuDan.getDanhSachQuanHe().stream()
                    .map(qh -> {
                        CuDanCanHoDTO qhDto = new CuDanCanHoDTO();
                        qhDto.setMaCuDan(qh.getMaCuDan());
                        qhDto.setMaCanHo(qh.getMaCanHo());
                        qhDto.setVaiTro(qh.getVaiTro());
                        qhDto.setNgayBatDau(qh.getNgayBatDau());
                        qhDto.setNgayKetThuc(qh.getNgayKetThuc());
                        if (qh.getCanHo() != null) {

                            if (qh.getCanHo().getKhuCanHo() != null) {
                                qhDto.setTenKhu(qh.getCanHo().getKhuCanHo().getTenKhu());
                            }
                        }
                        return qhDto;
                    })
                    .collect(Collectors.toList());
            dto.setDanhSachQuanHe(qhDTOs);

            // Tìm quan hệ đang hiệu lực để set field tương thích cũ
            cuDan.getDanhSachQuanHe().stream()
                    .filter(qh -> qh.getNgayKetThuc() == null)
                    .findFirst()
                    .ifPresent(qh -> {
                        dto.setMaCanHo(qh.getMaCanHo());
                        if (qh.getCanHo() != null && qh.getCanHo().getKhuCanHo() != null) {
                            dto.setMaKhu(qh.getCanHo().getKhuCanHo().getMaKhu());
                        }
                        dto.setVaiTro(qh.getVaiTro());
                        dto.setNgayBatDau(qh.getNgayBatDau());
                        dto.setNgayKetThuc(qh.getNgayKetThuc());
                    });
        }

        return dto;
    }

    // Lấy tất cả (trả về DTO)
    public List<CuDanDTO> getAll() {
        return cuDanRepo.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // PHÂN TRANG - trả về Page<DTO>
    public Page<CuDanDTO> getPaged(int page, int size, String search, String sortBy, String sortDirection) {
        Pageable pageable;
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            Sort sort = sortDirection.equalsIgnoreCase("DESC")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<CuDan> cuDanPage;
        if (search != null && !search.trim().isEmpty()) {
            cuDanPage = cuDanRepo.findByTenCuDanContainingIgnoreCaseOrMaCuDanContainingIgnoreCase(
                    search, search, pageable);
        } else {
            cuDanPage = cuDanRepo.findAll(pageable);
        }

        return cuDanPage.map(this::convertToDTO);
    }

    // Lấy theo ID (trả về Optional<DTO>)
    public Optional<CuDanDTO> getById(String maCuDan) {
        if (maCuDan == null)
            return Optional.empty();
        return cuDanRepo.findById(maCuDan)
                .map(this::convertToDTO);
    }

    // Thêm mới hoặc cập nhật
    @Transactional
    public CuDanDTO save(@NonNull CuDanDTO cuDanDTO) {
        String maCuDan = cuDanDTO.getMaCuDan();
        if (maCuDan == null) {
            throw new RuntimeException("Mã cư dân không được để trống!");
        }

        CuDan cuDan = cuDanRepo.findById(maCuDan).orElse(new CuDan());

        cuDan.setMaCuDan(maCuDan);
        cuDan.setTenCuDan(cuDanDTO.getTenCuDan());
        cuDan.setNgaySinh(cuDanDTO.getNgaySinh());
        cuDan.setGioiTinh(cuDanDTO.isGioiTinh());
        cuDan.setSoDT(cuDanDTO.getSoDT());
        cuDan.setSoCMT(cuDanDTO.getSoCMT());
        cuDan.setQueQuan(cuDanDTO.getQueQuan());

        CuDan saved = cuDanRepo.save(cuDan);

        // 2. Xử lý Quan hệ Căn hộ (nếu có maCanHo)
        String maCanHo = cuDanDTO.getMaCanHo();
        if (maCanHo != null && !maCanHo.trim().isEmpty()) {
            int vaiTroMoi = cuDanDTO.getVaiTro();

            // Tìm quan hệ hiện tại
            List<CuDanCanHo> hienTai = cuDanCanHoRepo.findByMaCuDanAndNgayKetThucIsNull(maCuDan);

            // Kiểm tra xem đã có quan hệ với căn hộ này chưa
            Optional<CuDanCanHo> qhGiong = hienTai.stream()
                    .filter(qh -> qh.getMaCanHo().equalsIgnoreCase(maCanHo))
                    .findFirst();

            if (qhGiong.isPresent()) {
                // Nếu đã ở căn này rồi, cập nhật vai trò và ngày tháng nếu có
                CuDanCanHo qh = qhGiong.get();
                boolean changed = false;
                if (qh.getVaiTro() != vaiTroMoi) {
                    qh.setVaiTro(vaiTroMoi);
                    changed = true;
                }
                if (cuDanDTO.getNgayBatDau() != null && !cuDanDTO.getNgayBatDau().equals(qh.getNgayBatDau())) {
                    qh.setNgayBatDau(cuDanDTO.getNgayBatDau());
                    changed = true;
                }
                if (cuDanDTO.getNgayKetThuc() != null && !cuDanDTO.getNgayKetThuc().equals(qh.getNgayKetThuc())) {
                    qh.setNgayKetThuc(cuDanDTO.getNgayKetThuc());
                    changed = true;
                } else if (cuDanDTO.getNgayKetThuc() == null && qh.getNgayKetThuc() != null) {
                    qh.setNgayKetThuc(null);
                    changed = true;
                }

                if (changed) {
                    cuDanCanHoRepo.save(qh);
                }
            } else {
                // Nếu sang căn mới hoặc chưa có căn nào:
                // Không kết thúc các quan hệ cũ nữa để cho phép một người ở nhiều căn

                // 2. Tạo quan hệ mới
                LocalDate start = cuDanDTO.getNgayBatDau() != null ? cuDanDTO.getNgayBatDau() : LocalDate.now();
                CuDanCanHo qhMoi = new CuDanCanHo(maCuDan, maCanHo, vaiTroMoi, start);
                if (cuDanDTO.getNgayKetThuc() != null) {
                    qhMoi.setNgayKetThuc(cuDanDTO.getNgayKetThuc());
                }
                cuDanCanHoRepo.save(qhMoi);
            }
        }

        return convertToDTO(saved);
    }

    public boolean existsById(String maCuDan) {
        if (maCuDan == null)
            return false;
        return cuDanRepo.existsById(maCuDan);
    }

    // Lấy theo vai trò
    public List<CuDanDTO> getByVaiTro(int vaiTro) {
        // Lấy theo vai trò (0: Chủ hộ, 1: Thành viên, 2: Chủ hộ thuê, 3: Thành viên
        // thuê)
        return cuDanRepo.findByVaiTroHienTai(vaiTro, Pageable.unpaged())
                .getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Xóa cư dân và toàn bộ dữ liệu liên quan
    @Transactional
    public void delete(String maCuDan) {
        if (maCuDan == null)
            return;

        // 1. Xóa tất cả bản ghi tiền điện theo mã cư dân
        tienDienRepo.deleteByMaCuDan(maCuDan);

        // 2. Xóa tiền nước
        tienNuocRepo.deleteByMaCuDan(maCuDan);

        // 3. Xóa tiền gửi xe
        tienGuiXeRepo.deleteByMaCuDan(maCuDan);

        // 4. Xóa tiền vệ sinh chung
        tienVeSinhChungRepo.deleteByMaCuDan(maCuDan);

        // 5. Xóa tiền phí duy trì
        tienPhiDuyTriRepo.deleteByMaCuDan(maCuDan);

        // 6. Xóa chi phí hoạt động
        tienChiPhiHoatDongRepo.deleteByMaCuDan(maCuDan);

        // 7. Xóa phương tiện đăng ký (đơn key - dùng deleteAll bình thường)
        loaiPhuongTienRepo.deleteAll(loaiPhuongTienRepo.findByMaCuDan(maCuDan));

        // 8. Xóa hợp đồng (đơn key - dùng deleteAll bình thường)
        hopDongRepo.deleteAll(hopDongRepo.findByMaCuDan(maCuDan));

        // 9. Xóa quan hệ cư dân - căn hộ
        cuDanCanHoRepo.deleteByMaCuDan(maCuDan);

        // 10. Cuối cùng xóa cư dân
        cuDanRepo.deleteById(maCuDan);
    }
}
