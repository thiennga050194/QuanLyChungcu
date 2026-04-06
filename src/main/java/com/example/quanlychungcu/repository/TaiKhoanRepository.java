package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {

    // Tìm kiếm phân trang
    @Query("SELECT tk FROM TaiKhoan tk WHERE " +
            "LOWER(tk.tenTaiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<TaiKhoan> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm theo vai trò
    List<TaiKhoan> findByVaiTro(int vaiTro);

    // Đếm theo vai trò
    @Query("SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.vaiTro = :vaiTro")
    long countByVaiTro(@Param("vaiTro") int vaiTro);

    // Kiểm tra username đã tồn tại
    boolean existsByTenTaiKhoan(String tenTaiKhoan);

    // Tìm username (cho Spring Security)
    Optional<TaiKhoan> findByTenTaiKhoan(String tenTaiKhoan);
}