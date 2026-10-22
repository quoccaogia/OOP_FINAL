package vn.edu.crud.menu;


import java.util.ArrayList;
import vn.edu.crud.dao.FlowerBatchDAO;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.dao.SupplierDAO;
import vn.edu.crud.model.Flower;
import vn.edu.crud.util.AnToan;
import vn.edu.crud.util.NhapLieu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class FlowerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FlowerDAO dao = new FlowerDAO();
    private final FlowerBatchDAO batchDAO = new FlowerBatchDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public void hienThi() {
        int chon;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          QUẢN LÝ HOA                 ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  ── THAO TÁC ──                      ║");
            System.out.println("║  1. Xem tất cả                       ║");
            System.out.println("║  2. Tìm theo mã                      ║");
            System.out.println("║  3. Thêm mới                         ║");
            System.out.println("║  4. Sửa                              ║");
            System.out.println("║  5. Xóa                              ║");
            System.out.println("║  ── BÁO CÁO ──                       ║");
            System.out.println("║  6. Hoa tồn < 10                     ║");
            System.out.println("║  7. Tìm hoa theo tên / danh mục      ║");
            System.out.println("║  8. Hoa theo khoảng giá              ║");
            System.out.println("║  9. Hoa theo màu                     ║");
            System.out.println("║ 10. Hoa bán chạy nhất                ║");
            System.out.println("║ 11. Hoa bán chậm nhất                ║");
            System.out.println("║ 12. Hoa giá giảm dần                 ║");
            System.out.println("║ 13. Tổng bán theo từng loại          ║");
            System.out.println("║ 14. Hoa chưa từng bán                ║");
            System.out.println("║ 15. Lô hoa tồn > 7 ngày              ║");
            System.out.println("║  0. Quay lại                         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Chọn: ");

            try { chon = Integer.parseInt(sc.nextLine()); }
            catch (NumberFormatException e) { chon = -1; }

            try {
                xulyChon(chon);
            } catch (Exception e) {
                AnToan.baoLoi(e, "số trong menu 0-15. VD: 1 = xem danh sách, 3 = thêm hoa");
            }
        } while (chon != 0);
    }

    private void xulyChon(int chon) {
        switch (chon) {
            case 1:
                List<Flower> tatCa = dao.findAll();
                inDsHoa(tatCa);
                // Sử dụng Stream và Lambda để tính tổng tồn kho
                int tongTon = tatCa.stream().mapToInt(f -> f.getStockQuantity()).sum();
                System.out.println("👉 Tổng cộng: " + tatCa.size()
                        + " loài hoa trong hệ thống — tổng tồn kho: " + tongTon + " bông.");
                break;
            case 2:
                System.out.print("Mã hoa (VD: FL01): ");
                String maHoa = sc.nextLine().trim().toUpperCase();
                Flower f = dao.timTheoMa(maHoa);

                if (f != null) {
                    System.out.println("✅ Thông tin hoa:");
                    List<Flower> ketQua = new ArrayList<>();
                    ketQua.add(f);
                    inDsHoa(ketQua);
                } else {
                    System.out.println("❌ Không tìm thấy hoa có mã: " + maHoa);
                }
                break;
            case 3:
                themHoa();
                break;
            case 4:
                suaHoa();
                break;
            case 5:
                xoaHoa();
                break;
            case 6:
                // Lambda lọc hoa tồn kho < 10

                inDsHoa(dao.hoaSapHetLambda(10));
                break;
            case 7:
                System.out.print("Từ khóa tìm kiếm (Tên hoặc Danh mục): ");
                // Lambda tìm kiếm hoa theo tên hoặc danh mục
                inDsHoa(dao.searchFlowersByLambda(sc.nextLine()));
                break;
            case 8:
                BigDecimal min = NhapLieu.tien(sc, "Giá min", "VD: 5000", true);
                BigDecimal max = NhapLieu.tien(sc, "Giá max", "VD: 150000", true);
                if (min.compareTo(max) > 0) {
                    System.out.println("⚠️ Giá min lớn hơn giá max — mình tự đổi lại cho bạn.");
                    BigDecimal tam = min;
                    min = max;
                    max = tam;
                }
                // Lambda lọc hoa theo khoảng giá

                inDsHoa(dao.timTheoKhoangGiaLambda(min, max));
                break;
            case 9:
                System.out.print("Màu (VD: Đỏ): ");
                inDsHoa(dao.timTheoMau(sc.nextLine()));
                break;
            case 10:
                inBang(dao.hoaBanChay());
                break;
            case 11:
                inBang(dao.hoaBanCham());
                break;
            case 12:
                // Lambda sắp xếp hoa giá giảm dần

                inDsHoa(dao.hoaGiaGiamDanLambda());
                break;
            case 13:
                inBang(dao.tongBanTheoHoa());
                break;
            case 14:
                inDsHoa(dao.hoaChuaBanDuoc());
                break;
            case 15:
                inBang(dao.loHoaTonLau(7));
                break;
            case 0:
                break;
            default:
                System.out.println("Chọn sai! Nhập số từ 0 đến 15.");
        }
    }

    private void themHoa() {
        try {
            String id = dao.sinhMaHoaMoi();
            System.out.println("Mã tự sinh: " + id);
            String name = NhapLieu.batBuoc(sc, "Tên hoa", "VD: Hồng Đỏ Đà Lạt");
            String cat = NhapLieu.batBuoc(sc, "Danh mục", "VD: Hồng");
            BigDecimal price = NhapLieu.tien(sc, "Đơn giá bán", "VD: 5000", false);
            int stock = NhapLieu.soNguyen(sc, "Tồn kho ban đầu", "VD: 100", 0, 1_000_000);
            String color = NhapLieu.batBuoc(sc, "Màu sắc", "VD: Đỏ");
            String supId = nhapMaNCC(null);
            System.out.println(dao.insert(id, name, cat, price, stock, color, supId)
                    ? "✅ Thêm thành công!"
                    : "❌ Thêm thất bại (có thể trùng mã hoa hoặc mã NCC không hợp lệ).");
        } catch (Exception e) {
            AnToan.baoLoi(e, "tên/danh mục/màu là chữ, giá là số > 0, tồn kho là số ≥ 0, mã NCC dạng NCC01");
        }
    }

    private void suaHoa() {
        try {
            System.out.print("Mã hoa cần sửa (VD: FL01): ");
            String id = sc.nextLine().trim().toUpperCase();
            Flower cu = dao.timTheoMa(id);
            if (cu == null) {
                System.out.println("❌ Không tìm thấy hoa mã " + id + "!");
                return;
            }
            if (batchDAO.coLo(id)) {
                System.out.println("⚠️ Hoa này đang có lô — tồn kho được tính theo số còn trong lô.");
                System.out.println("   Sửa 'Tồn kho' ở đây sẽ làm lệch với lô (nên sửa qua nhập hàng / thanh lý lô).");
            }
            String name = NhapLieu.batBuoc(sc, "Tên mới", "VD: Hồng Đỏ Đà Lạt");
            String cat = NhapLieu.batBuoc(sc, "Danh mục mới", "VD: Hồng");
            BigDecimal price = NhapLieu.tien(sc, "Đơn giá mới", "VD: 5000", false);
            int stock = NhapLieu.soNguyen(sc, "Tồn kho mới", "VD: 100", 0, 1_000_000);
            String color = NhapLieu.batBuoc(sc, "Màu mới", "VD: Đỏ");
            String supId = nhapMaNCC(cu.getSupplierId());
            System.out.println(dao.update(id, name, cat, price, stock, color, supId)
                    ? "✅ Sửa thành công!"
                    : "❌ Sửa thất bại (mã hoa không tồn tại hoặc dữ liệu không hợp lệ).");
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã hoa dạng FL01, giá là số > 0, tồn kho là số ≥ 0, mã NCC dạng NCC01");
        }
    }

    // Nhập mã NCC: Enter = giữ nguyên (khi sửa) / chưa có (khi thêm); nếu nhập thì mã phải tồn tại
    private String nhapMaNCC(String hienTai) {
        while (true) {
            String goiY = (hienTai == null || hienTai.isEmpty())
                    ? "VD: NCC01, Enter = chưa có"
                    : "Enter = giữ nguyên " + hienTai + ", VD: NCC01";
            System.out.print("Mã NCC (" + goiY + "): ");
            String ma = sc.nextLine().trim().toUpperCase();
            if (ma.isEmpty()) return hienTai;
            if (supplierDAO.findById(ma) != null) return ma;
            System.out.println("⚠️ Không có NCC mã " + ma + ". Xem danh sách ở menu Nhà cung cấp.");
        }
    }

    private void xoaHoa() {
        try {
            System.out.print("Mã hoa cần xóa (VD: FL01): ");
            String id = sc.nextLine().trim().toUpperCase();

            if (dao.timTheoMa(id) == null) {
                System.out.println("❌ Không tìm thấy hoa mã " + id + "!");
                return;
            }

            System.out.print("Xác nhận xóa " + id + "? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println(dao.delete(id)
                        ? "✅ Đã xóa!"
                        : "❌ Không xóa được: hoa đã có đơn hàng / lô hoa / lịch chăm sóc.");
            }
        } catch (Exception e) {
            AnToan.baoLoi(e, "mã hoa dạng FL01, VD: FL03");
        }
    }

    private void inDsHoa(List<Flower> list) {
        if (list.isEmpty()) { System.out.println("Không có dữ liệu."); return; }
        System.out.printf("%-8s | %-32s | %-16s | %-12s | %-6s | %-8s%n",
                "Mã", "Tên", "Danh mục", "Giá", "Tồn", "Màu");
        System.out.println("-".repeat(95));
        for (Flower f : list) {
            System.out.printf("%-8s | %-32s | %-16s | %-12s | %-6d | %-8s%n",
                    f.getFlowerId(), f.getName(), f.getCategory(),
                    f.getUnitPrice(), f.getStockQuantity(), f.getColor());
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