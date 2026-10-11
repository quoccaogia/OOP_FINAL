package vn.edu.crud.menu;

import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.FlowerSales;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class FlowerMenu {

    private final Scanner sc;
    private final FlowerDAO dao = new FlowerDAO();

    public FlowerMenu(Scanner sc) { this.sc = sc; }

    public void run() {
        while (true) {
            printMenu();
            int choice = readInt("Chọn: ");
            switch (choice) {
                case 1  -> showAll();
                case 2  -> showLowStock();
                case 3  -> searchByName();
                case 4  -> searchById();
                case 5  -> searchByPriceRange();
                case 6  -> showBestSellers();
                case 7  -> searchByColor();
                case 8  -> showSortedByPriceDesc();
                case 9  -> showWorstSellers();
                case 10 -> showSoldQuantity();
                case 11 -> showNeverOrdered();
                case 12 -> addFlower();
                case 13 -> updateFlower();
                case 14 -> deleteFlower();
                case 0  -> { return; }
                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n┌─── QUẢN LÝ HOA ────────────────────────┐");
        System.out.println("│  1.  Xem tất cả hoa                    │");
        System.out.println("│  2.  Hoa sắp hết hàng (< 10)           │");
        System.out.println("│  3.  Tìm hoa theo tên                  │");
        System.out.println("│  4.  Tìm hoa theo mã                   │");
        System.out.println("│  5.  Hoa có giá trong khoảng           │");
        System.out.println("│  6.  Top hoa bán chạy                  │");
        System.out.println("│  7.  Hoa theo màu                      │");
        System.out.println("│  8.  Sắp xếp theo giá giảm dần         │");
        System.out.println("│  9.  Top hoa bán ế                     │");
        System.out.println("│ 10.  Tổng SL đã bán theo loại          │");
        System.out.println("│ 11.  Hoa chưa từng được đặt            │");
        System.out.println("│ 12.  Thêm hoa mới                      │");
        System.out.println("│ 13.  Cập nhật hoa                      │");
        System.out.println("│ 14.  Xoá hoa                           │");
        System.out.println("│  0.  Quay lại                          │");
        System.out.println("└────────────────────────────────────────┘");
    }

    // ════════ CHỨC NĂNG ════════

    private void showAll() {
        System.out.println("\n=== TẤT CẢ HOA ===");
        printList(dao.findAll());
    }

    private void showLowStock() {
        System.out.println("\n=== HOA SẮP HẾT HÀNG ===");
        printList(dao.findLowStock());
    }

    private void searchByName() {
        System.out.print("Nhập từ khoá: ");
        String kw = sc.nextLine();
        System.out.println("\n=== KẾT QUẢ ===");
        printList(dao.findByName(kw));
    }

    private void searchById() {
        System.out.print("Nhập mã hoa: ");
        String id = sc.nextLine();
        Flower f = dao.findById(id);
        if (f == null) System.out.println("❌ Không tìm thấy!");
        else printOne(f);
    }

    private void searchByPriceRange() {
        System.out.print("Giá min: ");
        BigDecimal min = new BigDecimal(sc.nextLine());
        System.out.print("Giá max: ");
        BigDecimal max = new BigDecimal(sc.nextLine());
        printList(dao.findByPriceRange(min, max));
    }

    private void showBestSellers() {
        int n = readInt("Lấy top mấy? ");
        System.out.println("\n=== TOP " + n + " HOA BÁN CHẠY ===");
        for (FlowerSales fs : dao.findBestSellers(n))
            System.out.printf("  %-30s — đã bán: %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());
    }

    private void searchByColor() {
        System.out.print("Nhập màu: ");
        String color = sc.nextLine();
        printList(dao.findByColor(color));
    }

    private void showSortedByPriceDesc() {
        System.out.println("\n=== HOA SẮP XẾP THEO GIÁ GIẢM DẦN ===");
        printList(dao.findAllOrderByPriceDesc());
    }

    private void showWorstSellers() {
        int n = readInt("Lấy top mấy? ");
        System.out.println("\n=== TOP " + n + " HOA BÁN Ế NHẤT ===");
        for (FlowerSales fs : dao.findWorstSellers(n))
            System.out.printf("  %-30s — đã bán: %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());
    }

    private void showSoldQuantity() {
        System.out.println("\n=== TỔNG SL ĐÃ BÁN THEO LOẠI ===");
        for (FlowerSales fs : dao.findSoldQuantityByFlower())
            System.out.printf("  %-30s → %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());
    }

    private void showNeverOrdered() {
        System.out.println("\n=== HOA CHƯA TỪNG ĐƯỢC ĐẶT ===");
        printList(dao.findNeverOrdered());
    }

    private void addFlower() {
        System.out.println("\n=== THÊM HOA MỚI ===");
        Flower f = new Flower();
        System.out.print("Mã hoa: ");    f.setFlowerId(sc.nextLine());
        System.out.print("Tên hoa: ");   f.setName(sc.nextLine());
        System.out.print("Danh mục: ");  f.setCategory(sc.nextLine());
        System.out.print("Đơn giá: ");   f.setUnitPrice(new BigDecimal(sc.nextLine()));
        System.out.print("Số lượng: ");  f.setStockQuantity(readInt(""));
        System.out.print("Màu: ");       f.setColor(sc.nextLine());
        System.out.print("Mã NCC: ");    f.setSupplierId(sc.nextLine());

        System.out.println(dao.insert(f) ? "✅ Thêm thành công!" : "❌ Thất bại!");
    }

    private void updateFlower() {
        System.out.print("Nhập mã hoa cần sửa: ");
        String id = sc.nextLine();
        Flower f = dao.findById(id);
        if (f == null) { System.out.println("❌ Không tìm thấy!"); return; }

        System.out.print("Tên mới (" + f.getName() + "): ");
        String v = sc.nextLine();
        if (!v.isEmpty()) f.setName(v);

        System.out.print("Giá mới (" + f.getUnitPrice() + "): ");
        v = sc.nextLine();
        if (!v.isEmpty()) f.setUnitPrice(new BigDecimal(v));

        System.out.print("Số lượng mới (" + f.getStockQuantity() + "): ");
        v = sc.nextLine();
        if (!v.isEmpty()) f.setStockQuantity(Integer.parseInt(v));

        System.out.println(dao.update(f) ? "✅ Cập nhật OK!" : "❌ Thất bại!");
    }

    private void deleteFlower() {
        System.out.print("Nhập mã hoa cần xoá: ");
        String id = sc.nextLine();
        System.out.print("Xác nhận xoá (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) return;

        System.out.println(dao.delete(id) ? "✅ Xoá OK!" : "❌ Thất bại!");
    }

    // ════════ HELPER ════════

    private void printList(List<Flower> list) {
        if (list.isEmpty()) { System.out.println("  (Trống)"); return; }
        System.out.printf("  %-6s | %-28s | %12s | %5s%n",
                "Mã", "Tên", "Giá", "Tồn");
        System.out.println("  " + "─".repeat(60));
        for (Flower f : list) printOne(f);
        System.out.println("→ Tổng: " + list.size());
    }

    private void printOne(Flower f) {
        System.out.printf("  %-6s | %-28s | %,12d | %5d%n",
                f.getFlowerId(), f.getName(),
                f.getUnitPrice().longValue(), f.getStockQuantity());
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) sc.next();
        int n = sc.nextInt(); sc.nextLine();
        return n;
    }
}