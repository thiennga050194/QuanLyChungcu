/**
 * API Client for QuanLyChungCu
 * Centralized script to handle all API calls.
 */

const API_BASE = "/api";

const ApiClient = {
    // Shared fetch wrapper with error handling
    async request(url, options = {}) {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${API_BASE}${url}`, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token ? `Bearer ${token}` : '',
                    ...options.headers
                }
            });
            if (!response.ok) {
                if (response.status === 401) {
                    // Token expired or invalid
                    localStorage.removeItem('token');
                    localStorage.removeItem('username');
                    localStorage.removeItem('role');
                    window.location.href = '/login.html?error=unauthorized';
                    return null;
                }
                const error = await response.text();
                throw new Error(error || `Request failed with status ${response.status}`);
            }
            if (response.status === 204) return null;

            // Check if there is content to parse
            const text = await response.text();
            return text ? JSON.parse(text) : null;
        } catch (error) {
            console.error(`API Error [${url}]:`, error);
            throw error;
        }
    },

    // Helper để tạo URL phân trang
    buildPagedUrl(baseUrl, page, size, search = '', sortBy = '', sortDirection = 'ASC') {
        let url = `${baseUrl}/paged?page=${page}&size=${size}`;
        if (search) {
            url += `&search=${encodeURIComponent(search)}`;
        }
        if (sortBy) {
            url += `&sortBy=${sortBy}&sortDirection=${sortDirection}`;
        }
        return url;
    },

    // 1. Cư Dân
    cudan: {
        getAll: () => ApiClient.request("/cudan"),
        getById: (id) => ApiClient.request(`/cudan/${id}`),
        save: (data) => ApiClient.request("/cudan", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/cudan/${id}`, { method: "DELETE" }),
        update: (id, data) => ApiClient.request(`/cudan/${id}`, { method: "PUT", body: JSON.stringify(data) }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = '', sortDirection = 'ASC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/cudan", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getByVaiTro: (vaiTro) => ApiClient.request(`/cudan/vaitro/${vaiTro}`),
        getByMaCanHo: (maCanHo) => ApiClient.request(`/cudan/canho/${maCanHo}`),
    },

    // 2. Hợp Đồng
    hopdong: {
        getAll: () => ApiClient.request("/hopdong"),
        getById: (id) => ApiClient.request(`/hopdong/${id}`),
        save: (data) => ApiClient.request("/hopdong", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/hopdong/${id}`, { method: "DELETE" }),
        update: (id, data) => ApiClient.request(`/hopdong/${id}`, { method: "PUT", body: JSON.stringify(data) }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = 'ngayGiaoDich', sortDirection = 'DESC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/hopdong", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getByMaCanHo: (maCanHo) => ApiClient.request(`/hopdong/canho/${maCanHo}`),
        getByMaCuDan: (maCuDan) => ApiClient.request(`/hopdong/cudan/${maCuDan}`),
        getByNgay: (ngay) => ApiClient.request(`/hopdong/ngay/${ngay}`),
    },

    // 3. Phương Tiện
    phuongtien: {
        getAll: () => ApiClient.request("/phuongtien"),
        getById: (id) => ApiClient.request(`/phuongtien/${id}`),
        save: (data) => ApiClient.request("/phuongtien", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/phuongtien/${id}`, { method: "DELETE" }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = '', sortDirection = 'ASC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/phuongtien", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getByMaCuDan: (maCuDan) => ApiClient.request(`/phuongtien/cudan/${maCuDan}`),
        getByLoaiXe: (loaiXe) => ApiClient.request(`/phuongtien/loaixe/${encodeURIComponent(loaiXe)}`),
        getThongKe: () => ApiClient.request("/phuongtien/thongke"),
    },

    // 4. Phản Hồi
    phanhoi: {
        getAll: () => ApiClient.request("/phanhoi"),
        getById: (id) => ApiClient.request(`/phanhoi/${id}`),
        save: (data) => ApiClient.request("/phanhoi", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/phanhoi/${id}`, { method: "DELETE" }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = 'thoiGian', sortDirection = 'DESC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/phanhoi", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getByTaiKhoan: (taiKhoan) => ApiClient.request(`/phanhoi/taikhoan/${taiKhoan}`),
        getCoHinhAnh: () => ApiClient.request("/phanhoi/cohinhanh"),
        getLatest: (limit = 10) => ApiClient.request(`/phanhoi/latest?limit=${limit}`),
    },

    // 5. Thông Báo
    thongbao: {
        getAll: () => ApiClient.request("/thongbao"),
        getById: (id) => ApiClient.request(`/thongbao/${id}`),
        save: (data) => ApiClient.request("/thongbao", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/thongbao/${id}`, { method: "DELETE" }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = 'thoiGian', sortDirection = 'DESC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/thongbao", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getLatest: (limit = 5) => ApiClient.request(`/thongbao/latest?limit=${limit}`),
        getByYear: (year) => ApiClient.request(`/thongbao/nam/${year}`),
        getStatistics: () => ApiClient.request("/thongbao/statistics"),
    },

    // 6. Khu Căn Hộ
    khu: {
        getAll: () => ApiClient.request("/khu"),
        getById: (id) => ApiClient.request(`/khu/${id}`),
        create: (data) => ApiClient.request("/khu", { method: "POST", body: JSON.stringify(data) }),
        update: (id, data) => ApiClient.request(`/khu/${id}`, { method: "PUT", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/khu/${id}`, { method: "DELETE" }),
        addComplex: (data) => ApiClient.request("/khu/add-complex", { method: "POST", body: JSON.stringify(data) }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = '', sortDirection = 'ASC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/khu", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getByTen: (tenKhu) => ApiClient.request(`/khu/ten/${encodeURIComponent(tenKhu)}`),
        getTop: (limit = 5) => ApiClient.request(`/khu/top?limit=${limit}`),
        getTotalApartments: () => ApiClient.request("/khu/total-apartments"),
    },

    // 7. Căn Hộ
    canho: {
        getAll: () => ApiClient.request("/canho"),
        getById: (id) => ApiClient.request(`/canho/${id}`),
        save: (data) => ApiClient.request("/canho", { method: "POST", body: JSON.stringify(data) }),
        delete: (id) => ApiClient.request(`/canho/${id}`, { method: "DELETE" }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = '', sortDirection = 'ASC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/canho", page, size, search, sortBy, sortDirection)),

        // API bổ sung
        getEmpty: () => ApiClient.request("/canho/empty"),
        getByKhu: (maKhu) => ApiClient.request(`/canho/khu/${maKhu}`),
    },

    // 8. Các loại tiền phí
    tien: {
        dien: {
            getAll: () => ApiClient.request("/tien/dien"),
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/dien/cudan/${maCuDan}`),
            getByMonth: (year, month) => ApiClient.request(`/tien/dien/thang/${year}/${month}`),
            getStatistics: (year, month) => ApiClient.request(`/tien/dien/thongke/thang/${year}/${month}`),
            getOverview: () => ApiClient.request("/tien/dien/thongke/tongquan"),
            save: (data) => ApiClient.request("/tien/dien", { method: "POST", body: JSON.stringify(data) }),
            update: (maCuDan, maCanHo, ngayThu, data) =>
                ApiClient.request(`/tien/dien/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "PUT", body: JSON.stringify(data) }),
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/dien/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/dien", page, size, search, sortBy, sortDirection)),
        },

        nuoc: {
            getAll: () => ApiClient.request("/tien/nuoc"),
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/nuoc/cudan/${maCuDan}`),
            getByMonth: (year, month) => ApiClient.request(`/tien/nuoc/thang/${year}/${month}`),
            getTop: (year, month, limit = 3) => ApiClient.request(`/tien/nuoc/top/${year}/${month}?limit=${limit}`),
            getStatistics: (year, month) => ApiClient.request(`/tien/nuoc/thongke/thang/${year}/${month}`),
            getOverview: () => ApiClient.request("/tien/nuoc/thongke/tongquan"),
            save: (data) => ApiClient.request("/tien/nuoc", { method: "POST", body: JSON.stringify(data) }),
            update: (maCuDan, maCanHo, ngayThu, data) =>
                ApiClient.request(`/tien/nuoc/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "PUT", body: JSON.stringify(data) }),
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/nuoc/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/nuoc", page, size, search, sortBy, sortDirection)),
        },

        guiXe: {
            getAll: () => ApiClient.request("/tien/gui-xe"),
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/gui-xe/cudan/${maCuDan}`),
            getByMonth: (year, month) => ApiClient.request(`/tien/gui-xe/thang/${year}/${month}`),
            getStatistics: (year, month) => ApiClient.request(`/tien/gui-xe/thongke/thang/${year}/${month}`),
            getOverview: () => ApiClient.request("/tien/gui-xe/thongke/tongquan"),
            save: (data) => ApiClient.request("/tien/gui-xe", { method: "POST", body: JSON.stringify(data) }),
            update: (maCuDan, maCanHo, ngayThu, data) =>
                ApiClient.request(`/tien/gui-xe/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "PUT", body: JSON.stringify(data) }),
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/gui-xe/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/gui-xe", page, size, search, sortBy, sortDirection)),
        },

        chiPhiHoatDong: {
            getAll: () => ApiClient.request("/tien/chi-phi-hoat-dong"),
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/chi-phi-hoat-dong/cudan/${maCuDan}`),
            getByMonth: (year, month) => ApiClient.request(`/tien/chi-phi-hoat-dong/thang/${year}/${month}`),
            getStatistics: (year, month) => ApiClient.request(`/tien/chi-phi-hoat-dong/thongke/thang/${year}/${month}`),
            getOverview: () => ApiClient.request("/tien/chi-phi-hoat-dong/thongke/tongquan"),
            save: (data) => ApiClient.request("/tien/chi-phi-hoat-dong", { method: "POST", body: JSON.stringify(data) }),
            update: (maCuDan, maCanHo, ngayThu, data) =>
                ApiClient.request(`/tien/chi-phi-hoat-dong/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "PUT", body: JSON.stringify(data) }),
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/chi-phi-hoat-dong/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/chi-phi-hoat-dong", page, size, search, sortBy, sortDirection)),
        },

        phiDuyTri: {
            getAll: () => ApiClient.request("/tien/phi-duy-tri"),
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/phi-duy-tri/cudan/${maCuDan}`),
            getByMonth: (year, month) => ApiClient.request(`/tien/phi-duy-tri/thang/${year}/${month}`),
            getStatistics: (year, month) => ApiClient.request(`/tien/phi-duy-tri/thongke/thang/${year}/${month}`),
            getStatisticsByKhu: (year, month) => ApiClient.request(`/tien/phi-duy-tri/thongke/khu/${year}/${month}`),
            getOverview: () => ApiClient.request("/tien/phi-duy-tri/thongke/tongquan"),
            calculateFee: (soNguoi, donGia) =>
                ApiClient.request(`/tien/phi-duy-tri/tinh-phi?soNguoi=${soNguoi}&donGia=${donGia}`),
            save: (data) => ApiClient.request("/tien/phi-duy-tri", { method: "POST", body: JSON.stringify(data) }),
            update: (maCuDan, maCanHo, ngayThu, data) =>
                ApiClient.request(`/tien/phi-duy-tri/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "PUT", body: JSON.stringify(data) }),
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/phi-duy-tri/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/phi-duy-tri", page, size, search, sortBy, sortDirection)),
        },

        veSinhChung: {
            // Lấy tất cả bản ghi
            getAll: () => ApiClient.request("/tien/ve-sinh-chung"),

            // Lấy theo mã cư dân
            getByMaCuDan: (maCuDan) => ApiClient.request(`/tien/ve-sinh-chung/cudan/${maCuDan}`),

            // Lấy theo tháng
            getByMonth: (year, month) => ApiClient.request(`/tien/ve-sinh-chung/thang/${year}/${month}`),

            // Lấy top đóng góp nhiều nhất
            getTop: (year, month, limit = 3) =>
                ApiClient.request(`/tien/ve-sinh-chung/top/${year}/${month}?limit=${limit}`),

            // Lấy thống kê theo tháng
            getStatistics: (year, month) => ApiClient.request(`/tien/ve-sinh-chung/thongke/thang/${year}/${month}`),

            // Lấy thống kê theo khu vực
            getStatisticsByKhu: (year, month) => ApiClient.request(`/tien/ve-sinh-chung/thongke/khu/${year}/${month}`),

            // Lấy tổng quan
            getOverview: () => ApiClient.request("/tien/ve-sinh-chung/thongke/tongquan"),


            // Thêm mới bản ghi
            save: (data) => ApiClient.request("/tien/ve-sinh-chung", {
                method: "POST",
                body: JSON.stringify(data)
            }),

            // Cập nhật bản ghi
            update: (maCuDan, maCanHo, ngayThu, data) => ApiClient.request(`/tien/ve-sinh-chung/${maCuDan}/${maCanHo}/${ngayThu}`, {
                method: "PUT",
                body: JSON.stringify(data)
            }),

            // Xóa bản ghi
            delete: (maCuDan, maCanHo, ngayThu) =>
                ApiClient.request(`/tien/ve-sinh-chung/${maCuDan}/${maCanHo}/${ngayThu}`, { method: "DELETE" }),

            // API phân trang mới
            getPaged: (page, size, search = '', sortBy = 'ngayThu', sortDirection = 'DESC') =>
                ApiClient.request(ApiClient.buildPagedUrl("/tien/ve-sinh-chung", page, size, search, sortBy, sortDirection)),
        }
    },

    // 9. Hệ Thống / Tài Khoản
    user: {
        getMe: () => ApiClient.request("/user/me"),
        getAll: () => ApiClient.request("/user/all"),
        getByUsername: (username) => ApiClient.request(`/user/${username}`),
        getByVaiTro: (vaiTro) => ApiClient.request(`/user/vaitro/${vaiTro}`),
        getStatistics: () => ApiClient.request("/user/statistics"),
        save: (data) => ApiClient.request("/user", { method: "POST", body: JSON.stringify(data) }),
        update: (username, data) => ApiClient.request(`/user/${username}`, { method: "PUT", body: JSON.stringify(data) }),
        delete: (username) => ApiClient.request(`/user/${username}`, { method: "DELETE" }),

        // API phân trang mới
        getPaged: (page, size, search = '', sortBy = 'tenTaiKhoan', sortDirection = 'ASC') =>
            ApiClient.request(ApiClient.buildPagedUrl("/user", page, size, search, sortBy, sortDirection)),
    },

    // 10. Auth
    auth: {
        login: async (username, password) => {
            const data = await ApiClient.request("/auth/login", {
                method: "POST",
                body: JSON.stringify({ username, password })
            });
            if (data && data.accessToken) {
                localStorage.setItem('token', data.accessToken);
                localStorage.setItem('username', data.username);
                localStorage.setItem('role', data.role);
            }
            return data;
        },
        logout: () => {
            localStorage.removeItem('token');
            localStorage.removeItem('username');
            localStorage.removeItem('role');
            window.location.href = '/login.html';
        }
    }
};

// UI Manager to handle role-based interface
const UIManager = {
    role: null,

    async init() {
        try {
            const data = await ApiClient.user.getMe();
            this.role = (data.roles && data.roles.length > 0) ? data.roles[0] : null;
            if (this.role === 'ROLE_ADMIN') this.role = 'ADMIN';
            if (this.role === 'ROLE_STAFF') this.role = 'STAFF';

            // Cập nhật UI dựa trên role
            this.updateUserInfo();
        } catch (error) {
            console.error("Failed to load user info:", error);
            this.role = null;
        }
        this.applyRolePermissions();
    },

    updateUserInfo() {
        const userInfo = document.getElementById('userInfo');
        if (userInfo) {
            const roleText = this.role === 'ADMIN' ? 'Quản trị viên' :
                this.role === 'STAFF' ? 'Nhân viên' : 'Khách';
            userInfo.textContent = `Chào, ${roleText}`;
        }
    },

    applyRolePermissions() {
        const style = document.createElement('style');
        let css = '';

        if (this.role === 'ADMIN') {
            css += `
                .staff-only { display: none !important; }
                .btnDeleteMuaBan, .btnDeleteResident { display: inline-block !important; }
            `;
        } else if (this.role === 'STAFF') {
            css += `
                .admin-only { display: none !important; }
                .btn-delete-protected { display: inline-block !important; }
            `;
        } else {
            css += `
                .admin-only, .staff-only, .btn-delete-protected, .btnDeleteMuaBan, .btnDeleteResident {
                    display: none !important;
                }
            `;
        }

        style.innerHTML = css;
        document.head.appendChild(style);
    },

    // Kiểm tra quyền
    isAdmin() {
        return this.role === 'ADMIN';
    },

    isStaff() {
        return this.role === 'STAFF';
    },

    hasRole(role) {
        return this.role === role;
    }
};

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { ApiClient, UIManager };
}