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

    public PurchaseOrder() {}

    public String getPurchaseOrderId() { return purchaseOrderId; }
    public void setPurchaseOrderId(String purchaseOrderId) { this.purchaseOrderId = purchaseOrderId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public LocalDateTime getImportDate() { return importDate; }
    public void setImportDate(LocalDateTime importDate) { this.importDate = importDate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}