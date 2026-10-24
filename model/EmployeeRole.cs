using System;

namespace vn.edu.crud.model;

public class EmployeeRole
{
    public string Key { get; }
    public string Label { get; }
    public decimal LuongCoBan { get; }

    public EmployeeRole(string key, string label, decimal luongCoBan)
    {
        Key = key;
        Label = label;
        LuongCoBan = luongCoBan;
    }

    public static readonly EmployeeRole ChamHoa = new("CHAM_HOA", "Chăm sóc hoa", 5_500_000m);
    public static readonly EmployeeRole GiaoHang = new("GIAO_HANG", "Giao hàng", 6_000_000m);
    public static readonly EmployeeRole ThuNgan = new("THU_NGAN", "Thu ngân", 5_000_000m);
    public static readonly EmployeeRole QuanLy = new("QUAN_LY", "Quản lý", 10_000_000m);

    public static readonly EmployeeRole[] AllRoles = [ChamHoa, GiaoHang, ThuNgan, QuanLy];

    public static string GetLabelFromString(string? roleStr)
    {
        if (string.IsNullOrWhiteSpace(roleStr)) return "";
        foreach (var r in AllRoles)
        {
            if (r.Key.Equals(roleStr, StringComparison.OrdinalIgnoreCase) ||
                r.Label.Equals(roleStr, StringComparison.OrdinalIgnoreCase))
            {
                return r.Label;
            }
        }
        return roleStr;
    }
}
