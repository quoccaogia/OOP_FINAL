package vn.edu.crud.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InBangHelper {

    private static final NumberFormat VND = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DF_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DF_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public static void inBang(String[] headers, List<Object[]> list) {
        inBang(list, headers);
    }

    public static void inBang(List<Object[]> list, String[] headers) {
        if (list == null || list.isEmpty()) {
            System.out.println("Không có dữ liệu.");
            return;
        }

        int colCount = 0;
        for (Object[] r : list) {
            if (r != null && r.length > colCount) colCount = r.length;
        }
        if (headers != null && headers.length > colCount) {
            colCount = headers.length;
        }

        List<String[]> stringTable = new ArrayList<>();
        for (Object[] row : list) {
            String[] strRow = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                if (row != null && i < row.length && row[i] != null) {
                    strRow[i] = dinhDangGiaTri(row[i]);
                } else {
                    strRow[i] = "";
                }
            }
            stringTable.add(strRow);
        }

        int[] widths = new int[colCount];
        for (int i = 0; i < colCount; i++) {
            int maxW = (headers != null && i < headers.length) ? headers[i].length() : 4;
            for (String[] strRow : stringTable) {
                if (strRow[i].length() > maxW) maxW = strRow[i].length();
            }
            widths[i] = maxW;
        }

        int totalWidth = (colCount - 1) * 3;
        for (int w : widths) totalWidth += w;

        if (headers != null && headers.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < colCount; i++) {
                String h = i < headers.length ? headers[i] : "";
                sb.append(String.format("%-" + widths[i] + "s", h));
                if (i < colCount - 1) sb.append(" | ");
            }
            System.out.println(sb);
            System.out.println("-".repeat(Math.max(1, totalWidth)));
        } else {
            System.out.println("-".repeat(Math.max(1, totalWidth)));
        }

        for (String[] strRow : stringTable) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < colCount; i++) {
                sb.append(String.format("%-" + widths[i] + "s", strRow[i]));
                if (i < colCount - 1) sb.append(" | ");
            }
            System.out.println(sb);
        }
    }

    private static String dinhDangGiaTri(Object val) {
        if (val == null) return "";

        if (val instanceof java.sql.Date) {
            return ((java.sql.Date) val).toLocalDate().format(DF_DATE);
        }
        if (val instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) val).toLocalDateTime().format(DF_DATETIME);
        }
        if (val instanceof LocalDate) {
            return ((LocalDate) val).format(DF_DATE);
        }
        if (val instanceof LocalDateTime) {
            return ((LocalDateTime) val).format(DF_DATETIME);
        }
        if (val instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) val;
            if (bd.compareTo(BigDecimal.valueOf(1000)) >= 0) {
                return VND.format(bd);
            }
            return bd.toPlainString();
        }
        if (val instanceof Double || val instanceof Float) {
            double d = ((Number) val).doubleValue();
            if (d >= 1000) {
                return VND.format(d);
            }
            return String.valueOf(d);
        }

        String str = val.toString().trim();
        if (str.endsWith(".00")) {
            try {
                BigDecimal bd = new BigDecimal(str);
                if (bd.compareTo(BigDecimal.valueOf(1000)) >= 0) {
                    return VND.format(bd);
                }
            } catch (Exception ignored) {}
        }

        return str;
    }
}
