using System;

namespace vn.edu.crud.model;

public class Order
{
    public string OrderId { get; set; } = string.Empty;
    public string CustomerId { get; set; } = string.Empty;
    public string EmployeeId { get; set; } = string.Empty;
    public DateTime OrderDate { get; set; }
    public DateTime? DeliveryDate { get; set; }
    public string DeliveryAddress { get; set; } = string.Empty;
    public decimal TotalAmount { get; set; }
    public string OrderStatus { get; set; } = string.Empty;
    public string Note { get; set; } = string.Empty;

    public Order() { }
}
