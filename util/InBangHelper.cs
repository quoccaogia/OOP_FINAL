using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

namespace vn.edu.crud.util;

public static class InBangHelper
{
    private static readonly CultureInfo VnCulture = new("vi-VN");

        public static void InBang(string[] headers, List<object[]> list) => InBang(list, headers);

    public static void InBang(List<object[]> list, string[]? headers = null)
    {
        if (list == null || list.Count == 0)
        {
            Console.WriteLine("Không có dữ liệu.");
            return;
        }

        int colCount = list.Max(r => r.Length);
        if (headers != null && headers.Length > colCount)
            colCount = headers.Length;

        // Chuyển đổi toàn bộ dữ liệu thành chuỗi định dạng đẹp
        var stringTable = new List<string[]>();
        foreach (var row in list)
        {
            var strRow = new string[colCount];
            for (int i = 0; i < colCount; i++)
            {
                if (i < row.Length && row[i] != null)
                {
                    strRow[i] = DinhDangGiaTri(row[i]);
                }
                else
                {
                    strRow[i] = "";
                }
            }
            stringTable.Add(strRow);
        }

        // Tính độ rộng lớn nhất cho từng cột (dựa trên nội dung thực tế và tiêu đề)
        var widths = new int[colCount];
        for (int i = 0; i < colCount; i++)
        {
            int maxW = (headers != null && i < headers.Length) ? headers[i].Length : 4;
            foreach (var strRow in stringTable)
            {
                if (strRow[i].Length > maxW) maxW = strRow[i].Length;
            }
            widths[i] = maxW;
        }

        int totalWidth = widths.Sum() + 3 * (colCount - 1);

        // In tiêu đề nếu có
        if (headers != null && headers.Length > 0)
        {
            var headerCells = new string[colCount];
            for (int i = 0; i < colCount; i++)
            {
                string h = i < headers.Length ? headers[i] : "";
                headerCells[i] = h.PadRight(widths[i]);
            }
            Console.WriteLine(string.Join(" | ", headerCells));
            Console.WriteLine(new string('-', totalWidth));
        }
        else
        {
            Console.WriteLine(new string('-', totalWidth));
        }

        // In từng dòng dữ liệu căn lề thẳng tắp
        foreach (var strRow in stringTable)
        {
            var cells = new string[colCount];
            for (int i = 0; i < colCount; i++)
            {
                cells[i] = strRow[i].PadRight(widths[i]);
            }
            Console.WriteLine(string.Join(" | ", cells));
        }
    }

    private static string DinhDangGiaTri(object val)
    {
        if (val == null) return "";

        if (val is DateTime dt)
        {
            if (dt == DateTime.MinValue) return "—";
            return dt.TimeOfDay == TimeSpan.Zero
                ? dt.ToString("dd/MM/yyyy")
                : dt.ToString("dd/MM/yyyy HH:mm");
        }

        if (val is decimal dec)
        {
            return dec >= 1000 ? dec.ToString("N0", VnCulture) + " đ" : dec.ToString("G");
        }

        if (val is double d)
        {
            return d >= 1000 ? d.ToString("N0", VnCulture) + " đ" : d.ToString("G");
        }

        string str = val.ToString() ?? "";

        // Định dạng tiền nếu có dạng 180000.00 từ MySQL
        if (str.EndsWith(".00") && decimal.TryParse(str, NumberStyles.Any, CultureInfo.InvariantCulture, out decimal parsedMoney))
        {
            return parsedMoney >= 1000 ? parsedMoney.ToString("N0", VnCulture) + " đ" : parsedMoney.ToString("G");
        }

        // Định dạng ngày giờ nếu là chuỗi date từ MySQL
        if (DateTime.TryParse(str, out DateTime parsedDt) && str.Length > 10 &&
            (str.Contains("AM") || str.Contains("PM") || str.Count(c => c == '-') == 2 || str.Count(c => c == '/') == 2))
        {
            if (parsedDt == DateTime.MinValue) return "—";
            return parsedDt.TimeOfDay == TimeSpan.Zero
                ? parsedDt.ToString("dd/MM/yyyy")
                : parsedDt.ToString("dd/MM/yyyy HH:mm");
        }

        return str;
    }
}
