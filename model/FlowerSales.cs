namespace vn.edu.crud.model;

/// <summary>
/// DTO (Data Transfer Object) — class phụ để chứa
/// thông tin hoa + số lượng đã bán (dùng cho thống kê doanh số hoa).
/// </summary>
public class FlowerSales
{
    public Flower? Flower { get; set; }
    public int TotalSold { get; set; }

    public FlowerSales() {}

    public FlowerSales(Flower flower, int totalSold)
    {
        Flower = flower;
        TotalSold = totalSold;
    }

    public override string ToString()
    {
        return $"{Flower?.Name} — đã bán: {TotalSold}";
    }
}
