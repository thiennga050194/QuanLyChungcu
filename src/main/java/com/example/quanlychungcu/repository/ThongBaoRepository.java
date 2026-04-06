package com.example.quanlychungcu.repository;

import com.example.quanlychungcu.model.ThongBao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {

    // Tìm kiếm phân trang
    @Query("SELECT tb FROM ThongBao tb WHERE " +
            "LOWER(tb.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(tb.noiDung) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ThongBao> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Lấy thông báo mới nhất
    Page<ThongBao> findAllByOrderByThoiGianDesc(Pageable pageable);

    // Tìm theo khoảng thời gian
    List<ThongBao> findByThoiGianBetween(Timestamp start, Timestamp end);

    // Tìm theo tiêu đề
    List<ThongBao> findByTieuDeContainingIgnoreCase(String tieuDe);

    // Đếm số thông báo trong tháng
    @Query("SELECT COUNT(tb) FROM ThongBao tb WHERE " +
            "YEAR(tb.thoiGian) = :year AND MONTH(tb.thoiGian) = :month")
    long countByMonth(@Param("year") int year, @Param("month") int month);

    // Lấy thông báo theo năm
    @Query("SELECT tb FROM ThongBao tb WHERE YEAR(tb.thoiGian) = :year")
    List<ThongBao> findByYear(@Param("year") int year);
}