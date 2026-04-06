package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.LoaiPhuongTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiPhuongTienRepository extends JpaRepository<LoaiPhuongTien, String> {

    // Tìm kiếm phân trang
    @Query("SELECT pt FROM LoaiPhuongTien pt WHERE " +
            "LOWER(pt.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(pt.loaiXe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(pt.maDangKy) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<LoaiPhuongTien> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo mã cư dân
    List<LoaiPhuongTien> findByMaCuDan(String maCuDan);

    // Tìm theo loại xe
    List<LoaiPhuongTien> findByLoaiXe(String loaiXe);

    // Tìm theo biển số
    LoaiPhuongTien findByMaDangKy(String maDangKy);

    // Đếm số xe theo loại
    @Query("SELECT COUNT(pt) FROM LoaiPhuongTien pt WHERE pt.loaiXe = :loaiXe")
    long countByLoaiXe(@Param("loaiXe") String loaiXe);

    // Đếm số xe theo cư dân
    @Query("SELECT COUNT(pt) FROM LoaiPhuongTien pt WHERE pt.maCuDan = :maCuDan")
    long countByMaCuDan(@Param("maCuDan") String maCuDan);

    // Thống kê số lượng theo từng loại xe
    @Query("SELECT pt.loaiXe, COUNT(pt) FROM LoaiPhuongTien pt GROUP BY pt.loaiXe")
    List<Object[]> countByLoaiXeGroup();
}