package vn.edu.crud.util;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * Nhập liệu có kiểm tra ràng buộc + gợi ý định dạng cho người dùng.
 *
 * Mỗi hàm tự in câu nhắc kèm ví dụ, và lặp lại đến khi dữ liệu hợp lệ.
 * Các ràng buộc bám theo báo cáo chương 5.1 (SĐT 10 số, email có @,
 * tên không rỗng, lương/giá > 0, tồn kho >= 0...).
 */
public class NhapLieu {

    private NhapLieu() {
    }

    // =====================================================
    // CÁC HÀM KIỂM TRA (dùng lại được ở màn sửa)
    // =====================================================

    /** Chuẩn hoá SĐT: bỏ khoảng trắng, +84 -> 0, gộp các số 0 ở đầu. */
    public static String chuanHoaSdt(String sdt) {
        if (sdt == null) return "";
        String s = sdt.trim().replaceAll("\\s+", "");
        if (s.startsWith("+84")) s = "0" + s.substring(3);
        s = s.replaceFirst("^0+", "0");
        return s;
    }

    /** SĐT khách hàng: đúng 10 chữ số, bắt đầu bằng 0. */
    public static boolean laSdtKhach(String sdt) {
        return chuanHoaSdt(sdt).matches("0\\d{9}");
    }

    /** SĐT nhà cung cấp: 10-11 chữ số (điện thoại bàn), bắt đầu bằng 0. */
    public static boolean laSdtNhaCungCap(String sdt) {
        return chuanHoaSdt(sdt).matches("0\\d{9,10}");
    }

    /** Email: có dạng ten@domain.com. */
    public static boolean laEmail(String email) {
        return email != null
                && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // =====================================================
    // CÁC HÀM ĐỌC DỮ LIỆU
    // =====================================================

    /** Chuỗi bắt buộc, không được để trống. */
    public static String batBuoc(Scanner sc, String nhac, String goiY) {
        while (true) {
            System.out.print(nhac + " (" + goiY + "): ");
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("⚠️ Không được để trống.");
        }
    }

    /** Chuỗi có thể bỏ trống. */
    public static String tuyChon(Scanner sc, String nhac, String goiY) {
        System.out.print(nhac + " (" + goiY + "): ");
        return sc.nextLine().trim();
    }

    /** SĐT khách hàng 10 số. */
    public static String sdtKhach(Scanner sc, String nhac, boolean batBuoc) {
        while (true) {
            System.out.print(nhac + " (10 số, VD: 0901234567"
                    + (batBuoc ? "" : ", Enter để bỏ trống") + "): ");
            String s = chuanHoaSdt(sc.nextLine());
            if (s.isEmpty()) {
                if (!batBuoc) return "";
                System.out.println("⚠️ Không được để trống.");
                continue;
            }
            if (laSdtKhach(s)) return s;
            System.out.println("⚠️ SĐT phải gồm đúng 10 chữ số và bắt đầu bằng 0. VD: 0901234567");
        }
    }

    /** SĐT nhà cung cấp 10-11 số. */
    public static String sdtNhaCungCap(Scanner sc, String nhac) {
        while (true) {
            System.out.print(nhac + " (10-11 số, VD: 0263388899): ");
            String s = chuanHoaSdt(sc.nextLine());
            if (s.isEmpty()) {
                System.out.println("⚠️ Không được để trống.");
                continue;
            }
            if (laSdtNhaCungCap(s)) return s;
            System.out.println("⚠️ SĐT phải gồm 10-11 chữ số và bắt đầu bằng 0. VD: 0263388899");
        }
    }

    /** Email (cho phép bỏ trống nếu không bắt buộc). */
    public static String email(Scanner sc, String nhac, boolean batBuoc) {
        while (true) {
            System.out.print(nhac + " (VD: ten@gmail.com"
                    + (batBuoc ? "" : ", Enter để bỏ trống") + "): ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) {
                if (!batBuoc) return "";
                System.out.println("⚠️ Không được để trống.");
                continue;
            }
            if (laEmail(s)) return s;
            System.out.println("⚠️ Email không hợp lệ, phải có dạng ten@domain.com.");
        }
    }

    /** Số nguyên trong khoảng [min, max]. */
    public static int soNguyen(Scanner sc, String nhac, String goiY, int min, int max) {
        while (true) {
            System.out.print(nhac + " (" + goiY + "): ");
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n < min || n > max) {
                    System.out.println("⚠️ Giá trị phải từ " + min + " đến " + max + ".");
                    continue;
                }
                return n;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Phải nhập số nguyên. VD: " + min);
            }
        }
    }

    /** Số tiền > 0, hoặc >= 0 nếu choPhep0. */
    public static BigDecimal tien(Scanner sc, String nhac, String goiY, boolean choPhep0) {
        while (true) {
            System.out.print(nhac + " (" + goiY + "): ");
            try {
                BigDecimal v = new BigDecimal(sc.nextLine().trim());
                if (v.compareTo(BigDecimal.ZERO) < 0
                        || (!choPhep0 && v.compareTo(BigDecimal.ZERO) == 0)) {
                    System.out.println("⚠️ Số tiền phải " + (choPhep0 ? ">= 0" : "> 0") + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Phải nhập số. VD: 5000");
            }
        }
    }
}
