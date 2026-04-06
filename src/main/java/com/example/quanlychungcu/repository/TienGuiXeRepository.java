package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienGuiXe;
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
public interface TienGuiXeRepository extends JpaRepository<TienGuiXe, FeeId> {

        // Tìm kiếm phân trang (bao gồm tên cư dân)
        @Query("SELECT tgx FROM TienGuiXe tgx LEFT JOIN tgx.cuDan cd WHERE " +
                        "LOWER(tgx.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(tgx.ngayThu AS string) LIKE CONCAT('%', :keyword, '%')")
        Page<TienGuiXe> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        // Tìm theo mã cư dân
        List<TienGuiXe> findByMaCuDan(String maCuDan);

        // Tìm theo ngày thu
        List<TienGuiXe> findByNgayThu(LocalDate ngayThu);

        // Tìm theo khoảng thời gian
        List<TienGuiXe> findByNgayThuBetween(LocalDate start, LocalDate end);

        // Tìm theo trạng thái
        List<TienGuiXe> findByTrangThai(boolean trangThai);

        // Tìm theo tháng/năm
        @Query("SELECT tgx FROM TienGuiXe tgx WHERE " +
                        "YEAR(tgx.ngayThu) = :year AND MONTH(tgx.ngayThu) = :month")
        List<TienGuiXe> findByMonth(@Param("year") int year, @Param("month") int month);

        // Tính tổng tiền gửi xe theo tháng
        @Query("SELECT SUM(tgx.tongTienGuiXe) FROM TienGuiXe tgx WHERE " +
                        "YEAR(tgx.ngayThu) = :year AND MONTH(tgx.ngayThu) = :month")
        Double sumByMonth(@Param("year") int year, @Param("month") int month);

        // Thống kê số lượng xe theo tháng
        @Query("SELECT SUM(tgx.xeOTo), SUM(tgx.xeMay), SUM(tgx.xeDap) FROM TienGuiXe tgx WHERE " +
                        "YEAR(tgx.ngayThu) = :year AND MONTH(tgx.ngayThu) = :month")
        Object[] countVehiclesByMonth(@Param("year") int year, @Param("month") int month);

        // Đếm số hộ đã đóng theo tháng
        @Query("SELECT COUNT(tgx) FROM TienGuiXe tgx WHERE " +
                        "YEAR(tgx.ngayThu) = :year AND MONTH(tgx.ngayThu) = :month AND tgx.trangThai = true")
        long countPaidByMonth(@Param("year") int year, @Param("month") int month);

        // Tìm theo mã cư dân và ngày
        @Query("SELECT tgx FROM TienGuiXe tgx WHERE tgx.maCuDan = :maCuDan AND tgx.ngayThu = :ngayThu")
        List<TienGuiXe> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan, @Param("ngayThu") LocalDate ngayThu);

        // Xóa trực tiếp theo mã cư dân
        @Modifying
        @Query("DELETE FROM TienGuiXe tgx WHERE tgx.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}