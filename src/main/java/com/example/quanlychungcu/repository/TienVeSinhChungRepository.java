package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienVeSinhChung;
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
public interface TienVeSinhChungRepository extends JpaRepository<TienVeSinhChung, FeeId> {

        // Tìm kiếm phân trang (bao gồm tên cư dân)
        @Query("SELECT t FROM TienVeSinhChung t LEFT JOIN t.cuDan cd WHERE " +
                        "LOWER(t.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Page<TienVeSinhChung> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        List<TienVeSinhChung> findByMaCuDan(String maCuDan);

        List<TienVeSinhChung> findByNgayThu(LocalDate ngayThu);

        List<TienVeSinhChung> findByNgayThuBetween(LocalDate start, LocalDate end);

        List<TienVeSinhChung> findByTrangThai(boolean trangThai);

        @Query("SELECT t FROM TienVeSinhChung t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        List<TienVeSinhChung> findByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT COALESCE(SUM(t.tienVeSinhChung), 0.0) FROM TienVeSinhChung t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        Double sumByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT COALESCE(SUM(t.soNguoi), 0) FROM TienVeSinhChung t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        Long sumPeopleByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT COUNT(t) FROM TienVeSinhChung t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month AND t.trangThai = true")
        long countPaidByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT t FROM TienVeSinhChung t WHERE t.maCuDan = :maCuDan AND t.ngayThu = :ngayThu")
        List<TienVeSinhChung> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan,
                        @Param("ngayThu") LocalDate ngayThu);

        @Query("SELECT t FROM TienVeSinhChung t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month ORDER BY t.tienVeSinhChung DESC")
        List<TienVeSinhChung> findTopByMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

        // Xóa trực tiếp theo mã cư dân
        @Modifying
        @Query("DELETE FROM TienVeSinhChung t WHERE t.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}