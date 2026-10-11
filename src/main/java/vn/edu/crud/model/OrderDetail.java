package vn.edu.crud.model;

import java.math.BigDecimal;

public class OrderDetail {
    private String orderId;
    private String flowerId;
    private int quantity;
    private BigDecimal unitPriceAtSale;

    public OrderDetail() {}

    public OrderDetail(String orderId, String flowerId, int quantity, BigDecimal unitPriceAtSale) {
        this.orderId = orderId;
        this.flowerId = flowerId;
        this.quantity = quantity;
        this.unitPriceAtSale = unitPriceAtSale;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPriceAtSale() { return unitPriceAtSale; }
    public void setUnitPriceAtSale(BigDecimal unitPriceAtSale) { this.unitPriceAtSale = unitPriceAtSale; }

    @Override
    public String toString() {
        return "OrderDetail{" + orderId + " + " + flowerId + " x" + quantity + "}";
    }
}