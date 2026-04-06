package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienPhiDuyTri;
import com.example.quanlychungcu.model.FeeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TienPhiDuyTriRepository extends JpaRepository<TienPhiDuyTri, FeeId> {

    // Tìm kiếm phân trang (bao gồm tên cư dân)
    @Query("SELECT t FROM TienPhiDuyTri t LEFT JOIN t.cuDan cd WHERE " +
            "LOWER(t.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(t.ngayThu AS string) LIKE CONCAT('%', :keyword, '%')")
    Page<TienPhiDuyTri> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    List<TienPhiDuyTri> findByMaCuDan(String maCuDan);

    List<TienPhiDuyTri> findByNgayThu(LocalDate ngayThu);

    List<TienPhiDuyTri> findByNgayThuBetween(LocalDate start, LocalDate end);

    List<TienPhiDuyTri> findByTrangThai(boolean trangThai);

    @Query("SELECT t FROM TienPhiDuyTri t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
    List<TienPhiDuyTri> findByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(t.tienDuyTri) FROM TienPhiDuyTri t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
    Double sumByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(t.soNguoi) FROM TienPhiDuyTri t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
    Long sumPeopleByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(t) FROM TienPhiDuyTri t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month AND t.trangThai = true")
    long countPaidByMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT t FROM TienPhiDuyTri t WHERE t.maCuDan = :maCuDan AND t.ngayThu = :ngayThu")
    List<TienPhiDuyTri> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan, @Param("ngayThu") LocalDate ngayThu);

    @Query("SELECT t FROM TienPhiDuyTri t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month ORDER BY t.tienDuyTri DESC")
    List<TienPhiDuyTri> findTopByMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

    // Xóa trực tiếp theo mã cư dân
    @Modifying
    @Query("DELETE FROM TienPhiDuyTri t WHERE t.maCuDan = :maCuDan")
    void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}