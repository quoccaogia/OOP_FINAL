package vn.edu.crud.model;

/**
 * DTO (Data Transfer Object) — class phụ để chứa
 * thông tin hoa + số lượng đã bán (dùng cho Q07, Q12, Q23).
 */
public class FlowerSales {
    private Flower flower;
    private int totalSold;

    public FlowerSales() {}

    public FlowerSales(Flower flower, int totalSold) {
        this.flower = flower;
        this.totalSold = totalSold;
    }

    public Flower getFlower() { return flower; }
    public void setFlower(Flower flower) { this.flower = flower; }

    public int getTotalSold() { return totalSold; }
    public void setTotalSold(int totalSold) { this.totalSold = totalSold; }

    @Override
    public String toString() {
        return flower.getName() + " — đã bán: " + totalSold;
    }
}