# HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ CỬA HÀNG HOA (FLOWER SHOP)

> Tài liệu hướng dẫn thao tác thực tế theo từng tình huống: Khách đến mua hoa, Nhà cung cấp giao hàng, Chăm sóc hoa hàng ngày, Thu ngân thanh toán và Quản lý xem báo cáo cuối ngày.  
> Áp dụng đồng bộ cho cả 2 phiên bản: **Java** (`FlowerShopV2`) và **C#** (`FlowerShop_CSharp`).

---

## 📌 BẢNG TRA CỨU MENU CHÍNH (0 - 9)

Khi vừa mở chương trình, màn hình chính sẽ hiển thị 9 phân hệ:
* `[1]` Quản lý Khách hàng
* `[2]` Quản lý Nhà cung cấp
* `[3]` Quản lý Nhân viên
* `[4]` Quản lý Hoa (Bảng giá & tồn kho)
* `[5]` Quản lý Đơn hàng (Bán hàng)
* `[6]` Quản lý Thanh toán (Tiền mặt, Chuyển khoản, MoMo)
* `[7]` Quản lý Lịch chăm sóc hoa
* `[8]` Quản lý Phiếu nhập hàng
* `[9]` Quản lý Lô hoa trong kho
* `[0]` Thoát chương trình

---

## TÌNH HUỐNG 1: KHÁCH HÀNG ĐẾN MUA HOA / ĐẶT HÀNG

### Bước 1: Tiếp đón và lấy thông tin khách
1. Tại Menu chính, bấm phím `1` (Quản lý Khách hàng).
2. Chọn tiếp mục `[2] Nghiệp vụ (Tiếp đón khách)`.
3. Nhập **Số điện thoại** của khách (10 chữ số, VD: `0901234567`):
   * **Nếu là khách cũ**: Màn hình hiện thông tin khách và hỏi có muốn sửa địa chỉ/email không. Nếu không đổi gì, bấm `n`.
   * **Nếu là khách mới**: Hệ thống báo chưa có và hỏi có tạo mới không, bấm `y` -> Nhập Tên, Địa chỉ, Email. Hệ thống sẽ tự sinh mã khách (VD: `KH015`).
4. Ghi nhớ Mã khách hàng (VD: `KH001` hoặc `KH015`) để tạo đơn hàng.
5. Bấm `0` để quay lại Menu chính.

### Bước 2: Tạo đơn hàng mới
1. Tại Menu chính, bấm phím `5` (Quản lý Đơn hàng).
2. Chọn mục `[1] Tạo đơn hàng mới`.
3. Nhập các thông tin:
   * **Mã khách hàng**: Nhập mã ở Bước 1 (VD: `KH001`).
   * **Mã nhân viên phụ trách**: Nhập mã nhân viên đang bán hàng (VD: `NV01`).
   * **Địa chỉ giao hàng**: Bấm `Enter` để lấy địa chỉ mặc định của khách, hoặc gõ địa chỉ mới.
   * **Ngày giao hàng**: Bấm `Enter` để chọn giao ngay hôm nay.
4. Hệ thống sẽ tạo thành công đơn hàng và cấp Mã đơn (VD: `ORD013`).

### Bước 3: Chọn hoa đưa vào đơn hàng
1. Vẫn ở Menu 5, chọn tiếp mục `[2] Thêm hoa vào đơn`.
2. Nhập **Mã đơn hàng**: `ORD013`.
3. Nhập **Mã hoa** khách chọn (VD: `FL01` cho Hồng đỏ, `FL04` cho Lan hồ điệp... Có thể bấm `Enter` để xem bảng giá nếu chưa nhớ mã).
4. Nhập **Số lượng** khách mua (VD: `2`).
   * *Hệ thống sẽ tự động trừ số lượng hoa tương ứng trong kho và tính tiền.*
5. Nếu khách mua thêm loại hoa khác, tiếp tục chọn lại mục `[2]` để thêm tiếp.

### Bước 4: Thu tiền và thanh toán đơn hàng
1. Vẫn ở Menu 5, chọn tiếp mục `[3] Thanh toán đơn hàng`.
2. Nhập Mã đơn: `ORD013`.
3. Màn hình sẽ hiện tổng tiền đơn và số tiền còn thiếu.
4. Chọn hình thức khách trả:
   * `1`: Tiền mặt (TIEN_MAT)
   * `2`: Chuyển khoản ngân hàng (CHUYEN_KHOAN)
   * `3`: Ví MoMo (MOMO)
5. Nhập số tiền khách đưa:
   * Có thể trả đủ 100% tổng tiền.
   * Hoặc khách đặt cọc trước một phần (VD: đơn 500k, khách cọc 200k).
6. Bấm `Enter`, hệ thống báo *"Thanh toán thành công"* và cấp Mã giao dịch (VD: `P014`).

---

## TÌNH HUỐNG 2: NHÀ CUNG CẤP TỚI GIAO HOA (NHẬP HÀNG VÀO KHO)

### Bước 1: Kiểm tra nhà cung cấp
1. Bấm phím `2` (Quản lý Nhà cung cấp) -> Chọn `[1] Xem danh sách NCC`.
2. Nếu là nhà vườn/đối tác mới chưa có tên: Chọn `[2] Thêm nhà cung cấp mới` -> Nhập Tên NCC, SĐT, Địa chỉ.

### Bước 2: Tạo phiếu nhập hàng
1. Tại Menu chính, bấm phím `8` (Quản lý Phiếu nhập hàng).
2. Chọn mục `[2] Tạo phiếu nhập mới`.
3. Nhập:
   * **Mã nhà cung cấp**: VD: `NCC01`.
   * **Mã nhân viên nhận hàng**: VD: `NV01`.
   * **Ghi chú**: VD: Nhập hoa đợt sáng.
4. Hệ thống tạo phiếu nhập với trạng thái ban đầu là `CHO_XU_LY` (Mã phiếu VD: `PN006`).

### Bước 3: Đưa hoa vào từng Lô trong kho
1. Ngay sau khi tạo phiếu, hệ thống sẽ yêu cầu nhập chi tiết từng mặt hàng:
   * Nhập **Mã hoa** cần nhập (VD: `FL01`).
   * Nhập **Số lượng nhập** (VD: `50` cành).
   * Nhập **Giá vốn nhập vào** (VD: `120000`).
   * Nhập **Số ngày hoa tươi/Hạn dùng** (VD: `7` ngày).
   * Nhập **Ghi chú lô hoa** (VD: Hoa tươi loại 1).
2. Hệ thống sẽ tự động:
   * Tạo một Mã lô mới (VD: `L013`).
   * Tự động cộng thêm 50 cành vào số lượng tồn kho của loài hoa đó.
3. Nếu phiếu còn các loại hoa khác, tiếp tục nhập tiếp cho đến khi xong.

### Bước 4: Kiểm tra lại các lô hoa trong kho
1. Bấm `0` về Menu chính -> Chọn `9` (Quản lý Lô hoa).
2. Chọn `[1] Xem tất cả lô hoa`. Màn hình sẽ hiển thị bảng các lô hoa với đầy đủ: Mã lô, Nhà cung cấp, Tên hoa, Số lượng còn, Giá vốn, Hạn dùng và Trạng thái `AVAILABLE`.

---

## TÌNH HUỐNG 3: CÔNG VIỆC CHĂM SÓC HOA HÀNG NGÀY (ĐẦU NGÀY)

### Bước 1: Kiểm tra tình trạng hoa trong cửa hàng
1. Tại Menu chính, bấm phím `7` (Quản lý Lịch chăm sóc).
2. Chọn mục `[1] Bảng tình trạng chăm sóc`.
3. Màn hình hiện bảng tất cả các loài hoa kèm: Lần chăm cuối cùng, Độ tươi hiện tại, Nước tưới (Đủ hay Thiếu).

### Bước 2: Ghi nhận việc chăm sóc hoa
1. Sau khi nhân viên tưới nước hoặc tỉa lá xong, chọn mục `[2] Cập nhật tình trạng`.
2. Nhập:
   * **Mã hoa** vừa chăm (VD: `FL01`).
   * **Mã nhân viên** thực hiện (VD: `NV02`).
   * **Độ tươi**: Chọn `1. TƯƠI` | `2. HƠI HÉO` | `3. HÉO` | `4. HỎNG`.
   * **Tình trạng nước**: Chọn `1. ĐỦ` | `2. THIẾU`.
   * **Hành động thực hiện**: Gõ ví dụ: `Tưới nước, thay bình`.
   * **Ghi chú**: Gõ thêm nếu cần (hoặc bấm `Enter`).
3. Hệ thống lưu lại lịch sử chăm sóc để quản lý theo dõi.

### Bước 3: Xử lý hoa bị héo/hỏng (Thanh lý bỏ)
Nếu kiểm tra thấy hoa trong kho đã hỏng hoặc hết hạn sử dụng:
1. Về Menu chính, bấm phím `9` (Quản lý Lô hoa).
2. Chọn mục `[3] Thanh lý/Hủy lô hoa (Hết hạn)`.
3. Nhập Mã lô hoa bị hỏng (VD: `L003`).
4. Bấm `y` để xác nhận hủy.
   * *Hệ thống sẽ chuyển trạng thái lô hoa thành `EXPIRED` (Hết hạn).*
   * *Tự động trừ số hoa hỏng này ra khỏi tồn kho để nhân viên không bán nhầm cho khách.*

---

## TÌNH HUỐNG 4: THU NGÂN XỬ LÝ TIỀN BẠC & ĐƠN HÀNG

### 1. Xem danh sách các đơn còn nợ tiền (chưa thanh toán đủ)
* Bấm `6` (Quản lý Thanh toán) -> Chọn `[5] Đơn chưa thanh toán đủ`.
* Màn hình liệt kê chi tiết: Mã đơn, Khách hàng, Tổng tiền, Đã trả bao nhiêu, Còn nợ bao nhiêu.

### 2. Khách đổi ý hủy đơn hoặc yêu cầu hoàn tiền
* Bấm `6` (Quản lý Thanh toán) -> Chọn `[4] Hủy giao dịch / Hoàn tiền`.
* Nhập Mã thanh toán cần xử lý (VD: `P008`).
* Chọn `1` để Hủy giao dịch (nếu đơn chưa làm) hoặc chọn `2` để Hoàn tiền lại cho khách (chuyển sang trạng thái `DA_HOAN_TIEN`).

### 3. Cập nhật tiến độ giao hàng cho khách
* Bấm `5` (Quản lý Đơn hàng) -> Chọn `[4] Cập nhật trạng thái đơn`.
* Nhập Mã đơn (VD: `ORD001`) và cập nhật trạng thái tương ứng:
  * `1`: DANG_CHUAN_BI (Đang cắm hoa/gói hoa)
  * `2`: DANG_GIAO (Shipper đang đi giao)
  * `3`: HOAN_THANH (Khách đã nhận hoa thành công)
  * `4`: GIAO_THAT_BAI (Không liên lạc được khách)
  * `5`: DA_HUY (Khách báo hủy đơn)

---

## TÌNH HUỐNG 5: CHỦ CỬA HÀNG / QUẢN LÝ XEM BÁO CÁO CUỐI NGÀY

Vào cuối mỗi ngày làm việc, chủ cửa hàng thực hiện kiểm tra nhanh theo các mục sau:

| Mục tiêu kiểm tra | Menu cần bấm | Thao tác chi tiết |
| :--- | :---: | :--- |
| **1. Tổng doanh thu hôm nay** | `6 → [3]` | Chọn **Doanh thu hôm nay (LINQ/Lambda)** để xem tổng số tiền thực thu về trong ngày. |
| **2. Các đơn hàng chưa giao** | `5 → [9]` | Chọn **Đơn chưa giao hôm nay** để nhắc nhở nhân viên hoàn thành trước khi đóng cửa. |
| **3. Kiểm tra hoa sắp hết hàng** | `4 → [11]` | Chọn **Hoa sắp hết tồn kho** để lên kế hoạch đặt hàng cho ngày mai. |
| **4. Kiểm tra lô hoa sắp hết hạn** | `9 → [5]` | Chọn **Lô hoa sắp hết hạn (<= 3 ngày)** để ưu tiên cắm bán trước, tránh lãng phí. |
| **5. Tính lương & hoa hồng nhân viên** | `3 → [7]` | Nhập mã nhân viên để xem: Lương cứng tháng + Hoa hồng bán hàng (2% doanh số) + Tổng thực lĩnh. |
| **6. Khách hàng chi tiêu nhiều nhất** | `1 → [8]` | Chọn **Khách nhiều đơn nhất** hoặc `[9]` **Tổng chi tiêu theo khách** để gửi mã giảm giá tri ân. |

---

## 💡 MẸO NHẬP LIỆU DÀNH CHO NGƯỜI MỚI
1. **Ở bất kỳ màn hình nào**, nhập `0` là để **Quay lại** menu trước, không lo bị thoát đột ngột.
2. **Nếu nhập sai kiểu dữ liệu** (như gõ chữ vào ô cần số): Hệ thống đã bọc sẵn bộ xử lý an toàn (`AnToan`), chương trình sẽ không bị sập mà chỉ in câu nhắc lịch sự và cho phép nhập lại.
3. **Mã đối tượng chuẩn trong hệ thống**:
   * Khách hàng: `KH001`, `KH002`...
   * Nhà cung cấp: `NCC01`, `NCC02`...
   * Nhân viên: `NV01`, `NV02`...
   * Hoa: `FL01`, `FL02`...
   * Đơn hàng: `ORD001`, `ORD002`...
   * Phiếu nhập: `PN001`, `PN002`...
   * Lô hoa: `L001`, `L002`...
   * Giao dịch thanh toán: `P001`, `P002`...
