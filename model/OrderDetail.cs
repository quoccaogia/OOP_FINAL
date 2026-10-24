namespace vn.edu.crud.model;

public class OrderDetail
{
    public string OrderId { get; set; } = string.Empty;
    public string FlowerId { get; set; } = string.Empty;
    public int Quantity { get; set; }
    public decimal UnitPriceAtSale { get; set; }

    public OrderDetail() { }

    public OrderDetail(string orderId, string flowerId, int quantity, decimal price)
    {
        OrderId = orderId;
        FlowerId = flowerId;
        Quantity = quantity;
        UnitPriceAtSale = price;
    }
}
