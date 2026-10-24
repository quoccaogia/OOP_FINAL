namespace vn.edu.crud.model;

public class Flower
{
    public string FlowerId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Category { get; set; } = string.Empty;
    public decimal UnitPrice { get; set; }
    public int StockQuantity { get; set; }
    public string Color { get; set; } = string.Empty;
    public string SupplierId { get; set; } = string.Empty;

    public Flower() { }

    public Flower(string id, string name, string category, decimal price, int stock, string color, string supplierId)
    {
        FlowerId = id;
        Name = name;
        Category = category;
        UnitPrice = price;
        StockQuantity = stock;
        Color = color;
        SupplierId = supplierId;
    }
}
