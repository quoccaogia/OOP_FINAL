package vn.edu.crud.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PurchaseOrder {
    private String purchaseOrderId;
    private String supplierId;
    private String employeeId;
    private LocalDateTime importDate;
    private BigDecimal totalAmount;
    private String status;
    private String note;

    // Constructor rỗng, constructor đầy đủ, getters, setters, toString
}