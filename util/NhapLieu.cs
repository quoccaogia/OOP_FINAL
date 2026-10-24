using System;
using System.Text.RegularExpressions;

namespace vn.edu.crud.util;

public static class NhapLieu
{
    public static string ChuanHoaSdt(string? sdt)
    {
        if (string.IsNullOrWhiteSpace(sdt)) return "";
        string s = Regex.Replace(sdt.Trim(), @"\s+", "");
        if (s.StartsWith("+84")) s = "0" + s.Substring(3);
        s = Regex.Replace(s, "^0+", "0");
        return s;
    }

    public static bool LaSdtKhach(string? sdt)
    {
        return Regex.IsMatch(ChuanHoaSdt(sdt), @"^0\d{9}$");
    }

    public static bool LaSdtNhaCungCap(string? sdt)
    {
        return Regex.IsMatch(ChuanHoaSdt(sdt), @"^0\d{9,10}$");
    }

    public static bool LaEmail(string? email)
    {
        if (string.IsNullOrWhiteSpace(email)) return false;
        return Regex.IsMatch(email, @"^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$");
    }

    public static string BatBuoc(string nhac, string goiY)
    {
        while (true)
        {
            Console.Write($"{nhac} ({goiY}): ");
            string? s = Console.ReadLine()?.Trim();
            if (!string.IsNullOrEmpty(s)) return s;
            Console.WriteLine("⚠️ Không được để trống.");
        }
    }

    public static string TuyChon(string nhac, string goiY)
    {
        Console.Write($"{nhac} ({goiY}): ");
        return Console.ReadLine()?.Trim() ?? "";
    }

    public static string SdtKhach(string nhac, bool batBuoc)
    {
        while (true)
        {
            Console.Write($"{nhac} (10 số, VD: 0901234567{(batBuoc ? "" : ", Enter để bỏ trống")}): ");
            string s = ChuanHoaSdt(Console.ReadLine());
            if (string.IsNullOrEmpty(s))
            {
                if (!batBuoc) return "";
                Console.WriteLine("⚠️ Không được để trống.");
                continue;
            }
            if (LaSdtKhach(s)) return s;
            Console.WriteLine("⚠️ SĐT phải gồm đúng 10 chữ số và bắt đầu bằng 0. VD: 0901234567");
        }
    }

    public static string SdtNhaCungCap(string nhac)
    {
        while (true)
        {
            Console.Write($"{nhac} (10-11 số, VD: 0263388899): ");
            string s = ChuanHoaSdt(Console.ReadLine());
            if (string.IsNullOrEmpty(s))
            {
                Console.WriteLine("⚠️ Không được để trống.");
                continue;
            }
            if (LaSdtNhaCungCap(s)) return s;
            Console.WriteLine("⚠️ SĐT phải gồm 10-11 chữ số và bắt đầu bằng 0. VD: 0263388899");
        }
    }

    public static string Email(string nhac, bool batBuoc)
    {
        while (true)
        {
            Console.Write($"{nhac} (VD: ten@gmail.com{(batBuoc ? "" : ", Enter để bỏ trống")}): ");
            string? s = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(s))
            {
                if (!batBuoc) return "";
                Console.WriteLine("⚠️ Không được để trống.");
                continue;
            }
            if (LaEmail(s)) return s;
            Console.WriteLine("⚠️ Email không hợp lệ, phải có dạng ten@domain.com.");
        }
    }

    public static int SoNguyen(string nhac, string goiY, int min, int max)
    {
        while (true)
        {
            Console.Write($"{nhac} ({goiY}): ");
            if (int.TryParse(Console.ReadLine()?.Trim(), out int n))
            {
                if (n < min || n > max)
                {
                    Console.WriteLine($"⚠️ Giá trị phải từ {min} đến {max}.");
                    continue;
                }
                return n;
            }
            Console.WriteLine($"⚠️ Phải nhập số nguyên. VD: {min}");
        }
    }

    public static decimal Tien(string nhac, string goiY, bool choPhep0)
    {
        while (true)
        {
            Console.Write($"{nhac} ({goiY}): ");
            if (decimal.TryParse(Console.ReadLine()?.Trim(), out decimal v))
            {
                if (v < 0 || (!choPhep0 && v == 0))
                {
                    Console.WriteLine($"⚠️ Số tiền phải {(choPhep0 ? ">= 0" : "> 0")}.");
                    continue;
                }
                return v;
            }
            Console.WriteLine("⚠️ Phải nhập số. VD: 5000");
        }
    }
}
