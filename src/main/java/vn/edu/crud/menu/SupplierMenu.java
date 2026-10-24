package vn.edu.crud.menu;

import vn.edu.crud.util.InBangHelper;

import java.util.List;
import java.util.Scanner;

import vn.edu.crud.service.PurchaseOrderService;
import vn.edu.crud.service.SupplierService;
import vn.edu.crud.model.Supplier;
import vn.edu.crud.util.NhapLieu;

public class SupplierMenu {
    private final Scanner sc = new Scanner(System.in);
    private final SupplierService supplierService = new SupplierService();
    private final PurchaseOrderService poService = new PurchaseOrderService();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         QUẢN LÝ NHÀ CUNG CẤP         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Xem tất cả danh sách             ║");
            System.out.println("║  2. Nghiệp vụ (Tiếp nhận NCC)        ║");
            System.out.println("║  3. Tìm kiếm nhà cung cấp            ║");
            System.out.println("║  4. Xóa nhà cung cấp                 ║");
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
                case 1 -> inDanhSach(supplierService.findAll());
                case 2 -> tiepNhanNhaCungCap();
                case 3 -> timKiemNhaCungCapLambda();
                case 4 -> xoaNhaCungCap();
                case 6 -> InBangHelper.inBang(supplierService.tongNhapTheoNCC(), new String[]{"Mã NCC", "Tên NCC", "Tổng Tiền Nhập"});
                case 7 -> InBangHelper.inBang(supplierService.nccDangCungCapHoa(), new String[]{"Mã NCC", "Tên NCC", "SĐT", "Địa Chỉ", "Người Đại Diện"});
                case 8 -> InBangHelper.inBang(supplierService.nccNhieuLoaiHoaNhat(), new String[]{"Mã NCC", "Tên NCC", "Số Loài Hoa"});
                case 9 -> InBangHelper.inBang(supplierService.thongKeLoaiHoaTheoNCC(), new String[]{"Mã NCC", "Tên NCC", "Số Loài Hoa"});
                case 0 -> {
                }
                default -> System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }
    // =========================================================================
// CHỨC NĂNG TÌM KIẾM NHÀ CUNG CẤP BẰNG LAMBDA (MỤC 2)
// =========================================================================
private void timKiemNhaCungCapLambda() {
    System.out.println("\n--- TÌM KIẾM NHÀ CUNG CẤP (SỬ DỤNG LAMBDA) ---");
    System.out.print("Nhập tên nhà cung cấp cần tìm: ");
    String tuKhoa = sc.nextLine();

    // Gọi hàm tìm kiếm bằng Lambda đã viết trong SupplierService
    List<Supplier> ketQua = supplierService.searchSuppliersByLambda(tuKhoa);

    if (ketQua.isEmpty()) {
        System.out.println("❌ Không tìm thấy nhà cung cấp nào có tên khớp với: \"" + tuKhoa + "\"");
    } else {
        System.out.println("✅ Tìm thấy " + ketQua.size() + " nhà cung cấp phù hợp:");
        // Sử dụng lại hàm inDanhSach có sẵn để in bảng kết quả đẹp mắt
        inDanhSach(ketQua);
    }
}
    // =====================================================
    // CHỨC NĂNG 2: NGHIỆP VỤ TIẾP NHẬN NCC (CHECK SĐT)
    // =====================================================
    private void tiepNhanNhaCungCap() {
        System.out.println("\n--- TIẾP NHẬN NHÀ CUNG CẤP ---");
        System.out.print("Nhập SĐT nhà cung cấp (VD: 0263388899, có thể nhập một phần): ");
        String sdt = supplierService.chuanHoaSdt(sc.nextLine());

        List<Supplier> ketQua = supplierService.timTheoSdt(sdt);

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

        Supplier ncc = supplierService.findById(id);
        if (ncc == null) {
            System.out.println("❌ Không tìm thấy NCC có mã: " + id);
            return;
        }

        System.out.println("\n--- THÔNG TIN NHÀ CUNG CẤP ---");
        System.out.println("Tên NCC : " + ncc.getName());
        System.out.println("SĐT     : " + ncc.getPhone());

        // Kiểm tra xem NCC này đã có phiếu nhập hàng nào chưa
        if (!poService.phieuNhapCuaNCC(id).isEmpty()) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! NCC này đã có Phiếu Nhập Hàng trong hệ thống.");
            return;
        }
        // Còn loại hoa nào đang dùng NCC này không (báo cáo quy định RESTRICT)
        if (supplierService.coHoa(id)) {
            System.out.println("\n❌ KHÔNG THỂ XÓA! NCC này đang cung cấp loại hoa trong danh mục.");
            System.out.println("👉 Chuyển các loại hoa đó sang NCC khác (menu Hoa → [4] Sửa) rồi hãy xóa.");
            return;
        }

        System.out.print("\nBạn có chắc chắn muốn xóa NCC này? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(supplierService.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
        } else {
            System.out.println("Đã hủy thao tác.");
        }
    }

    // =====================================================
    // CÁC HÀM HỖ TRỢ
    // =====================================================
    private void themNhaCungCapMoi(String sdtDaNhap) {
        String id = supplierService.sinhMaNCCMoi();
        System.out.println("Mã NCC tự sinh: " + id);

        String sdt = (sdtDaNhap == null || sdtDaNhap.isEmpty() || !NhapLieu.laSdtNhaCungCap(sdtDaNhap))
                ? NhapLieu.sdtNhaCungCap(sc, "SĐT NCC")
                : sdtDaNhap;
        String ten = NhapLieu.batBuoc(sc, "Tên NCC", "VD: Vựa Hoa Đà Lạt");
        String diaChi = NhapLieu.tuyChon(sc, "Địa chỉ", "VD: Đà Lạt, Lâm Đồng");
        String daiDien = NhapLieu.tuyChon(sc, "Người đại diện", "VD: Ông Bảy");

        Supplier ncc = new Supplier(id, ten, sdt, diaChi, daiDien);
        if (supplierService.insert(ncc)) System.out.println("✅ Thêm nhà cung cấp thành công!");
    }

    private void suaNhaCungCap(Supplier ncc) {
        System.out.println("\n--- SỬA THÔNG TIN NHÀ CUNG CẤP ---");
        System.out.print("Tên mới (Enter = giữ nguyên, VD: Vựa Hoa Đà Lạt): ");
        String ten = sc.nextLine().trim();
        if (!ten.isEmpty()) ncc.setName(ten);

        System.out.print("Địa chỉ mới (Enter = giữ nguyên, VD: Đà Lạt, Lâm Đồng): ");
        String diaChi = sc.nextLine().trim();
        if (!diaChi.isEmpty()) ncc.setAddress(diaChi);

        System.out.print("Người đại diện mới (Enter = giữ nguyên, VD: Ông Bảy): ");
        String daiDien = sc.nextLine().trim();
        if (!daiDien.isEmpty()) ncc.setRepresentative(daiDien);

        if (supplierService.update(ncc)) System.out.println("✅ Sửa thành công!");
    }

    // =====================================================
    // HELPER IN BẢNG
    // =====================================================
    private void inDanhSach(List<Supplier> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        List<Object[]> rows = list.stream().map(s -> new Object[]{
            s.getSupplierId(), s.getName(), s.getPhone(),
            s.getAddress() == null ? "" : s.getAddress(),
            s.getRepresentative() == null ? "" : s.getRepresentative()
        }).toList();
        InBangHelper.inBang(new String[]{"Mã NCC", "Tên NCC", "SĐT", "Địa Chỉ", "Người Đại Diện"}, rows);
    }

    private void inBang(List<Object[]> list) {
        InBangHelper.inBang(list, null);
    }
}