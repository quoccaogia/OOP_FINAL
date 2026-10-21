package vn.edu.crud.util;

/**
 * In thông báo lỗi thân thiện khi một thao tác ném exception.
 *
 * Mục đích: không lộ stack trace ra màn hình, không làm sập chương trình,
 * và luôn nói cho người dùng biết phải nhập đúng như thế nào.
 */
public class AnToan {

    private AnToan() {
    }

    /**
     * @param e    lỗi bắt được
     * @param goiY mô tả dữ liệu đúng, VD: "số lượng là số nguyên dương, VD: 10"
     */
    public static void baoLoi(Exception e, String goiY) {
        System.out.println("\n❌ Thao tác không thực hiện được: " + tomTat(e));

        if (goiY != null && !goiY.isBlank()) {
            System.out.println("👉 Nhập đúng: " + goiY);
        }
        System.out.println("👉 Dữ liệu vừa nhập chưa được ghi, bạn kiểm tra rồi thử lại nhé.");
    }

    private static String tomTat(Exception e) {
        if (e instanceof NumberFormatException) {
            return "chỗ vừa nhập phải là SỐ.";
        }
        if (e instanceof java.time.format.DateTimeParseException) {
            return "ngày giờ sai định dạng.";
        }
        if (e instanceof NullPointerException) {
            return "thiếu dữ liệu bắt buộc (có thể đơn/hoa chưa có thông tin).";
        }
        if (e instanceof ArithmeticException) {
            return "phép tính không hợp lệ (chia cho 0?).";
        }
        String m = e.getMessage();
        return (m == null || m.isBlank()) ? e.getClass().getSimpleName() : m;
    }
}
