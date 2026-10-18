package vn.edu.crud.menu;


import java.util.ArrayList;
import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class FlowerMenu {
    private final Scanner sc = new Scanner(System.in);
    private final FlowerDAO dao = new FlowerDAO();

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
            System.out.println("║  7. Tìm hoa theo tên                 ║");
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

            switch (chon) {
                case 1: inDsHoa(dao.findAll()); break;
                case 2:
                    System.out.print("Mã hoa: ");
                    String maHoa = sc.nextLine().toUpperCase();
                    Flower f = dao.timTheoMa(maHoa);

                    if (f != null) {
                        System.out.println("✅ Thông tin hoa:");
                        // Tạo một List tạm chứa 1 bông hoa này để tái sử dụng hàm in bảng
                        List<Flower> ketQua = new ArrayList<>();
                        ketQua.add(f);
                        inDsHoa(ketQua);
                    } else {
                        System.out.println("❌ Không tìm thấy hoa có mã: " + maHoa);
                    }
                    break;
                case 3: themHoa(); break;
                case 4: suaHoa(); break;
                case 5: xoaHoa(); break;
                case 6: inDsHoa(dao.hoaSapHet(10)); break;
                case 7:
                    System.out.print("Từ khóa: ");
                    inDsHoa(dao.timTheoTen(sc.nextLine()));
                    break;
                case 8:
                    System.out.print("Giá min: "); BigDecimal min = new BigDecimal(sc.nextLine());
                    System.out.print("Giá max: "); BigDecimal max = new BigDecimal(sc.nextLine());
                    inDsHoa(dao.timTheoGia(min, max));
                    break;
                case 9:
                    System.out.print("Màu: ");
                    inDsHoa(dao.timTheoMau(sc.nextLine()));
                    break;
                case 10: inBang(dao.hoaBanChay()); break;
                case 11: inBang(dao.hoaBanCham()); break;
                case 12: inDsHoa(dao.hoaGiaGiamDan()); break;
                case 13: inBang(dao.tongBanTheoHoa()); break;
                case 14: inDsHoa(dao.hoaChuaBanDuoc()); break;
                case 15: inBang(dao.loHoaTonLau(7)); break;
                case 0: break;
                default: System.out.println("Chọn sai!");
            }
        } while (chon != 0);
    }

    private void themHoa() {
        String id = dao.sinhMaHoaMoi();
        System.out.println("Mã tự sinh: " + id);
        System.out.print("Tên: ");      String name  = sc.nextLine();
        System.out.print("Danh mục: "); String cat   = sc.nextLine();
        System.out.print("Đơn giá: ");  BigDecimal price = new BigDecimal(sc.nextLine());
        System.out.print("Tồn kho: ");  int stock = Integer.parseInt(sc.nextLine());
        System.out.print("Màu: ");      String color = sc.nextLine();
        System.out.print("Mã NCC: ");   String supId = sc.nextLine();
        System.out.println(dao.insert(id, name, cat, price, stock, color, supId)
                ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void suaHoa() {
        System.out.print("Mã hoa cần sửa: ");
        String id = sc.nextLine();
        if (dao.timTheoMa(id) == null) { System.out.println("❌ Không tìm thấy!"); return; }
        System.out.print("Tên mới: ");      String name  = sc.nextLine();
        System.out.print("Danh mục mới: "); String cat   = sc.nextLine();
        System.out.print("Đơn giá mới: ");  BigDecimal price = new BigDecimal(sc.nextLine());
        System.out.print("Tồn kho mới: ");  int stock = Integer.parseInt(sc.nextLine());
        System.out.print("Màu mới: ");      String color = sc.nextLine();
        System.out.print("Mã NCC mới: ");   String supId = sc.nextLine();
        System.out.println(dao.update(id, name, cat, price, stock, color, supId)
                ? "✅ Sửa thành công!" : "❌ Thất bại!");
    }

    private void xoaHoa() {
        System.out.print("Mã hoa cần xóa: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "✅ Đã xóa!" : "❌ Thất bại!");
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