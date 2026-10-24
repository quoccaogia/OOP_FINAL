using System;

namespace vn.edu.crud.model;

public class FlowerBatch
{
    public string BatchId { get; set; } = string.Empty;
    public string PurchaseOrderId { get; set; } = string.Empty;
    public string FlowerId { get; set; } = string.Empty;
    public int QuantityImported { get; set; }
    public int QuantityRemaining { get; set; }
    public decimal UnitCost { get; set; }
    public DateTime? ImportDate { get; set; }
    public DateTime? ExpiryDate { get; set; }
    public string Status { get; set; } = string.Empty;
    public string Note { get; set; } = string.Empty;

    public FlowerBatch() { }
}
