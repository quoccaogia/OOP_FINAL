using System;

namespace vn.edu.crud.model;

public class Payment
{
    public string PaymentId { get; set; } = string.Empty;
    public string OrderId { get; set; } = string.Empty;
    public decimal Amount { get; set; }
    public string PaymentMethod { get; set; } = string.Empty;
    public DateTime? PaymentDate { get; set; }
    public string PaymentStatus { get; set; } = string.Empty;

    public Payment() { }
}
