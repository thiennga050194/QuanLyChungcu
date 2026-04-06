package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TienDien;
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
public interface TienDienRepository extends JpaRepository<TienDien, FeeId> {

        // Tìm kiếm phân trang (bao gồm tên cư dân)
        @Query("SELECT td FROM TienDien td LEFT JOIN td.cuDan cd WHERE " +
                        "LOWER(td.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(cd.tenCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(td.ngayThu AS string) LIKE CONCAT('%', :keyword, '%')")
        Page<TienDien> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

        // Tìm theo mã cư dân
        List<TienDien> findByMaCuDan(String maCuDan);

        // Tìm theo ngày thu
        List<TienDien> findByNgayThu(LocalDate ngayThu);

        // Tìm theo khoảng thời gian
        List<TienDien> findByNgayThuBetween(LocalDate start, LocalDate end);

        // Tìm theo trạng thái
        List<TienDien> findByTrangThai(boolean trangThai);

        // Tìm theo tháng/năm
        @Query("SELECT td FROM TienDien td WHERE " +
                        "YEAR(td.ngayThu) = :year AND MONTH(td.ngayThu) = :month")
        List<TienDien> findByMonth(@Param("year") int year, @Param("month") int month);

        // Tính tổng tiền điện theo tháng
        @Query("SELECT SUM(td.tongSoTienPhaiTra) FROM TienDien td WHERE " +
                        "YEAR(td.ngayThu) = :year AND MONTH(td.ngayThu) = :month")
        Double sumByMonth(@Param("year") int year, @Param("month") int month);

        // Tính tổng số điện theo tháng
        @Query("SELECT SUM(td.tongSoDienSuDung) FROM TienDien td WHERE " +
                        "YEAR(td.ngayThu) = :year AND MONTH(td.ngayThu) = :month")
        Double sumDienByMonth(@Param("year") int year, @Param("month") int month);

        // Đếm số hộ đã đóng theo tháng
        @Query("SELECT COUNT(td) FROM TienDien td WHERE " +
                        "YEAR(td.ngayThu) = :year AND MONTH(td.ngayThu) = :month AND td.trangThai = true")
        long countPaidByMonth(@Param("year") int year, @Param("month") int month);

        // Tìm theo mã cư dân và ngày
        @Query("SELECT td FROM TienDien td WHERE td.maCuDan = :maCuDan AND td.ngayThu = :ngayThu")
        List<TienDien> findByMaCuDanAndNgayThu(@Param("maCuDan") String maCuDan, @Param("ngayThu") LocalDate ngayThu);

        // Xóa trực tiếp theo mã cư dân (không cần load entity)
        @Modifying
        @Query("DELETE FROM TienDien td WHERE td.maCuDan = :maCuDan")
        void deleteByMaCuDan(@Param("maCuDan") String maCuDan);
}