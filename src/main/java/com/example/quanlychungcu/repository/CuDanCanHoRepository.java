package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.CuDanCanHo;
import com.example.quanlychungcu.model.CuDanCanHoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuDanCanHoRepository extends JpaRepository<CuDanCanHo, CuDanCanHoId> {

        // ===== CÁC METHOD CŨ (GIỮ NGUYÊN) =====
        List<CuDanCanHo> findByMaCuDanAndNgayKetThucIsNull(String maCuDan);

        List<CuDanCanHo> findByMaCanHoAndNgayKetThucIsNull(String maCanHo);

        Optional<CuDanCanHo> findByMaCuDanAndMaCanHoAndNgayKetThucIsNull(String maCuDan, String maCanHo);

        List<CuDanCanHo> findByMaCuDanOrderByNgayBatDauDesc(String maCuDan);

        List<CuDanCanHo> findByMaCanHoOrderByNgayBatDauDesc(String maCanHo);

        boolean existsByMaCuDanAndNgayKetThucIsNull(String maCuDan);

        long countByMaCuDanAndNgayKetThucIsNull(String maCuDan);

        long countByMaCanHoAndNgayKetThucIsNull(String maCanHo);

        @Query("SELECT c FROM CuDanCanHo c WHERE c.maCanHo = :maCanHo AND c.vaiTro = 0 AND c.ngayKetThuc IS NULL")
        Optional<CuDanCanHo> findChuHoByCanHo(@Param("maCanHo") String maCanHo);

        // ===== SỬA LỖI: ĐẶT TÊN KHÁC CHO 2 METHOD =====

        // Tìm thành viên (vaiTro = 1)
        @Query("SELECT c FROM CuDanCanHo c WHERE c.maCanHo = :maCanHo AND c.vaiTro = 1 AND c.ngayKetThuc IS NULL")
        List<CuDanCanHo> findThanhVienByCanHo(@Param("maCanHo") String maCanHo);

        // Tìm người thuê (vaiTro = 2)
        @Query("SELECT c FROM CuDanCanHo c WHERE c.maCanHo = :maCanHo AND c.vaiTro = 2 AND c.ngayKetThuc IS NULL")
        List<CuDanCanHo> findNguoiThueByCanHo(@Param("maCanHo") String maCanHo);

        // Hoặc dùng 1 method với tham số vaiTro
        List<CuDanCanHo> findByMaCanHoAndVaiTroAndNgayKetThucIsNull(String maCanHo, int vaiTro);

        @Query("SELECT c FROM CuDanCanHo c WHERE c.maCanHo = :maCanHo AND " +
                        "((c.ngayBatDau BETWEEN :startDate AND :endDate) OR " +
                        "(c.ngayKetThuc BETWEEN :startDate AND :endDate))")
        List<CuDanCanHo> findByCanHoAndDateRange(
                        @Param("maCanHo") String maCanHo,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        // ===== THÊM PHÂN TRANG =====

        @Override
        @NonNull
        Page<CuDanCanHo> findAll(@NonNull Pageable pageable);

        Page<CuDanCanHo> findByMaCuDanAndNgayKetThucIsNull(String maCuDan, Pageable pageable);

        Page<CuDanCanHo> findByMaCanHoAndNgayKetThucIsNull(String maCanHo, Pageable pageable);

        Page<CuDanCanHo> findByMaCuDanOrderByNgayBatDauDesc(String maCuDan, Pageable pageable);

        Page<CuDanCanHo> findByMaCanHoOrderByNgayBatDauDesc(String maCanHo, Pageable pageable);

        Page<CuDanCanHo> findByMaCanHoAndVaiTroAndNgayKetThucIsNull(String maCanHo, int vaiTro, Pageable pageable);

        @Query("SELECT c FROM CuDanCanHo c WHERE " +
                        "(:maCuDan IS NULL OR c.maCuDan LIKE %:maCuDan%) AND " +
                        "(:maCanHo IS NULL OR c.maCanHo LIKE %:maCanHo%) AND " +
                        "(:vaiTro IS NULL OR c.vaiTro = :vaiTro) AND " +
                        "(:trangThai = 'all' OR " +
                        "   (:trangThai = 'active' AND c.ngayKetThuc IS NULL) OR " +
                        "   (:trangThai = 'inactive' AND c.ngayKetThuc IS NOT NULL))")
        Page<CuDanCanHo> search(
                        @Param("maCuDan") String maCuDan,
                        @Param("maCanHo") String maCanHo,
                        @Param("vaiTro") Integer vaiTro,
                        @Param("trangThai") String trangThai,
                        Pageable pageable);

        // Xóa trực tiếp theo mã cư dân
        @Modifying
        @Query("DELETE FROM CuDanCanHo c WHERE c.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}