package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienChiPhiHoatDong;
import com.example.quanlychungcu.model.FeeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface TienChiPhiHoatDongRepository extends JpaRepository<TienChiPhiHoatDong, FeeId> {

        // Tìm kiếm phân trang (bao gồm tên cư dân)
        @Query("SELECT t FROM TienChiPhiHoatDong t LEFT JOIN t.cuDan cd WHERE " +
                        "LOWER(t.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(t.ngayThu AS string) LIKE CONCAT('%', :keyword, '%')")
        Page<TienChiPhiHoatDong> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        List<TienChiPhiHoatDong> findByMaCuDan(String maCuDan);

        List<TienChiPhiHoatDong> findByNgayThu(LocalDate ngayThu);

        List<TienChiPhiHoatDong> findByNgayThuBetween(LocalDate start, LocalDate end);

        List<TienChiPhiHoatDong> findByTrangThai(boolean trangThai);

        @Query("SELECT t FROM TienChiPhiHoatDong t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        List<TienChiPhiHoatDong> findByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT SUM(t.tienChiPhiHoatDong) FROM TienChiPhiHoatDong t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        Double sumByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT SUM(t.soNguoi) FROM TienChiPhiHoatDong t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month")
        Long sumPeopleByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT COUNT(t) FROM TienChiPhiHoatDong t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month AND t.trangThai = true")
        long countPaidByMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT t FROM TienChiPhiHoatDong t WHERE t.maCuDan = :maCuDan AND t.ngayThu = :ngayThu")
        List<TienChiPhiHoatDong> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan,
                        @Param("ngayThu") LocalDate ngayThu);

        @Query("SELECT t FROM TienChiPhiHoatDong t WHERE YEAR(t.ngayThu) = :year AND MONTH(t.ngayThu) = :month ORDER BY t.tienChiPhiHoatDong DESC")
        List<TienChiPhiHoatDong> findTopByMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

        // Xóa trực tiếp theo mã cư dân
        @Modifying
        @Query("DELETE FROM TienChiPhiHoatDong t WHERE t.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}