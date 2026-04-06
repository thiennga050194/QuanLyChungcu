package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.HopDong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HopDongRepository extends JpaRepository<HopDong, String> {

    // Tìm kiếm phân trang
    @Query("SELECT hd FROM HopDong hd WHERE " +
            "LOWER(hd.maHopDong) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(hd.tenKH) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(hd.maCanHo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(hd.maCuDan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<HopDong> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo mã căn hộ
    List<HopDong> findByMaCanHo(String maCanHo);

    // Tìm theo mã cư dân
    List<HopDong> findByMaCuDan(String maCuDan);

    // Tìm theo ngày giao dịch
    List<HopDong> findByNgayGiaoDichContaining(String ngayGiaoDich);

    // Đếm số hợp đồng theo căn hộ
    @Query("SELECT COUNT(hd) FROM HopDong hd WHERE hd.maCanHo = :maCanHo")
    long countByMaCanHo(@Param("maCanHo") String maCanHo);
}