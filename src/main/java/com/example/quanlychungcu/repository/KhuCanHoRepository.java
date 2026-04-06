package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.KhuCanHo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuCanHoRepository extends JpaRepository<KhuCanHo, String> {

    // Tìm kiếm phân trang theo tên khu hoặc mã khu
    @Query("SELECT k FROM KhuCanHo k WHERE " +
            "LOWER(k.tenKhu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(k.maKhu) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<KhuCanHo> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo tên khu (chính xác)
    KhuCanHo findByTenKhu(String tenKhu);

    // Tìm khu có nhiều căn nhất
    @Query("SELECT k FROM KhuCanHo k ORDER BY k.soCanTT DESC")
    List<KhuCanHo> findTopBySoCanTT(Pageable pageable);

    // Đếm tổng số căn hộ của tất cả các khu
    @Query("SELECT SUM(k.soCanTT) FROM KhuCanHo k")
    Long getTotalApartments();

    // Kiểm tra tên khu đã tồn tại chưa
    boolean existsByTenKhu(String tenKhu);
}