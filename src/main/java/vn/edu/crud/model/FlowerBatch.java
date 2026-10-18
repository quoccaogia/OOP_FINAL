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

    public FlowerBatch() {}

    public FlowerBatch(String batchId, String purchaseOrderId, String flowerId, int quantityImported, int quantityRemaining, BigDecimal unitCost, LocalDateTime importDate, LocalDate expiryDate, String status, String note) {
        this.batchId = batchId;
        this.purchaseOrderId = purchaseOrderId;
        this.flowerId = flowerId;
        this.quantityImported = quantityImported;
        this.quantityRemaining = quantityRemaining;
        this.unitCost = unitCost;
        this.importDate = importDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.note = note;
    }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public String getPurchaseOrderId() { return purchaseOrderId; }
    public void setPurchaseOrderId(String purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }

    public int getQuantityImported() { return quantityImported; }
    public void setQuantityImported(int quantityImported) { this.quantityImported = quantityImported; }

    public int getQuantityRemaining() { return quantityRemaining; }
    public void setQuantityRemaining(int quantityRemaining) { this.quantityRemaining = quantityRemaining; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public LocalDateTime getImportDate() { return importDate; }
    public void setImportDate(LocalDateTime importDate) { this.importDate = importDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "FlowerBatch{" + batchId + ", " + flowerId + ", " + status + "}";
    }
}