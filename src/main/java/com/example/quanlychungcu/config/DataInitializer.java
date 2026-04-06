package com.example.quanlychungcu.config;

import com.example.quanlychungcu.model.*;
import com.example.quanlychungcu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private CuDanRepository cuDanRepository;
    @Autowired
    private ThongBaoRepository thongBaoRepository;
    @Autowired
    private PhanHoiRepository phanHoiRepository;
    @Autowired
    private KhuCanHoRepository khuCanHoRepository;
    @Autowired
    private CanHoRepository canHoRepository;
    @Autowired
    private LoaiPhuongTienRepository phuongTienRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CuDanCanHoRepository cuDanCanHoRepository;
    @Autowired
    private TienDienRepository tienDienRepository;
    @Autowired
    private TienNuocRepository tienNuocRepository;
    @Autowired
    private TienGuiXeRepository tienGuiXeRepository;
    @Autowired
    private TienPhiDuyTriRepository tienPhiDuyTriRepository;
    @Autowired
    private TienVeSinhChungRepository tienVeSinhChungRepository;
    @Autowired
    private TienChiPhiHoatDongRepository tienChiPhiHoatDongRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========== BẮT ĐẦU KHỞI TẠO DỮ LIỆU ĐỒ SỘ (MASSIVE DATA SEEDING) ==========");

        // 1. Tài khoản mặc định
        createAccountIfMissing("Admin", "123456", 0);
        createAccountIfMissing("NV001", "123456", 1);
        createAccountIfMissing("NV002", "123456", 1);

        // 2. Khu Căn Hộ
        if (khuCanHoRepository.count() == 0) {
            String[][] khus = {
                    { "AA", "Golden Tower A", "25", "10" },
                    { "AB", "Golden Tower B", "25", "10" },
                    { "BA", "Silver Garden A", "15", "8" },
                    { "BB", "Silver Garden B", "15", "8" },
                    { "CA", "Diamond Plaza A", "30", "12" },
                    { "CB", "Diamond Plaza B", "30", "12" }
            };
            for (String[] k : khus) {
                khuCanHoRepository.save(
                        new KhuCanHo(k[0], k[1], Integer.parseInt(k[2]), Integer.parseInt(k[3]), k[1] + ", Hà Nội"));
            }
            System.out.println("✓ Khởi tạo xong các Khu");
        }

        // 3. Căn Hộ
        if (canHoRepository.count() == 0) {
            List<KhuCanHo> allKhu = khuCanHoRepository.findAll();
            for (KhuCanHo k : allKhu) {
                for (int tang = 1; tang <= 4; tang++) {
                    for (int can = 1; can <= 6; can++) {
                        String maCH = String.format("%s%02d%02d", k.getMaKhu(), tang, can);
                        float dt = 40.0f + random.nextInt(100);
                        int sp = (dt < 60) ? 1 : (dt < 90 ? 2 : 3);
                        canHoRepository.save(new CanHo(maCH, dt, sp, true, k.getMaKhu()));
                    }
                }
            }
            System.out.println("✓ Khởi tạo xong Căn hộ (tất cả trạng thái Đã bán để nhận cư dân)");
        }

        // 4. Cư Dân
        if (cuDanRepository.count() < 50) {
            String[] ho = { "Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Phan", "Vũ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô" };
            String[] lot = { "Văn", "Thị", "Anh", "Minh", "Quốc", "Hồng", "Kim", "Xuân" };
            String[] ten = { "An", "Hòa", "Linh", "Dương", "Thảo", "Hùng", "Tùng", "Sơn", "Hồng", "Trang", "Quang",
                    "Phúc", "Lộc", "Thọ" };

            for (int i = 1; i <= 100; i++) {
                String maCD = String.format("%06d", 300000 + i);
                String hoTen = ho[random.nextInt(ho.length)] + " " + lot[random.nextInt(lot.length)] + " "
                        + ten[random.nextInt(ten.length)];
                CuDan cd = new CuDan(maCD, hoTen, LocalDate.now().minusYears(20 + random.nextInt(40)),
                        random.nextBoolean(), "09" + String.format("%08d", random.nextInt(100000000)),
                        String.format("%09d", random.nextInt(1000000000)), "Hà Nội");
                cuDanRepository.save(cd);
                createAccountIfMissing(maCD, "123456", 2);
            }
            System.out.println("✓ Khởi tạo xong 100 Cư dân mới");
        }

        // 5. Mối quan hệ cư dân - căn hộ
        if (cuDanCanHoRepository.count() < 30) {
            List<CanHo> chs = canHoRepository.findAll();
            List<CuDan> cds = cuDanRepository.findAll();
            int idx = 0;
            for (int i = 0; i < chs.size() && idx < cds.size(); i++) {
                CanHo ch = chs.get(i);
                CuDan chu = cds.get(idx++);
                assignResidentToApartment(chu.getMaCuDan(), ch.getMaCanHo(), (random.nextInt(10) < 8 ? 0 : 2));

                // Thêm thành viên (50%)
                if (random.nextBoolean() && idx < cds.size()) {
                    assignResidentToApartment(cds.get(idx++).getMaCuDan(), ch.getMaCanHo(), 1);
                }
            }
            System.out.println("✓ Thiết lập xong các mối quan hệ cư dân - căn hộ");
        }

        // 6. KHỞI TẠO PHÍ (BẮT BUỘC)
        // Nếu các bảng phí đang ít hoặc trống, ta sẽ quét qua tất cả quan hệ hiện có để
        // tạo phí
        if (tienDienRepository.count() < 50 || tienNuocRepository.count() < 50) {
            System.out.println("--- Đang quét và tạo dữ liệu phí cho cư dân ---");
            List<CuDanCanHo> rels = cuDanCanHoRepository.findAll();
            int countSeed = 0;
            for (CuDanCanHo rel : rels) {
                // Chỉ tạo phí cho Chủ hộ (0) hoặc Chủ thuê (2)
                if (rel.getNgayKetThuc() == null && (rel.getVaiTro() == 0 || rel.getVaiTro() == 2)) {
                    seedFeesForRelationship(rel.getMaCuDan(), rel.getMaCanHo());
                    countSeed++;
                }
            }
            System.out.println("✓ Đã nạp thành công dữ liệu phí cho " + countSeed + " hộ dân (3 tháng lịch sử)");
        }

        // 7. Thông báo & Phản hồi
        if (thongBaoRepository.count() < 3) {
            thongBaoRepository.save(new ThongBao(0, "Thông báo bảo trì hệ thống điện",
                    "Hệ thống điện tòa AA sẽ được bảo trì vào CN tới.", Timestamp.valueOf(LocalDateTime.now())));
            thongBaoRepository.save(new ThongBao(0, "Chào mừng năm mới 2026",
                    "Ban quản lý chúc toàn thể cư dân một năm mới an khang thịnh vượng.",
                    Timestamp.valueOf(LocalDateTime.now().minusDays(2))));
        }

        // 8. ĐẢM BẢO TẤT CẢ MẬT KHẨU ĐỀU ĐƯỢC MÃ HÓA (Dành cho Java 8)
        System.out.println("--- Đang kiểm tra định dạng mật khẩu toàn hệ thống ---");
        List<TaiKhoan> allAccounts = taiKhoanRepository.findAll();
        long encodedCount = 0;
        for (TaiKhoan tk : allAccounts) {
            if (tk.getMatKhau() == null || !tk.getMatKhau().startsWith("$2a$")) {
                // Nếu chưa mã hóa, ta lấy mật khẩu cũ để mã hóa lại, nếu rỗng thì dùng mặc định 123456
                String rawPass = (tk.getMatKhau() != null && !tk.getMatKhau().isEmpty()) ? tk.getMatKhau() : "123456";
                tk.setMatKhau(passwordEncoder.encode(rawPass));
                taiKhoanRepository.save(tk);
                encodedCount++;
            }
        }
        if (encodedCount > 0) {
            System.out.println("✓ Đã mã hóa lại " + encodedCount + " tài khoản.");
        }

        System.out.println("========== HOÀN TẤT MASSIVE DATA SEEDING ==========");
    }

    private void createAccountIfMissing(String username, String password, int role) {
        Optional<TaiKhoan> existing = taiKhoanRepository.findByTenTaiKhoan(username);
        if (!existing.isPresent()) {
            TaiKhoan tk = new TaiKhoan();
            tk.setTenTaiKhoan(username);
            tk.setMatKhau(passwordEncoder.encode(password));
            tk.setVaiTro(role);
            taiKhoanRepository.save(tk);
            System.out.println("✓ Đã tạo tài khoản mới: " + username);
        } else {
            // Nếu đã tồn tại, kiểm tra xem mật khẩu có được mã hóa chưa (BCrypt bắt đầu bằng $2a$)
            TaiKhoan tk = existing.get();
            if (tk.getMatKhau() == null || !tk.getMatKhau().startsWith("$2a$")) {
                tk.setMatKhau(passwordEncoder.encode(password));
                taiKhoanRepository.save(tk);
                System.out.println("✓ Đã mã hóa lại mật khẩu cho tài khoản: " + username);
            }
        }
    }

    private void assignResidentToApartment(String maCD, String maCH, int role) {
        try {
            cuDanCanHoRepository
                    .save(new CuDanCanHo(maCD, maCH, role, LocalDate.now().minusMonths(random.nextInt(12) + 1)));
        } catch (Exception ignored) {
        }
    }

    private void seedFeesForRelationship(String maCD, String maCH) {
        LocalDate start = LocalDate.now().minusMonths(3);
        for (int i = 0; i < 3; i++) {
            LocalDate date = start.plusMonths(i).withDayOfMonth(1);
            boolean paid = (i < 2) || random.nextBoolean();

            try {
                // Điện
                float kw = 120 + random.nextInt(350);
                tienDienRepository.save(new TienDien(maCD, maCH, date, kw, kw * 3200f, paid));

                // Nước
                float m3 = 10 + random.nextInt(20);
                tienNuocRepository.save(new TienNuoc(maCD, maCH, date, m3, m3 * 16000f, paid));

                // Xe
                TienGuiXe gx = new TienGuiXe(maCD, maCH, date);
                // Ô tô
                int soXeOTo = random.nextInt(10) < 3 ? 1 : 0;
                gx.setXeOTo(soXeOTo);
                gx.setTienGuiOTo(soXeOTo * 1200000f);

                int soXeMay = 1 + random.nextInt(2);
                gx.setXeMay(soXeMay);
                gx.setTienGuiXeMay(soXeMay * 70000f);
                // Xe đạp (THÊM VÀO)
                int soXeDap = random.nextInt(10) < 5 ? 1 : 0; // 50% có xe đạp
                gx.setXeDap(soXeDap);
                gx.setTienGuiXeDap(soXeDap * 300000f); // Giá xe đạp 300k/tháng
                // Trạng thái
                gx.setTrangThai(paid);
                // KHÔNG cần set tổng tiền - entity tự tính
                tienGuiXeRepository.save(gx);
                // Duy trì
                tienPhiDuyTriRepository.save(new TienPhiDuyTri(maCD, maCH, date, 1 + random.nextInt(3), 100000f, paid));

                // Hoạt động & Vệ sinh
                tienVeSinhChungRepository.save(new TienVeSinhChung(maCD, maCH, date, 1, 40000f, paid));
                tienChiPhiHoatDongRepository.save(new TienChiPhiHoatDong(maCD, maCH, date, 1, 50000f, paid));
            } catch (Exception e) {
                System.err.println("⚠ Lỗi nạp phí [" + maCD + "|" + maCH + "|" + date + "]: " + e.getMessage());
            }
        }
    }
}