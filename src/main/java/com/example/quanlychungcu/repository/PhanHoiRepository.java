package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.PhanHoi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PhanHoiRepository extends JpaRepository<PhanHoi, Integer> {

    // Tìm kiếm phân trang
    @Query("SELECT ph FROM PhanHoi ph WHERE " +
            "LOWER(ph.taiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(ph.phanHoi) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PhanHoi> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo tài khoản
    List<PhanHoi> findByTaiKhoan(String taiKhoan);

    // Tìm theo ban quản lý
    List<PhanHoi> findByBanquanly(int banquanly);

    // Tìm theo thời gian
    List<PhanHoi> findByThoiGianBetween(Timestamp start, Timestamp end);

    // Tìm phản hồi có hình ảnh
    @Query("SELECT ph FROM PhanHoi ph WHERE ph.image IS NOT NULL AND ph.image != ''")
    List<PhanHoi> findByCoHinhAnh();

    // Đếm số phản hồi theo tài khoản
    @Query("SELECT COUNT(ph) FROM PhanHoi ph WHERE ph.taiKhoan = :taiKhoan")
    long countByTaiKhoan(@Param("taiKhoan") String taiKhoan);

    // Lấy phản hồi mới nhất
    Page<PhanHoi> findAllByOrderByThoiGianDesc(Pageable pageable);
}