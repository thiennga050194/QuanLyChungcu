package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.CuDan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuDanRepository extends JpaRepository<CuDan, String> {

    // ===== PHÂN TRANG CƠ BẢN =====

    // Tìm kiếm theo tên hoặc mã (có phân trang)
    Page<CuDan> findByTenCuDanContainingIgnoreCaseOrMaCuDanContainingIgnoreCase(
            String tenCuDan, String maCuDan, Pageable pageable);

    // Tìm kiếm nâng cao với nhiều tiêu chí
    @Query("SELECT c FROM CuDan c WHERE " +
            "(:keyword IS NULL OR " +
            "   LOWER(c.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(c.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(c.soDT) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(c.soCMT) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "   LOWER(c.queQuan) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<CuDan> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm cư dân theo độ tuổi
    @Query("SELECT c FROM CuDan c WHERE YEAR(c.ngaySinh) BETWEEN :startYear AND :endYear")
    Page<CuDan> findByNamSinhBetween(@Param("startYear") int startYear,
                                     @Param("endYear") int endYear,
                                     Pageable pageable);

    // ===== CÁC METHOD TÌM KIẾM THEO QUAN HỆ =====

    // Tìm cư dân theo căn hộ (thông qua bảng trung gian)
    @Query("SELECT c FROM CuDan c JOIN c.danhSachQuanHe cc WHERE cc.maCanHo = :maCanHo AND cc.ngayKetThuc IS NULL")
    Page<CuDan> findByCanHoHienTai(@Param("maCanHo") String maCanHo, Pageable pageable);

    // Tìm cư dân theo khu (thông qua căn hộ)
    @Query("SELECT DISTINCT c FROM CuDan c JOIN c.danhSachQuanHe cc JOIN cc.canHo ch WHERE ch.maKhu = :maKhu AND cc.ngayKetThuc IS NULL")
    Page<CuDan> findByKhuHienTai(@Param("maKhu") String maKhu, Pageable pageable);

    // Tìm cư dân theo vai trò trong căn hộ
    @Query("SELECT c FROM CuDan c JOIN c.danhSachQuanHe cc WHERE cc.vaiTro = :vaiTro AND cc.ngayKetThuc IS NULL")
    Page<CuDan> findByVaiTroHienTai(@Param("vaiTro") int vaiTro, Pageable pageable);

    // ===== CÁC METHOD KHÔNG PHÂN TRANG (GIỮ NGUYÊN) =====

    // Tìm theo tên (chứa) - không phân trang
    List<CuDan> findByTenCuDanContainingIgnoreCase(String tenCuDan);

    // Tìm theo số điện thoại
    Optional<CuDan> findBySoDT(String soDT);

    // Tìm theo số CMT
    Optional<CuDan> findBySoCMT(String soCMT);

    // Kiểm tra tồn tại theo số điện thoại (loại trừ 1 cư dân)
    @Query("SELECT COUNT(c) > 0 FROM CuDan c WHERE c.soDT = :soDT AND c.maCuDan != :maCuDan")
    boolean existsBySoDTAndNotMaCuDan(@Param("soDT") String soDT, @Param("maCuDan") String maCuDan);

    // Đếm số cư dân theo quê quán
    @Query("SELECT c.queQuan, COUNT(c) FROM CuDan c GROUP BY c.queQuan")
    List<Object[]> thongKeTheoQueQuan();

    // ===== THỐNG KÊ =====

    // Đếm số cư dân theo giới tính
    @Query("SELECT c.gioiTinh, COUNT(c) FROM CuDan c GROUP BY c.gioiTinh")
    List<Object[]> thongKeTheoGioiTinh();

    // Đếm số cư dân đang ở (có quan hệ hiện tại)
    @Query("SELECT COUNT(DISTINCT c) FROM CuDan c JOIN c.danhSachQuanHe cc WHERE cc.ngayKetThuc IS NULL")
    long countDangOHienTai();

    // Top cư dân có nhiều căn hộ nhất
    @Query("SELECT c.maCuDan, c.tenCuDan, COUNT(cc) as soCanHo FROM CuDan c " +
            "JOIN c.danhSachQuanHe cc WHERE cc.ngayKetThuc IS NULL " +
            "GROUP BY c.maCuDan, c.tenCuDan ORDER BY soCanHo DESC")
    List<Object[]> findTopCuDanBySoCanHo(Pageable pageable);
}