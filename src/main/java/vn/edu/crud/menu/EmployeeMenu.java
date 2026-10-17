package vn.edu.crud.menu;

import vn.edu.crud.dao.EmployeeDAO;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.EmployeeRole;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class EmployeeMenu {
    private final Scanner sc = new Scanner(System.in);
    private final EmployeeDAO dao = new EmployeeDAO();

    // Định dạng tiền tệ VNĐ cho đẹp (VD: 5.500.000)
    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          QUẢN LÝ NHÂN VIÊN           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa (Nhập Mã NV)                 ║");
            System.out.println("║  5. Xóa (Nhập Mã NV)                 ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. NV đang phụ trách đơn            ║");
            System.out.println("║  7. NV nhiều đơn nhất                ║");
            System.out.println("║  8. Tính lương tháng + Hoa hồng      ║"); // MỚI
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: timTheoMa(); break;
                case 3: themNhanVien(); break;
                case 4: suaNhanVien(); break;
                case 5: xoaNhanVien(); break;
                case 6: inBang(dao.nhanVienCoDonHang()); break;
                case 7: inBang(dao.nhanVienNhieuDonNhat()); break;
                case 8: tinhLuongNhanVien(); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themNhanVien() {
        String id = dao.sinhMaNVMoi();
        System.out.println("Mã NV tự sinh: " + id);

        System.out.print("Tên NV: ");
        String ten = sc.nextLine();

        // --- CHỌN CHỨC VỤ ---
        EmployeeRole[] dsRole = EmployeeRole.values();
        System.out.println("\n--- CHỌN CHỨC VỤ ---");
        for (int i = 0; i < dsRole.length; i++) {
            System.out.printf("  %d. %-15s (Lương tháng: %s)\n",
                    (i + 1), dsRole[i].getLabel(), VND.format(dsRole[i].getLuongCoBan()));
        }

        EmployeeRole roleChon = null;
        while (roleChon == null) {
            System.out.print("Nhập số chọn (1-" + dsRole.length + "): ");
            try {
                int chonRole = Integer.parseInt(sc.nextLine());
                if (chonRole >= 1 && chonRole <= dsRole.length) {
                    roleChon = dsRole[chonRole - 1];
                } else {
                    System.out.println("Vui lòng chọn đúng số!");
                }
            } catch (NumberFormatException e) { System.out.println("Vui lòng nhập số!"); }
        }

        System.out.print("SĐT: ");
        String sdt = sc.nextLine();

        // --- GỢI Ý LƯƠNG ---
        System.out.printf("Lương tháng (Enter để nhận mức mặc định %s): ", VND.format(roleChon.getLuongCoBan()));
        String luongStr = sc.nextLine();
        BigDecimal luong;
        if (luongStr.trim().isEmpty()) {
            luong = roleChon.getLuongCoBan();
        } else {
            try { luong = new BigDecimal(luongStr); }
            catch (NumberFormatException e) {
                System.out.println("⚠️ Lỗi định dạng, tự động lấy lương mặc định.");
                luong = roleChon.getLuongCoBan();
            }
        }

        // Lưu vào DB với role là tên Enum (VD: THU_NGAN)
        Employee nv = new Employee(id, ten, roleChon.name(), sdt, luong);
        if (dao.insert(nv)) System.out.println("✅ Thêm nhân viên thành công!");
        else System.out.println("❌ Thêm thất bại!");
    }

    private void tinhLuongNhanVien() {
        System.out.print("Nhập Mã NV cần tính lương (VD: NV02): ");
        String id = sc.nextLine().trim().toUpperCase();

        Employee nv = dao.findById(id);
        if (nv == null) {
            System.out.println("❌ Không tìm thấy nhân viên có mã: " + id);
            return;
        }

        BigDecimal luongCung = nv.getSalary();
        BigDecimal doanhThu = dao.tinhDoanhThuNhanVien(id);
        BigDecimal tyLeHoaHong = new BigDecimal("0.02"); // 2% hoa hồng
        BigDecimal tienHoaHong = doanhThu.multiply(tyLeHoaHong);
        BigDecimal tongLinh = luongCung.add(tienHoaHong);

        System.out.println("\n--- BẢNG TÍNH LƯƠNG THÁNG ---");
        System.out.println("Mã NV      : " + nv.getEmployeeId());
        System.out.println("Tên NV     : " + nv.getName());
        System.out.println("Chức vụ    : " + EmployeeRole.getLabelFromString(nv.getRole()));
        System.out.println("--------------------------------------");
        System.out.printf("Lương cứng : %s\n", VND.format(luongCung));
        System.out.printf("Doanh thu  : %s\n", VND.format(doanhThu));
        System.out.printf("Hoa hồng   : %s (2%% doanh thu)\n", VND.format(tienHoaHong));
        System.out.println("--------------------------------------");
        System.out.printf("TỔNG LĨNH  : %s\n", VND.format(tongLinh));
    }

    private void timTheoMa() {
        System.out.print("Nhập Mã NV cần tìm (VD: NV01): ");
        String id = sc.nextLine().trim().toUpperCase();
        Employee nv = dao.findById(id);
        if (nv == null) {
            System.out.println("❌ Không tìm thấy nhân viên có mã: " + id);
        } else {
            System.out.println("✅ Thông tin nhân viên:");
            System.out.printf("%-8s | %-25s | %-15s | %-15s | %-15s%n", "Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương");
            System.out.println("-".repeat(85));
            String roleHienThi = EmployeeRole.getLabelFromString(nv.getRole());
            System.out.printf("%-8s | %-25s | %-15s | %-15s | %-15s%n",
                    nv.getEmployeeId(), nv.getName(), roleHienThi, nv.getPhone(), VND.format(nv.getSalary()));
        }
    }

    private void suaNhanVien() {
        System.out.print("Nhập Mã NV cần sửa (VD: NV01): ");
        String id = sc.nextLine().trim().toUpperCase();
        Employee nv = dao.findById(id);
        if (nv == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.println("\n--- THÔNG TIN HIỆN TẠI ---");
        System.out.println("Tên: " + nv.getName() + " | Chức vụ: " + EmployeeRole.getLabelFromString(nv.getRole()) + " | Lương: " + VND.format(nv.getSalary()));

        System.out.print("Tên mới (bỏ trống để giữ nguyên): ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) nv.setName(ten);

        System.out.print("Bạn có muốn đổi chức vụ không? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            EmployeeRole[] dsRole = EmployeeRole.values();
            for (int i = 0; i < dsRole.length; i++) {
                System.out.printf("  %d. %s\n", (i + 1), dsRole[i].getLabel());
            }
            System.out.print("Chọn số: ");
            try {
                int chonRole = Integer.parseInt(sc.nextLine());
                if (chonRole >= 1 && chonRole <= dsRole.length) {
                    nv.setRole(dsRole[chonRole - 1].name());
                }
            } catch (Exception e) { System.out.println("Giữ nguyên chức vụ cũ."); }
        }

        System.out.print("SĐT mới (bỏ trống để giữ nguyên): ");
        String sdt = sc.nextLine();
        if (!sdt.isEmpty()) nv.setPhone(sdt);

        System.out.print("Lương mới (bỏ trống để giữ nguyên): ");
        String luongStr = sc.nextLine();
        if (!luongStr.isEmpty()) {
            try { nv.setSalary(new BigDecimal(luongStr)); }
            catch (Exception e) { System.out.println("Lỗi định dạng lương, giữ nguyên mức cũ."); }
        }

        if (dao.update(nv)) System.out.println("✅ Sửa thành công!");
        else System.out.println("❌ Sửa thất bại!");
    }

    private void xoaNhanVien() {
        System.out.print("Nhập Mã NV cần xóa (VD: NV02): ");
        String id = sc.nextLine().trim().toUpperCase();
        Employee nv = dao.findById(id);
        if (nv == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.println("--- THÔNG TIN NHÂN VIÊN SẮP XÓA ---");
        System.out.println("Tên NV : " + nv.getName());
        System.out.println("Chức vụ: " + EmployeeRole.getLabelFromString(nv.getRole()));

        if (dao.kiemTraNhanVienCoDon(id)) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! Nhân viên này đã phụ trách Đơn hàng.");
            return;
        }
        System.out.print("\nBạn có chắc chắn muốn xóa? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    private void inDanhSach(List<Employee> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-8s | %-25s | %-15s | %-15s | %-15s%n", "Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương/Tháng");
        System.out.println("-".repeat(85));
        for (Employee e : list) {
            String tenHienThi = EmployeeRole.getLabelFromString(e.getRole());
            System.out.printf("%-8s | %-25s | %-15s | %-15s | %-15s%n",
                    e.getEmployeeId(), e.getName(), tenHienThi, e.getPhone(), VND.format(e.getSalary()));
        }
    }

    private void inBang(List<Object[]> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        for (Object[] row : list) {
            StringBuilder sb = new StringBuilder();
            for (Object o : row) sb.append(o).append(" | ");
            System.out.println(sb);
        }
    }
}