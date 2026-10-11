package vn.edu.crud.model;

import java.math.BigDecimal;

public class Flower {
    private String flowerId;
    private String name;
    private String category;
    private BigDecimal unitPrice;
    private int stockQuantity;
    private String color;
    private String supplierId;

    public Flower() {}

    public Flower(String flowerId, String name, String category, BigDecimal unitPrice,
                  int stockQuantity, String color, String supplierId) {
        this.flowerId = flowerId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.color = color;
        this.supplierId = supplierId;
    }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    @Override
    public String toString() {
        return "Flower{" + flowerId + ", " + name + ", " + unitPrice + "}";
    }
}