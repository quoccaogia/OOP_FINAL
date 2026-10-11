package vn.edu.crud.test;

import vn.edu.crud.dao.FlowerDAO;
import vn.edu.crud.model.Flower;
import vn.edu.crud.model.FlowerSales;

import java.math.BigDecimal;
import java.util.List;

public class TestFlowerQueries {

    public static void main(String[] args) {
        FlowerDAO dao = new FlowerDAO();

        // Q01
        System.out.println("\n═══ Q01: HOA SẮP HẾT HÀNG (< 10) ═══");
        for (Flower f : dao.findLowStock())
            System.out.printf("  %s | %s | tồn: %d%n",
                    f.getFlowerId(), f.getName(), f.getStockQuantity());

        // Q03
        System.out.println("\n═══ Q03: TÌM HOA CHỨA 'Hồng' ═══");
        for (Flower f : dao.findByName("Hồng"))
            System.out.println("  " + f.getFlowerId() + " | " + f.getName());

        // Q05
        System.out.println("\n═══ Q05: TÌM HOA FL01 ═══");
        Flower f = dao.findById("FL01");
        System.out.println(f != null ? "  " + f.getName() : "  Không tìm thấy");

        // Q06
        System.out.println("\n═══ Q06: HOA CÓ GIÁ 200K - 500K ═══");
        for (Flower x : dao.findByPriceRange(
                new BigDecimal("200000"), new BigDecimal("500000")))
            System.out.printf("  %s | %,d VND%n",
                    x.getName(), x.getUnitPrice().longValue());

        // Q07
        System.out.println("\n═══ Q07: TOP 3 HOA BÁN CHẠY ═══");
        for (FlowerSales fs : dao.findBestSellers(3))
            System.out.printf("  %s — đã bán: %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());

        // Q08
        System.out.println("\n═══ Q08: HOA MÀU ĐỎ ═══");
        for (Flower x : dao.findByColor("Đỏ"))
            System.out.println("  " + x.getName());

        // Q10
        System.out.println("\n═══ Q10: SẮP XẾP THEO GIÁ GIẢM DẦN ═══");
        for (Flower x : dao.findAllOrderByPriceDesc())
            System.out.printf("  %-30s | %,d VND%n",
                    x.getName(), x.getUnitPrice().longValue());

        // Q12
        System.out.println("\n═══ Q12: TOP 3 HOA BÁN Ế NHẤT ═══");
        for (FlowerSales fs : dao.findWorstSellers(3))
            System.out.printf("  %s — đã bán: %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());

        // Q23
        System.out.println("\n═══ Q23: TỔNG SỐ LƯỢNG ĐÃ BÁN THEO TỪNG LOẠI ═══");
        for (FlowerSales fs : dao.findSoldQuantityByFlower())
            System.out.printf("  %-30s → %d%n",
                    fs.getFlower().getName(), fs.getTotalSold());

        // Q28
        System.out.println("\n═══ Q28: HOA CHƯA TỪNG ĐƯỢC ĐẶT ═══");
        for (Flower x : dao.findNeverOrdered())
            System.out.println("  " + x.getName());
    }
}