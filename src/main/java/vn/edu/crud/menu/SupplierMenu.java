package vn.edu.crud.menu;

import vn.edu.crud.dao.SupplierDAO;
import vn.edu.crud.dao.PurchaseOrderDAO;
import vn.edu.crud.model.Supplier;

import java.util.List;
import java.util.Scanner;

public class SupplierMenu {
    private final Scanner sc = new Scanner(System.in);
    private final SupplierDAO dao = new SupplierDAO();
    private final PurchaseOrderDAO poDAO = new PurchaseOrderDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         QUẢN LÝ NHÀ CUNG CẤP         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả danh sách             ║");
            System.out.println("║  2. Nghiệp vụ (Tiếp nhận NCC)        ║");
            System.out.println("║  3. Xóa nhà cung cấp                 ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Tổng tiền nhập theo NCC          ║");
            System.out.println("║  7. NCC đang cung cấp hoa            ║");
            System.out.println("║  8. NCC cung cấp nhiều loại hoa nhất ║");
            System.out.println("║  9. Thống kê loại hoa theo NCC       ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            switch (chon) {
                case 1: inDanhSach(dao.findAll()); break;
                case 2: tiepNhanNhaCungCap(); break;
                case 3: xoaNhaCungCap(); break;
                case 6: inBang(dao.tongNhapTheoNCC()); break;
                case 7: inBang(dao.nccDangCungCapHoa()); break;
                case 8: inBang(dao.nccNhieuLoaiHoaNhat()); break;
                case 9: inBang(dao.thongKeLoaiHoaTheoNCC()); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    // =====================================================
    // CHỨC NĂNG 2: NGHIỆP VỤ TIẾP NHẬN NCC (CHECK SĐT)
    // =====================================================
    private void tiepNhanNhaCungCap() {
        System.out.println("\n--- TIẾP NHẬN NHÀ CUNG CẤP ---");
        System.out.print("Nhập SĐT nhà cung cấp: ");
        String sdt = dao.chuanHoaSdt(sc.nextLine());

        List<Supplier> ketQua = dao.timTheoSdt(sdt);

        if (ketQua.isEmpty()) {
            System.out.println("❌ Không tìm thấy NCC nào dùng SĐT này.");
            System.out.print("👉 Tạo hồ sơ nhà cung cấp mới? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                themNhaCungCapMoi(sdt);
            }
        } else {
            System.out.println("✅ Tìm thấy nhà cung cấp:");
            inDanhSach(ketQua);

            Supplier ncc = ketQua.get(0);
            System.out.print("👉 Bạn có muốn cập nhật (sửa) thông tin NCC này không? (y/n): ");

            if (sc.nextLine().equalsIgnoreCase("y")) {
                suaNhaCungCap(ncc);
            }
        }
    }

    // =====================================================
    // CHỨC NĂNG 3: XÓA NHÀ CUNG CẤP
    // =====================================================
    private void xoaNhaCungCap() {
        System.out.println("\n--- XÓA NHÀ CUNG CẤP ---");
        System.out.print("Nhập Mã NCC cần xóa (VD: NCC01): ");
        String id = sc.nextLine().trim().toUpperCase();

        Supplier ncc = dao.findById(id);
        if (ncc == null) {
            System.out.println("❌ Không tìm thấy NCC có mã: " + id);
            return;
        }

        System.out.println("\n--- THÔNG TIN NHÀ CUNG CẤP ---");
        System.out.println("Tên NCC : " + ncc.getName());
        System.out.println("SĐT     : " + ncc.getPhone());

        // Kiểm tra xem NCC này đã có phiếu nhập hàng nào chưa
        if (!poDAO.phieuNhapCuaNCC(id).isEmpty()) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! NCC này đã có Phiếu Nhập Hàng trong hệ thống.");
            return;
        }
        // Còn loại hoa nào đang dùng NCC này không (báo cáo quy định RESTRICT)
        if (dao.coHoa(id)) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! NCC này đang cung cấp loại hoa trong danh mục.");
            System.out.println("👉 Chuyển các loại hoa đó sang NCC khác (menu Hoa → [4] Sửa) rồi hãy xóa.");
            return;
        }

        System.out.print("\nBạn có chắc chắn muốn xóa NCC này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        } else {
            System.out.println("Đã hủy thao tác.");
        }
    }

    // =====================================================
    // CÁC HÀM HỖ TRỢ
    // =====================================================
    private void themNhaCungCapMoi(String sdtDaNhap) {
        String id = dao.sinhMaNCCMoi();
        System.out.println("Mã NCC tự sinh: " + id);

        System.out.print("Tên NCC: ");
        String ten = sc.nextLine();
        System.out.print("Địa chỉ: ");
        String diaChi = sc.nextLine();
        System.out.print("Người đại diện: ");
        String daiDien = sc.nextLine();

        Supplier ncc = new Supplier(id, ten, sdtDaNhap, diaChi, daiDien);
        if (dao.insert(ncc)) System.out.println("✅ Thêm nhà cung cấp thành công!");
    }

    private void suaNhaCungCap(Supplier ncc) {
        System.out.println("\n--- SỬA THÔNG TIN NHÀ CUNG CẤP ---");
        System.out.print("Tên mới (bỏ trống để giữ nguyên): ");
        String ten = sc.nextLine();
        if (!ten.isEmpty()) ncc.setName(ten);

        System.out.print("Địa chỉ mới (bỏ trống để giữ nguyên): ");
        String diaChi = sc.nextLine();
        if (!diaChi.isEmpty()) ncc.setAddress(diaChi);

        System.out.print("Người đại diện mới (bỏ trống để giữ nguyên): ");
        String daiDien = sc.nextLine();
        if (!daiDien.isEmpty()) ncc.setRepresentative(daiDien);

        if (dao.update(ncc)) System.out.println("✅ Sửa thành công!");
    }

    // =====================================================
    // HELPER IN BẢNG
    // =====================================================
    private void inDanhSach(List<Supplier> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-8s | %-25s | %-15s | %-25s | %-20s%n", "Mã NCC", "Tên NCC", "SĐT", "Địa Chỉ", "Người Đại Diện");
        System.out.println("-".repeat(100));
        for (Supplier s : list) {
            System.out.printf("%-8s | %-25s | %-15s | %-25s | %-20s%n",
                    s.getSupplierId(), s.getName(), s.getPhone(), s.getAddress(), s.getRepresentative());
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