using System;

namespace vn.edu.crud.util;

public static class AnToan
{
    public static void BaoLoi(Exception e, string? goiY = null)
    {
        Console.WriteLine($"\n❌ Thao tác không thực hiện được: {TomTat(e)}");

        if (!string.IsNullOrWhiteSpace(goiY))
        {
            Console.WriteLine($"👉 Nhập đúng: {goiY}");
        }
        Console.WriteLine("👉 Dữ liệu vừa nhập chưa được ghi, bạn kiểm tra rồi thử lại nhé.");
    }

    private static string TomTat(Exception e)
    {
        return e switch
        {
            FormatException => "chỗ vừa nhập phải là SỐ.",
            NullReferenceException => "thiếu dữ liệu bắt buộc (có thể đơn/hoa chưa có thông tin).",
            DivideByZeroException => "phép tính không hợp lệ (chia cho 0?).",
            _ => string.IsNullOrWhiteSpace(e.Message) ? e.GetType().Name : e.Message
        };
    }
}
