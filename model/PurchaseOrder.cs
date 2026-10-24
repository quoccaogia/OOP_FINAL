using System;

namespace vn.edu.crud.model;

public class PurchaseOrder
{
    public string PurchaseOrderId { get; set; } = string.Empty;
    public string SupplierId { get; set; } = string.Empty;
    public string EmployeeId { get; set; } = string.Empty;
    public DateTime? ImportDate { get; set; }
    public decimal TotalAmount { get; set; }
    public string Status { get; set; } = string.Empty;
    public string Note { get; set; } = string.Empty;

    public PurchaseOrder() { }
}
