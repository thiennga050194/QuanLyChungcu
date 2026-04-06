-- Tháo bỏ các ràng buộc cũ nếu có
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'QuanLyChungCu')
BEGIN

    DROP DATABASE QuanLyChungCu;
END
GO

CREATE DATABASE QuanLyChungCu;
GO
USE QuanLyChungCu;
GO

-- 1. Bảng Khu Căn Hộ

-- =============================================
-- 1. XÓA CÁC BẢNG CŨ THEO ĐÚNG THỨ TỰ
-- =============================================
IF OBJECT_ID('TienDien', 'U') IS NOT NULL DROP TABLE TienDien;
IF OBJECT_ID('TienNuoc', 'U') IS NOT NULL DROP TABLE TienNuoc;
IF OBJECT_ID('TienVeSinhChung', 'U') IS NOT NULL DROP TABLE TienVeSinhChung;
IF OBJECT_ID('TienPhiDuyTri', 'U') IS NOT NULL DROP TABLE TienPhiDuyTri;
IF OBJECT_ID('TienGuiXe', 'U') IS NOT NULL DROP TABLE TienGuiXe;
IF OBJECT_ID('TienChiPhiHoatDong', 'U') IS NOT NULL DROP TABLE TienChiPhiHoatDong;
IF OBJECT_ID('HopDong', 'U') IS NOT NULL DROP TABLE HopDong;
IF OBJECT_ID('LoaiPhuongTien', 'U') IS NOT NULL DROP TABLE LoaiPhuongTien;
IF OBJECT_ID('CUDAN_CANHO', 'U') IS NOT NULL DROP TABLE CUDAN_CANHO; -- Lưu ý tên bảng
IF OBJECT_ID('PhanHoi', 'U') IS NOT NULL DROP TABLE PhanHoi;
IF OBJECT_ID('ThongBao', 'U') IS NOT NULL DROP TABLE ThongBao;
IF OBJECT_ID('CANHO', 'U') IS NOT NULL DROP TABLE CANHO;           -- Lưu ý tên bảng
IF OBJECT_ID('CUDAN', 'U') IS NOT NULL DROP TABLE CUDAN;           -- Lưu ý tên bảng
IF OBJECT_ID('KHUCANHO', 'U') IS NOT NULL DROP TABLE KHUCANHO;     -- Lưu ý tên bảng
IF OBJECT_ID('TaiKhoan', 'U') IS NOT NULL DROP TABLE TaiKhoan;

-- =============================================
-- 2. TẠO MỚI CÁC BẢNG (CHUẨN TÊN THEO ENTITY)
-- =============================================

CREATE TABLE TaiKhoan (
    TenTaiKhoan NVARCHAR(50) PRIMARY KEY,
    MatKhau NVARCHAR(max) NOT NULL,
    VaiTro INT NOT NULL 
);

CREATE TABLE KHUCANHO (
    MaKhu NVARCHAR(50) PRIMARY KEY,
    TenKhu NVARCHAR(255) NOT NULL,
    SoTang INT,
    SoCanTT INT, 
    DiaChi NVARCHAR(MAX)
);

CREATE TABLE CANHO (
    MaCanHo NVARCHAR(50) PRIMARY KEY,
    DienTich FLOAT,
    SoPhong INT,
    TrangThai BIT NOT NULL DEFAULT 0,
    MaKhu NVARCHAR(50),
    CONSTRAINT FK_CanHo_Khu FOREIGN KEY (MaKhu) REFERENCES KHUCANHO(MaKhu)
);

CREATE TABLE CUDAN (
    MaCuDan NVARCHAR(50) PRIMARY KEY,
    TenCuDan NVARCHAR(255) NOT NULL,
    NgaySinh DATE,
    GioiTinh BIT, 
    SoDT NVARCHAR(15),
    SoCMT NVARCHAR(20),
    QueQuan NVARCHAR(MAX)
);

-- Bảng trung gian (Tên phải là CUDAN_CANHO)
CREATE TABLE CUDAN_CANHO (
    MaCuDan NVARCHAR(50),
    MaCanHo NVARCHAR(50),
    NgayBatDau DATE,
    VaiTro INT, 
    NgayKetThuc DATE,
    CONSTRAINT PK_CuDanCanHo PRIMARY KEY (MaCuDan, MaCanHo, NgayBatDau),
    CONSTRAINT FK_CDCH_CuDan FOREIGN KEY (MaCuDan) REFERENCES CUDAN(MaCuDan),
    CONSTRAINT FK_CDCH_CanHo FOREIGN KEY (MaCanHo) REFERENCES CANHO(MaCanHo)
);

CREATE TABLE ThongBao (
    MaThongBao INT IDENTITY(1,1) PRIMARY KEY,
    TieuDe NVARCHAR(255),
    NoiDung NVARCHAR(MAX),
    ThoiGian DATETIME DEFAULT GETDATE()
);

CREATE TABLE PhanHoi (
    MaPhanHoi INT IDENTITY(1,1) PRIMARY KEY,
    TaiKhoan NVARCHAR(50),
    PhanHoi NVARCHAR(MAX),
    Image NVARCHAR(MAX),
    ThoiGian DATETIME DEFAULT GETDATE(),
    BanQuanLy INT DEFAULT 0,
    CONSTRAINT FK_PhanHoi_TK FOREIGN KEY (TaiKhoan) REFERENCES TaiKhoan(TenTaiKhoan)
);

CREATE TABLE LoaiPhuongTien (
    
    MaCuDan NVARCHAR(50),
    LoaiXe NVARCHAR(100),
	MaDangKy NVARCHAR(50) PRIMARY KEY,
    CONSTRAINT FK_PhuongTien_CuDan FOREIGN KEY (MaCuDan) REFERENCES CUDAN(MaCuDan)
);

CREATE TABLE HopDong (
    MaHopDong NVARCHAR(50) PRIMARY KEY,
    TenKH NVARCHAR(255),
    MaCanHo NVARCHAR(50),
    MaCuDan NVARCHAR(50),
    DiaChiKhachHang NVARCHAR(MAX),
    NgayGiaoDich NVARCHAR(50),
    CONSTRAINT FK_HopDong_CanHo FOREIGN KEY (MaCanHo) REFERENCES CANHO(MaCanHo),
    CONSTRAINT FK_HopDong_CuDan FOREIGN KEY (MaCuDan) REFERENCES CUDAN(MaCuDan)
);

-- Các bảng Tiền Phí (Tên bảng khớp với @Table trong code)
CREATE TABLE TienDien (
    MaCuDan NVARCHAR(50),
    MaCanHo NVARCHAR(50),
    NgayThu DATE,
    TongSoDienSuDung FLOAT,
    tongSoTienPhaiTra FLOAT,
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienDien PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);

CREATE TABLE TienNuoc (
    MaCuDan NVARCHAR(50),
    MaCanHo NVARCHAR(50), 
    NgayThu DATE,
    SoKhoiNuocSuDung FLOAT, 
    SoTienNuocPhaiTra FLOAT, 
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienNuoc PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);

CREATE TABLE TienVeSinhChung (
    MaCuDan NVARCHAR(50), 
    MaCanHo NVARCHAR(50), 
    NgayThu DATE,
    SoNguoi INT, 
    TienVeSinhChung FLOAT, 
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienVSC PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);

CREATE TABLE TienPhiDuyTri (
    MaCuDan NVARCHAR(50), 
    MaCanHo NVARCHAR(50), 
    NgayThu DATE,
    SoNguoi INT, 
    TienDuyTri FLOAT, 
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienPDT PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);

CREATE TABLE TienGuiXe (
    MaCuDan NVARCHAR(50), 
    MaCanHo NVARCHAR(50), NgayThu DATE,
    XeOTo INT DEFAULT 0, 
    TienGuiOTo FLOAT DEFAULT 0,
    XeMay INT DEFAULT 0, 
    TienGuiXeMay FLOAT DEFAULT 0,
    XeDap INT DEFAULT 0, 
    TienGuiXeDap FLOAT DEFAULT 0,
    TongTienGuiXe FLOAT, 
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienGuiXe PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);

CREATE TABLE TienChiPhiHoatDong (
    MaCuDan NVARCHAR(50),
    MaCanHo NVARCHAR(50),
    NgayThu DATE,
    soNguoi int,
    tienChiPhiHoatDong FLOAT, 
    TrangThai BIT DEFAULT 0,
    CONSTRAINT PK_TienCPHD PRIMARY KEY (MaCuDan, MaCanHo, NgayThu)
);
