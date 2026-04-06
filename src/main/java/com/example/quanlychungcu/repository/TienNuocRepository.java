package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienNuoc;
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
public interface TienNuocRepository extends JpaRepository<TienNuoc, FeeId> {

        // Tìm kiếm phân trang (bao gồm tên cư dân)
        @Query("SELECT tn FROM TienNuoc tn LEFT JOIN tn.cuDan cd WHERE " +
                        "LOWER(tn.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(tn.ngayThu AS string) LIKE CONCAT('%', :keyword, '%')")
        Page<TienNuoc> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        // Tìm theo mã cư dân
        List<TienNuoc> findByMaCuDan(String maCuDan);

        // Tìm theo ngày thu
        List<TienNuoc> findByNgayThu(LocalDate ngayThu);

        // Tìm theo khoảng thời gian
        List<TienNuoc> findByNgayThuBetween(LocalDate start, LocalDate end);

        // Tìm theo trạng thái
        List<TienNuoc> findByTrangThai(boolean trangThai);

        // Tìm theo tháng/năm
        @Query("SELECT tn FROM TienNuoc tn WHERE " +
                        "YEAR(tn.ngayThu) = :year AND MONTH(tn.ngayThu) = :month")
        List<TienNuoc> findByMonth(@Param("year") int year, @Param("month") int month);

        // Tính tổng tiền nước theo tháng
        @Query("SELECT SUM(tn.soTienNuocPhaiTra) FROM TienNuoc tn WHERE " +
                        "YEAR(tn.ngayThu) = :year AND MONTH(tn.ngayThu) = :month")
        Double sumByMonth(@Param("year") int year, @Param("month") int month);

        // Tính tổng số khối nước theo tháng
        @Query("SELECT SUM(tn.soKhoiNuocSuDung) FROM TienNuoc tn WHERE " +
                        "YEAR(tn.ngayThu) = :year AND MONTH(tn.ngayThu) = :month")
        Double sumKhoiByMonth(@Param("year") int year, @Param("month") int month);

        // Đếm số hộ đã đóng theo tháng
        @Query("SELECT COUNT(tn) FROM TienNuoc tn WHERE " +
                        "YEAR(tn.ngayThu) = :year AND MONTH(tn.ngayThu) = :month AND tn.trangThai = true")
        long countPaidByMonth(@Param("year") int year, @Param("month") int month);

        // Tìm theo mã cư dân và ngày
        @Query("SELECT tn FROM TienNuoc tn WHERE tn.maCuDan = :maCuDan AND tn.ngayThu = :ngayThu")
        List<TienNuoc> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan, @Param("ngayThu") LocalDate ngayThu);

        // Lấy top hộ dùng nhiều nước nhất
        @Query("SELECT tn FROM TienNuoc tn WHERE YEAR(tn.ngayThu) = :year AND MONTH(tn.ngayThu) = :month " +
                        "ORDER BY tn.soKhoiNuocSuDung DESC")
        List<TienNuoc> findTopByMonth(@Param("year") int year, @Param("month") int month, Pageable pageable);

        // Xóa trực tiếp theo mã cư dân
        @Modifying
        @Query("DELETE FROM TienNuoc tn WHERE tn.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}