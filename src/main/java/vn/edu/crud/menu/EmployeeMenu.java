package vn.edu.crud.menu;

import vn.edu.crud.util.InBangHelper;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import vn.edu.crud.service.EmployeeService;
import vn.edu.crud.model.Employee;
import vn.edu.crud.model.EmployeeRole;
import vn.edu.crud.util.NhapLieu;

public class EmployeeMenu {
    private final Scanner sc = new Scanner(System.in);
    private final EmployeeService employeeService = new EmployeeService();

    // Định dạng tiền tệ VNĐ cho đẹp (VD: 5.500.000)
    private final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          QUẢN LÝ NHÂN VIÊN           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm kiếm nhân viên               ║");
            System.out.println("║  3. Tìm theo mã                      ║");
            System.out.println("║  4. Thêm mới                         ║");
            System.out.println("║  5. Sửa (Nhập Mã NV)                 ║");
            System.out.println("║  6. Xóa (Nhập Mã NV)                 ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  7. NV đang phụ trách đơn            ║");
            System.out.println("║  8. NV nhiều đơn nhất                ║");
            System.out.println("║  9. Tính lương tháng + Hoa hồng      ║"); // MỚI
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1 -> inDanhSach(employeeService.findAll());
                case 2 -> timKiemNhanVienLambda();
                case 3 -> timTheoMa();
                case 4 -> themNhanVien();
                case 5 -> suaNhanVien();
                case 6 -> xoaNhanVien();
                case 7 -> InBangHelper.inBang(employeeService.nhanVienCoDonHang(), new String[]{"Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương"});
                case 8 -> InBangHelper.inBang(employeeService.nhanVienNhieuDonNhat(), new String[]{"Mã NV", "Tên NV", "Số Lượng Đơn"});
                case 9 -> tinhLuongNhanVien();
                case 0 -> {
                }
                default -> System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

// =========================================================================
// CHỨC NĂNG TÌM KIẾM NHÂN VIÊN BẰNG LAMBDA (MỤC 3)
// =========================================================================
private void timKiemNhanVienLambda() {
    System.out.println("\n--- TÌM KIẾM NHÂN VIÊN (SỬ DỤNG LAMBDA) ---");
    System.out.print("Nhập mã hoặc tên nhân viên cần tìm: ");
    String tuKhoa = sc.nextLine();

    // Gọi hàm filter/search bằng Lambda đã viết trong EmployeeService
    List<Employee> ketQua = employeeService.searchEmployeesByLambda(tuKhoa);

    if (ketQua.isEmpty()) {
        System.out.println("❌ Không tìm thấy nhân viên nào khớp với từ khóa: \"" + tuKhoa + "\"");
    } else {
        System.out.println("✅ Tìm thấy " + ketQua.size() + " nhân viên phù hợp:");
        // Sử dụng lại hàm inDanhSach có sẵn trong EmployeeMenu để hiển thị dạng bảng đẹp mắt
        inDanhSach(ketQua);
    }
}

    private void themNhanVien() {
        String id = employeeService.sinhMaNVMoi();
        System.out.println("Mã NV tự sinh: " + id);

        String ten = NhapLieu.batBuoc(sc, "Tên NV", "VD: Nguyễn Văn A");

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

        String sdt = NhapLieu.tuyChon(sc, "SĐT", "VD: 0901234567");

        // --- GỢI Ý LƯƠNG (phải > 0) ---
        BigDecimal luong = roleChon.getLuongCoBan();
        while (true) {
            System.out.printf("Lương tháng (Enter = mặc định %s, VD: 6000000): ",
                    VND.format(roleChon.getLuongCoBan()));
            String s = sc.nextLine().trim();
            if (s.isEmpty()) break;
            try {
                BigDecimal v = new BigDecimal(s);
                if (v.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("⚠️ Lương phải lớn hơn 0.");
                    continue;
                }
                luong = v;
                break;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Phải nhập số. VD: 6000000");
            }
        }

        // Lưu vào DB với role là tên Enum (VD: THU_NGAN)
        Employee nv = new Employee(id, ten, roleChon.name(), sdt, luong);
        if (employeeService.insert(nv)) System.out.println("✅ Thêm nhân viên thành công!");
        else System.out.println("❌ Thêm thất bại!");
    }

    private void tinhLuongNhanVien() {
        System.out.print("Nhập Mã NV cần tính lương (VD: NV02): ");
        String id = sc.nextLine().trim().toUpperCase();

        Employee nv = employeeService.findById(id);
        if (nv == null) {
            System.out.println("❌ Không tìm thấy nhân viên có mã: " + id);
            return;
        }

        BigDecimal luongCung = nv.getSalary();
        BigDecimal doanhThu = employeeService.tinhDoanhThuNhanVien(id);
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
        Employee nv = employeeService.findById(id);
        if (nv == null) {
            System.out.println("❌ Không tìm thấy nhân viên có mã: " + id);
        } else {
            System.out.println("✅ Thông tin nhân viên:");
            String roleHienThi = EmployeeRole.getLabelFromString(nv.getRole());
            List<Object[]> rows = java.util.Collections.singletonList(new Object[]{
                nv.getEmployeeId(), nv.getName(), roleHienThi, nv.getPhone(), nv.getSalary()
            });
            InBangHelper.inBang(new String[]{"Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương"}, rows);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private void suaNhanVien() {
        System.out.print("Nhập Mã NV cần sửa (VD: NV01): ");
        String id = sc.nextLine().trim().toUpperCase();
        Employee nv = employeeService.findById(id);
        if (nv == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.println("\n--- THÔNG TIN HIỆN TẠI ---");
        System.out.println("Tên: " + nv.getName() + " | Chức vụ: " + EmployeeRole.getLabelFromString(nv.getRole()) + " | Lương: " + VND.format(nv.getSalary()));

        System.out.print("Tên mới (Enter = giữ nguyên, VD: Nguyễn Văn A): ");
        String ten = sc.nextLine().trim();
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

        System.out.print("SĐT mới (Enter = giữ nguyên, VD: 0901234567): ");
        String sdt = sc.nextLine().trim();
        if (!sdt.isEmpty()) nv.setPhone(sdt);

        while (true) {
            System.out.print("Lương mới (Enter = giữ nguyên, VD: 6000000): ");
            String luongStr = sc.nextLine().trim();
            if (luongStr.isEmpty()) break;
            try {
                BigDecimal v = new BigDecimal(luongStr);
                if (v.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("⚠️ Lương phải lớn hơn 0.");
                    continue;
                }
                nv.setSalary(v);
                break;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Phải nhập số. VD: 6000000");
            }
        }

        if (employeeService.update(nv)) System.out.println("✅ Sửa thành công!");
        else System.out.println("❌ Sửa thất bại!");
    }

    private void xoaNhanVien() {
        System.out.print("Nhập Mã NV cần xóa (VD: NV02): ");
        String id = sc.nextLine().trim().toUpperCase();
        Employee nv = employeeService.findById(id);
        if (nv == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.println("--- THÔNG TIN NHÂN VIÊN SẮP XÓA ---");
        System.out.println("Tên NV : " + nv.getName());
        System.out.println("Chức vụ: " + EmployeeRole.getLabelFromString(nv.getRole()));

        if (employeeService.kiemTraNhanVienCoDon(id)) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! Nhân viên này đã phụ trách Đơn hàng.");
            return;
        }
        System.out.print("\nBạn có chắc chắn muốn xóa? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(employeeService.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        }
    }

    private void inDanhSach(List<Employee> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        List<Object[]> rows = list.stream().map(e -> new Object[]{
            e.getEmployeeId(), e.getName(), EmployeeRole.getLabelFromString(e.getRole()), e.getPhone(), e.getSalary()
        }).toList();
        InBangHelper.inBang(new String[]{"Mã NV", "Tên NV", "Chức Vụ", "SĐT", "Lương/Tháng"}, rows);
    }

    private void inBang(List<Object[]> list) {
        InBangHelper.inBang(list, null);
    }
}