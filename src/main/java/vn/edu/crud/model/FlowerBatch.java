package vn.edu.crud.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FlowerBatch {
    private String batchId;
    private String purchaseOrderId;
    private String flowerId;
    private int quantityImported;
    private int quantityRemaining;
    private BigDecimal unitCost;
    private LocalDateTime importDate;
    private LocalDate expiryDate;
    private String status;
    private String note;

    // Constructor rỗng, constructor đầy đủ, getters, setters, toString
}