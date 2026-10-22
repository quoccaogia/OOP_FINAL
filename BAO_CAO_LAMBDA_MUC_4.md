# TÀI LIỆU BÁO CÁO ĐỒ ÁN OOP: BIỂU THỨC LAMBDA & STREAM API (MỤC 4 - QUẢN LÝ HOA)

> **Dành cho:** Báo cáo, thuyết trình và trả lời vấn đáp đồ án môn Lập trình hướng đối tượng (Java).  
> **Phạm vi:** Mục 4 — Quản lý Danh mục Hoa (`FlowerDAO.java` và `FlowerMenu.java`).  
> **Tác giả:** Nhóm đồ án FlowerShop.

---

## I. TỔNG QUAN VỀ LAMBDA & STREAM API TRONG ĐỒ ÁN

### 1. Lambda Expression là gì?
* **Khái niệm:** Biểu thức Lambda (ra mắt từ Java 8) là một hàm ẩn danh (anonymous function) — nghĩa là một hàm không có tên, không cần khai báo lớp rườm rà, giúp truyền khối xử lý logic như một tham số.
* **Cú pháp cơ bản:**
  $$\text{(tham\_số)} \rightarrow \text{\{ thân\_hàm \}}$$
  *Ví dụ:* `f -> f.getStockQuantity() < 10`  
  *(Đọc là: với mỗi bông hoa `f`, kiểm tra xem tồn kho của nó có nhỏ hơn 10 hay không)*.

### 2. Stream API là gì?
* **Stream** là một luồng dữ liệu (pipeline) cho phép thao tác trên danh sách (`List`) theo phong cách lập trình hàm (Functional Programming).
* Chu trình của 1 Stream gồm 3 bước:
  1. **Khởi tạo Stream:** `allFlowers.stream()` (biến danh sách thành luồng dữ liệu).
  2. **Thao tác trung gian (Intermediate):** `.filter(...)`, `.sorted(...)`, `.mapToInt(...)` (lọc, sắp xếp, biến đổi dữ liệu).
  3. **Thao tác kết thúc (Terminal):** `.collect(...)`, `.sum()`, `.count()` (gom kết quả lại thành `List` hoặc tính ra một giá trị).

---

## II. GIẢI THÍCH CHI TIẾT 5 ĐOẠN CODE LAMBDA ĐÃ VIẾT

---

### 1. Tìm kiếm hoa theo Tên hoặc Danh mục
* **Vị trí:** [`FlowerDAO.java`](file:///D:/OOP/FlowerShopV2/src/main/java/vn/edu/crud/dao/FlowerDAO.java) — phương thức `searchFlowersByLambda`
* **Đoạn code:**
```java
public List<Flower> searchFlowersByLambda(String keyword) {
    List<Flower> allFlowers = this.findAll();

    if (keyword == null || keyword.trim().isEmpty()) {
        return allFlowers;
    }

    String tuKhoa = keyword.toLowerCase().trim();

    return allFlowers.stream()
            .filter(f -> (f.getName() != null && f.getName().toLowerCase().contains(tuKhoa)) ||
                         (f.getCategory() != null && f.getCategory().toLowerCase().contains(tuKhoa)))
            .collect(Collectors.toList());
}
```

* **Cách đọc hiểu từng dòng:**
  1. `allFlowers.stream()`: Mở luồng duyệt qua từng đối tượng `Flower` trong danh sách.
  2. `.filter(...)`: Giữ lại những bông hoa thỏa mãn điều kiện bên trong ngoặc:
     * `f` là biến đại diện cho từng đối tượng `Flower`.
     * `f.getName().toLowerCase().contains(tuKhoa)`: Chuyển tên hoa về chữ thường rồi kiểm tra xem có chứa từ khóa không (tìm kiếm không phân biệt hoa/thường).
     * `||`: Hoặc tên hoa khớp, HOẶC danh mục hoa khớp.
     * Kiểm tra `!= null` để tránh lỗi kinh điển `NullPointerException`.
  3. `.collect(Collectors.toList())`: Thu thập các bông hoa thỏa mãn điều kiện và đóng gói thành một `List<Flower>` mới trả về cho Menu hiển thị.

* **Cách trả lời giảng viên:**
  > *"Thưa thầy/cô, ở đây em dùng phương thức `filter` của Stream API kết hợp với biểu thức Lambda `f -> ...`. Biểu thức này nhận vào đối tượng hoa `f`, kiểm tra xem tên hoặc danh mục có chứa từ khóa cần tìm hay không. Nếu có (trả về `true`) thì giữ lại, cuối cùng dùng `Collectors.toList()` gom thành danh sách kết quả."*

---

### 2. Lọc hoa sắp hết hàng (Tồn kho < 10)
* **Vị trí:** [`FlowerDAO.java`](file:///D:/OOP/FlowerShopV2/src/main/java/vn/edu/crud/dao/FlowerDAO.java) — phương thức `hoaSapHetLambda`
* **Đoạn code:**
```java
public List<Flower> hoaSapHetLambda(int nguong) {
    List<Flower> allFlowers = this.findAll();

    return allFlowers.stream()
            .filter(f -> f.getStockQuantity() < nguong)
            .collect(Collectors.toList());
}
```

* **Cách đọc hiểu:**
  * Lấy toàn bộ hoa từ cơ sở dữ liệu lên bộ nhớ.
  * `.filter(f -> f.getStockQuantity() < nguong)`: Duyệt từng bông hoa `f`, nếu `stockQuantity` nhỏ hơn ngưỡng (ví dụ 10) thì giữ lại.

* **Cách trả lời giảng viên:**
  > *"Dạ hàm này lọc danh sách hoa trên bộ nhớ bằng Lambda. Điều kiện `f.getStockQuantity() < nguong` giúp hệ thống tìm ra những loài hoa sắp cạn hàng để nhân viên kịp thời tạo phiếu nhập."*

---

### 3. Lọc hoa theo khoảng giá
* **Vị trí:** [`FlowerDAO.java`](file:///D:/OOP/FlowerShopV2/src/main/java/vn/edu/crud/dao/FlowerDAO.java) — phương thức `timTheoKhoangGiaLambda`
* **Đoạn code:**
```java
public List<Flower> timTheoKhoangGiaLambda(BigDecimal giaMin, BigDecimal giaMax) {
    List<Flower> allFlowers = this.findAll();

    return allFlowers.stream()
            .filter(f -> f.getUnitPrice() != null
                      && f.getUnitPrice().compareTo(giaMin) >= 0
                      && f.getUnitPrice().compareTo(giaMax) <= 0)
            .collect(Collectors.toList());
}
```

* **Cách đọc hiểu:**
  * Giá tiền trong đồ án sử dụng kiểu `BigDecimal` (kiểu dữ liệu chuẩn cho tiền tệ trong Java để tránh sai số thập phân của `float`/`double`).
  * `compareTo()` là phương thức so sánh của `BigDecimal`:
    * `A.compareTo(B) >= 0` nghĩa là $A \ge B$.
    * `A.compareTo(B) <= 0` nghĩa là $A \le B$.
  * Biểu thức lambda kiểm tra xem đơn giá của hoa có nằm trong đoạn `[giaMin, giaMax]` hay không.

* **Cách trả lời giảng viên:**
  > *"Dạ vì đơn giá lưu dạng `BigDecimal` nên em không dùng toán tử `>=`, `<=` mà dùng hàm `compareTo()`. Biểu thức Lambda `f -> ...` lọc ra các loài hoa có đơn giá lớn hơn hoặc bằng giá tối thiểu và nhỏ hơn hoặc bằng giá tối đa."*

---

### 4. Sắp xếp danh sách hoa theo giá giảm dần
* **Vị trí:** [`FlowerDAO.java`](file:///D:/OOP/FlowerShopV2/src/main/java/vn/edu/crud/dao/FlowerDAO.java) — phương thức `hoaGiaGiamDanLambda`
* **Đoạn code:**
```java
public List<Flower> hoaGiaGiamDanLambda() {
    List<Flower> allFlowers = this.findAll();

    return allFlowers.stream()
            .filter(f -> f.getUnitPrice() != null)
            .sorted((f1, f2) -> f2.getUnitPrice().compareTo(f1.getUnitPrice()))
            .collect(Collectors.toList());
}
```

* **Cách đọc hiểu:**
  * `.filter(f -> f.getUnitPrice() != null)`: Loại bỏ các bông hoa chưa có giá để khi so sánh không bị lỗi `NullPointerException`.
  * `.sorted((f1, f2) -> ...)`: Dùng biểu thức Lambda để định nghĩa tiêu chí so sánh (thay thế cho interface `Comparator` truyền thống).
  * **Quy tắc so sánh:**
    * Nếu viết `f1.compareTo(f2)` $\rightarrow$ Giá tăng dần.
    * Đảo lại viết `f2.compareTo(f1)` $\rightarrow$ **Giá giảm dần** (hoa đắt tiền nhất đứng đầu bảng).

* **Cách trả lời giảng viên:**
  > *"Dạ ở đây em dùng `.sorted()` truyền vào biểu thức Lambda có 2 tham số `(f1, f2)`. Bằng cách gọi `f2.getUnitPrice().compareTo(f1.getUnitPrice())`, Java sẽ tự động sắp xếp danh sách theo thứ tự đơn giá giảm dần."*

---

### 5. Tính tổng tồn kho của toàn bộ hoa
* **Vị trí:** [`FlowerMenu.java`](file:///D:/OOP/FlowerShopV2/src/main/java/vn/edu/crud/menu/FlowerMenu.java) — chức năng 1 (Xem tất cả danh sách hoa)
* **Đoạn code:**
```java
// Thay thế cho vòng for cũ:
int tongTon = tatCa.stream()
        .mapToInt(f -> f.getStockQuantity())
        .sum();
```

* **Cách đọc hiểu:**
  * `tatCa.stream()`: Tạo Stream từ danh sách `List<Flower>`.
  * `.mapToInt(f -> f.getStockQuantity())`: Biến đổi luồng các bông hoa thành luồng các con số nguyên (IntStream) — chỉ lấy thuộc tính `stockQuantity`.
  * `.sum()`: Tính tổng toàn bộ các con số tồn kho trong luồng đó.

* **Cách trả lời giảng viên:**
  > *"Dạ thay vì phải viết vòng lặp `for` và khai báo biến đếm cộng dồn, em dùng `mapToInt` để trích xuất số lượng tồn kho của từng bông hoa rồi gọi hàm `.sum()` để tính tổng toàn bộ tồn kho một cách ngắn gọn và tường minh."*

---

## III. BỘ CÂU HỎI VẤN ĐÁP THẦY CÔ HAY HỎI (CHEAT-SHEET)

### ❓ Câu 1: Tại sao lại dùng Lambda và Stream trong khi có thể viết vòng lặp for hoặc dùng SQL `WHERE`?
* **Trả lời:**
  * **So với vòng lặp `for`:** Code dùng Lambda ngắn gọn hơn, không cần tạo biến tạm, giảm thiểu lỗi sai chỉ số (Index), và code dễ đọc hiểu theo dạng luồng dữ liệu (Pipeline).
  * **So với câu lệnh SQL:** Khi dữ liệu đã được tải lên bộ nhớ (RAM), việc lọc bằng Lambda diễn ra ngay lập tức mà không phải tốn thời gian gửi truy vấn lại sang MySQL Server (giảm tải cho kết nối mạng và Database).

### ❓ Câu 2: Trong biểu thức `f -> f.getStockQuantity() < 10`, `f` ở đâu ra và kiểu dữ liệu là gì?
* **Trả lời:**
  * `f` là biến do mình tự đặt tên (tham số đầu vào của biểu thức Lambda).
  * Kiểu dữ liệu của `f` là `Flower`, do trình biên dịch Java tự động suy luận (Type Inference) dựa trên danh sách `List<Flower>` gọi `stream()`.

### ❓ Câu 3: Thao tác kết thúc (Terminal Operation) là gì? Nếu không có nó thì chuyện gì xảy ra?
* **Trả lời:**
  * Các hàm như `.collect()`, `.sum()`, `.count()` là Terminal Operation.
  * Stream trong Java hoạt động theo cơ chế **Lazy Evaluation** (chỉ thực thi khi cần). Nếu không có Terminal Operation, toàn bộ các hàm trung gian như `filter` hay `sorted` sẽ **không bao giờ được chạy**.
