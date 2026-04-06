package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.CanHo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CanHoRepository extends JpaRepository<CanHo, String> {

    // ===== PHÂN TRANG CƠ BẢN =====

    // Tìm kiếm phân trang theo mã căn hộ hoặc tên khu
    @Query("SELECT ch FROM CanHo ch WHERE " +
            "LOWER(ch.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(ch.khuCanHo.tenKhu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CanHo> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo trạng thái có phân trang
    Page<CanHo> findByTrangThai(boolean trangThai, Pageable pageable);

    // Tìm theo mã khu có phân trang
    Page<CanHo> findByMaKhu(String maKhu, Pageable pageable);

    // Tìm căn hộ trống có phân trang
    @Query("SELECT ch FROM CanHo ch WHERE ch.trangThai = false")
    Page<CanHo> findEmptyApartments(Pageable pageable);

    // ===== PHÂN TRANG CHO QUAN HỆ NHIỀU-NHIỀU =====

    // Tìm căn hộ theo cư dân (phân trang)
    @Query("SELECT ch FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE cc.maCuDan = :maCuDan AND cc.ngayKetThuc IS NULL")
    Page<CanHo> findCanHoByCuDan(@Param("maCuDan") String maCuDan, Pageable pageable);

    // Tìm căn hộ theo cư dân và vai trò (phân trang)
    @Query("SELECT ch FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE cc.maCuDan = :maCuDan AND cc.vaiTro = :vaiTro AND cc.ngayKetThuc IS NULL")
    Page<CanHo> findCanHoByCuDanAndVaiTro(@Param("maCuDan") String maCuDan, @Param("vaiTro") int vaiTro, Pageable pageable);

    // ===== TÌM KIẾM NÂNG CAO CÓ PHÂN TRANG =====

    // Tìm kiếm căn hộ theo nhiều tiêu chí
    @Query("SELECT DISTINCT ch FROM CanHo ch " +
            "LEFT JOIN ch.danhSachQuanHe cc " +
            "WHERE (:maCanHo IS NULL OR LOWER(ch.maCanHo) LIKE LOWER(CONCAT('%', :maCanHo, '%'))) AND " +
            "(:maKhu IS NULL OR ch.maKhu = :maKhu) AND " +
            "(:trangThai IS NULL OR ch.trangThai = :trangThai) AND " +
            "(:coNguoiO IS NULL OR " +
            "   (:coNguoiO = true AND EXISTS (SELECT 1 FROM CuDanCanHo c WHERE c.maCanHo = ch.maCanHo AND c.ngayKetThuc IS NULL)) OR " +
            "   (:coNguoiO = false AND NOT EXISTS (SELECT 1 FROM CuDanCanHo c WHERE c.maCanHo = ch.maCanHo AND c.ngayKetThuc IS NULL)))")
    Page<CanHo> searchCanHo(
            @Param("maCanHo") String maCanHo,
            @Param("maKhu") String maKhu,
            @Param("trangThai") Boolean trangThai,
            @Param("coNguoiO") Boolean coNguoiO,
            Pageable pageable);

    // ===== THỐNG KÊ CÓ PHÂN TRANG =====

    // Tìm căn hộ có nhiều cư dân nhất (phân trang)
    @Query("SELECT ch FROM CanHo ch LEFT JOIN ch.danhSachQuanHe cc " +
            "WHERE cc.ngayKetThuc IS NULL " +
            "GROUP BY ch " +
            "ORDER BY COUNT(cc) DESC")
    Page<CanHo> findTopCanHoBySoNguoi(Pageable pageable);

    // Tìm căn hộ theo khoảng diện tích
    @Query("SELECT ch FROM CanHo ch WHERE ch.dienTich BETWEEN :minDienTich AND :maxDienTich")
    Page<CanHo> findByDienTichBetween(@Param("minDienTich") float minDienTich,
                                      @Param("maxDienTich") float maxDienTich,
                                      Pageable pageable);

    // Tìm căn hộ theo số phòng
    Page<CanHo> findBySoPhong(int soPhong, Pageable pageable);

    // ===== CÁC METHOD KHÔNG PHÂN TRANG (GIỮ NGUYÊN) =====

    // Tìm theo trạng thái (không phân trang)
    List<CanHo> findByTrangThai(boolean trangThai);

    // Tìm theo mã khu (không phân trang)
    List<CanHo> findByMaKhu(String maKhu);

    // Tìm căn hộ trống (không phân trang)
    @Query("SELECT ch FROM CanHo ch WHERE ch.trangThai = false")
    List<CanHo> findEmptyApartments();

    // Đếm số căn hộ theo khu
    @Query("SELECT COUNT(ch) FROM CanHo ch WHERE ch.maKhu = :maKhu")
    long countByMaKhu(@Param("maKhu") String maKhu);

    // Tìm căn hộ theo cư dân (không phân trang)
    @Query("SELECT ch FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE cc.maCuDan = :maCuDan AND cc.ngayKetThuc IS NULL")
    List<CanHo> findCanHoByCuDan(@Param("maCuDan") String maCuDan);

    // Tìm căn hộ theo cư dân và vai trò (không phân trang)
    @Query("SELECT ch FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE cc.maCuDan = :maCuDan AND cc.vaiTro = :vaiTro AND cc.ngayKetThuc IS NULL")
    List<CanHo> findCanHoByCuDanAndVaiTro(@Param("maCuDan") String maCuDan, @Param("vaiTro") int vaiTro);

    // Kiểm tra căn hộ có cư dân không
    @Query("SELECT COUNT(cc) > 0 FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE ch.maCanHo = :maCanHo AND cc.ngayKetThuc IS NULL")
    boolean hasResidents(@Param("maCanHo") String maCanHo);

    // Đếm số cư dân trong căn hộ
    @Query("SELECT COUNT(cc) FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE ch.maCanHo = :maCanHo AND cc.ngayKetThuc IS NULL")
    long countResidentsInCanHo(@Param("maCanHo") String maCanHo);

    // Tìm chủ hộ của căn hộ
    @Query("SELECT cc.cuDan FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE ch.maCanHo = :maCanHo AND cc.vaiTro = 0 AND cc.ngayKetThuc IS NULL")
    Optional<Object> findChuHoByCanHo(@Param("maCanHo") String maCanHo);

    // Tìm tất cả cư dân trong căn hộ (chưa kết thúc)
    @Query("SELECT cc.cuDan FROM CanHo ch JOIN ch.danhSachQuanHe cc WHERE ch.maCanHo = :maCanHo AND cc.ngayKetThuc IS NULL")
    List<Object> findAllResidentsInCanHo(@Param("maCanHo") String maCanHo);

    // Thống kê số căn hộ theo trạng thái và khu
    @Query("SELECT ch.trangThai, COUNT(ch) FROM CanHo ch WHERE ch.maKhu = :maKhu GROUP BY ch.trangThai")
    List<Object[]> thongKeCanHoTheoKhu(@Param("maKhu") String maKhu);

    // Tìm căn hộ có nhiều cư dân nhất (không phân trang)

}