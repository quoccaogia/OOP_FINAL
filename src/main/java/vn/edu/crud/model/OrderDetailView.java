package vn.edu.crud.model;

import java.math.BigDecimal;

public class OrderDetailView {
    private String flowerId;
    private String flowerName;
    private int quantity;
    private BigDecimal unitPriceAtSale;
    private BigDecimal subtotal;

    public OrderDetailView() {}

    public OrderDetailView(String flowerId, String flowerName,
                           int quantity, BigDecimal unitPriceAtSale) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.quantity = quantity;
        this.unitPriceAtSale = unitPriceAtSale;
        this.subtotal = unitPriceAtSale.multiply(BigDecimal.valueOf(quantity));
    }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPriceAtSale() { return unitPriceAtSale; }
    public void setUnitPriceAtSale(BigDecimal unitPriceAtSale) { this.unitPriceAtSale = unitPriceAtSale; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return flowerName + " x" + quantity + " = " + subtotal;
    }
}