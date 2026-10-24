# HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ CỬA HÀNG HOA (FLOWER SHOP)

> **Tài liệu hướng dẫn nghiệp vụ thực tế**: Dành cho Thu ngân, Nhân viên bán hàng, Thủ kho, Nhân viên chăm sóc hoa và Quản lý cửa hàng.  
> **Nguyên tắc thiết kế**: Bám sát 100% thực tế vận hành — **người dùng không cần nhớ hay ghi chép bất kỳ mã ID nào** (Mã khách hàng, Mã hoa, Mã đơn, Mã NCC, Mã NV...). Tất cả đều hỗ trợ tra cứu bằng **Số điện thoại (SĐT)** hoặc **chọn số thứ tự (STT 1, 2, 3...)** trực tiếp trên màn hình.  
> Áp dụng đồng bộ cho cả 2 phiên bản: **Java** (`FlowerShopV2`) và **C#** (`FlowerShop_CSharp`).

---

## 📌 BẢNG TRA CỨU MENU CHÍNH (0 - 9)

Khi khởi động chương trình, màn hình chính hiển thị 9 phân hệ chức năng:
* `[1]` Quản lý Khách hàng *(Tiếp đón khách, xem lịch sử đặt hàng theo SĐT)*
* `[2]` Quản lý Nhà cung cấp *(Thông tin đối tác, địa chỉ, SĐT nhà vườn)*
* `[3]` Quản lý Nhân viên *(Phân quyền: Thu ngân, Bán hàng, Kho, Chăm sóc)*
* `[4]` Quản lý Hoa *(Bảng giá, tồn kho tổng, tìm kiếm theo tên)*
* `[5]` Quản lý Đơn hàng *(Tạo đơn bằng SĐT, chọn hoa theo STT, thanh toán, giao hàng)*
* `[6]` Quản lý Thanh toán *(Lịch sử giao dịch, hoàn tiền, doanh thu)*
* `[7]` Quản lý Lịch chăm sóc hoa *(Bảng tình trạng độ tươi, nước tưới hàng ngày)*
* `[8]` Quản lý Phiếu nhập hàng *(Nhập hoa từ nhà vườn, sinh lô tự động)*
* `[9]` Quản lý Lô hoa trong kho *(Theo dõi hạn dùng FEFO, thanh lý hoa hỏng)*
* `[0]` Thoát chương trình

---

## 🌸 TÌNH HUỐNG 1: KHÁCH HÀNG TỚI MUA HOA / ĐẶT ĐƠN HÀNG MỚI

Quy trình thực tế tại quầy thu ngân diễn ra liền mạch chỉ trong Menu `5` mà không cần mở nhiều màn hình rời rạc.

### Bước 1: Tạo đơn hàng mới bằng Số điện thoại (SĐT)
1. Tại Menu chính, bấm phím `5` (**Quản lý Đơn hàng**) -> Chọn tiếp mục `[1] Tạo đơn hàng mới`.
2. Hệ thống tự động cấp Mã đơn mới (VD: `ORD013`).
3. Màn hình yêu cầu: `SĐT khách hàng (10 số, VD: 0901234567, Enter để bỏ trống):`
   * **Nếu là khách cũ**: Hệ thống tự động nhận diện và hiển thị: `✅ Khách hàng: Nguyễn Văn A (Mã: KH001)`.
   * **Nếu là khách mới (hoặc chưa có SĐT)**: Hệ thống thông báo chưa có và hỏi: `👉 Tạo khách hàng mới với SĐT này? (y/n):`
     - Bấm `y` -> Nhập nhanh: **Tên khách hàng**, **Địa chỉ**, **Email** ngay tại chỗ. Hệ thống tự động tạo hồ sơ khách hàng mới và gắn vào đơn.
     - *(Nếu là khách vãng lai mua nhanh không muốn lưu SĐT: Bấm `Enter` để bỏ trống).*
4. **Chọn Nhân viên bán hàng**: Màn hình liệt kê danh sách nhân viên:
   ```text
   --- CHỌN NHÂN VIÊN PHỤ TRÁCH ---
     1. Nguyễn Văn A         (Mã: NV01 - BAN_HANG)
     2. Trần Thị B           (Mã: NV02 - THU_NGAN)
   Nhập số chọn nhân viên (1-2): 1
   ```
   *Chỉ cần gõ `1` hoặc `2` để chọn nhân viên, không cần gõ mã `NV01`.*
5. **Ngày giờ giao hàng**: Bấm `Enter` để hệ thống tự động chọn giao sau 2 tiếng (`+2h`), hoặc nhập ngày giờ hẹn giao nếu khách đặt trước.
6. **Địa chỉ giao hàng**: Bấm `Enter` để hệ thống tự động lấy địa chỉ nhà đã lưu của khách (hoặc gõ địa chỉ mới nếu khách muốn gửi tặng người khác).
7. Hệ thống báo: `✅ Tạo đơn thành công!` với trạng thái ban đầu là `DANG_CHUAN_BI` (Đang chuẩn bị hoa).

---

### Bước 2: Chọn hoa đưa vào đơn hàng
1. Vẫn ở Menu `5`, chọn tiếp mục `[2] Thêm hoa vào đơn`.
2. Màn hình hiển thị danh sách các đơn hàng hiện có kèm số thứ tự (STT):
   ```text
   DANH SÁCH ĐƠN HÀNG HIỆN CÓ:
   STT  | Mã Đơn   | Khách Hàng             | Trạng Thái        
   ----------------------------------------------------------
   1    | ORD013   | Nguyễn Văn A           | Đang chuẩn bị hoa 
   2    | ORD012   | Trần Thị Lan           | Đang chuẩn bị hoa 
   ```
   👉 Gõ số `1` để chọn ngay đơn `ORD013` vừa tạo *(không cần nhớ hay gõ chữ `ORD013`)*.
3. Màn hình hiển thị bảng các loại hoa còn hàng trong kho:
   ```text
   DANH SÁCH HOA CÒN TRONG KHO:
   STT  | Mã Hoa   | Tên Hoa                   | Đơn Giá      | Tồn  
   ----------------------------------------------------------------
   1    | FL01     | Bó Hoa Hồng Đỏ Cổ Điển    | 250.000 đ    | 33   
   2    | FL02     | Bó Hoa Hướng Dương Rực Rỡ | 200.000 đ    | 18   
   3    | FL04     | Chậu Lan Hồ Điệp 3 Nhánh  | 850.000 đ    | 5    
   ```
4. Gõ số thứ tự của hoa khách chọn (VD: gõ `1` để chọn Hoa Hồng Đỏ).
5. Nhập **Số lượng mua** (VD: gõ `2`).
   * *Hệ thống tự động trừ kho 2 bông từ lô hoa có hạn sử dụng gần nhất (FEFO) và tính tiền.*
6. Hệ thống hỏi: `Thêm hoa khác vào đơn? (c/K, Enter = c):`
   * Bấm `Enter` nếu khách mua thêm bó hoa khác.
   * Gõ `k` nếu đã chọn xong.

---

### Bước 3: Thu tiền và thanh toán đơn hàng
1. Vẫn ở Menu `5`, chọn tiếp mục `[3] Thanh toán đơn hàng`.
2. Màn hình hiển thị danh sách các đơn hàng chưa thu đủ tiền:
   ```text
   DANH SÁCH ĐƠN HÀNG CHƯA THU ĐỦ TIỀN:
   STT  | Mã Đơn   | Khách Hàng       | Trạng Thái        | Tổng Tiền    | Đã Trả 
   ------------------------------------------------------------------------------
   1    | ORD013   | Nguyễn Văn A     | Đang chuẩn bị hoa | 500.000 đ    | 0 đ    
   ```
   👉 Gõ số `1` để chọn đơn cần thanh toán.
3. Màn hình hiển thị tổng tiền và 2 phương án thanh toán chuẩn nghiệp vụ:
   ```text
   Chọn hình thức thanh toán:
     1. Trả đủ tiền cọc (20%) : 100.000 đ
     2. Trả hết cả tiền đơn   : 500.000 đ
     0. Hủy thao tác
   Nhập số chọn: 
   ```
   * **Bấm `1`**: Nếu khách đặt hoa trước giao sau (thu trước 20% tiền cọc).
   * **Bấm `2`**: Nếu khách mua lấy ngay hoặc muốn trả đủ 100%.
4. Chọn hình thức trả tiền:
   * Bấm `Enter` để chọn mặc định là **TIEN_MAT** (Tiền mặt).
   * Hoặc gõ `CHUYEN_KHOAN`, `MOMO`.
5. Hệ thống in thông báo: `✅ Thanh toán thành công!` và cấp Mã giao dịch (VD: `P014`).

---

## 🔍 TÌNH HUỐNG 2: KHÁCH ĐẾN HỎI LỊCH SỬ ĐƠN HÀNG / TRA CỨU ĐƠN CŨ

Khách hàng đến quầy hoặc gọi điện không bao giờ nhớ Mã đơn hàng (`ORD...`) hay Mã khách (`KH...`). Thu ngân thao tác:
1. Vào Menu `5` (Đơn hàng) chọn mục `[11] Đơn hàng của 1 khách (Nhập SĐT)`.  
   *(Hoặc vào Menu `1` Khách hàng chọn mục `[10] Đơn hàng của 1 khách (Nhập SĐT)`).*
2. Nhập **Số điện thoại** của khách (VD: `0901234567`).
3. Màn hình lập tức hiển thị toàn bộ lịch sử đơn hàng của khách hàng đó:
   * Mã đơn hàng
   * Ngày giờ đặt & Ngày hẹn giao
   * Địa chỉ giao hàng
   * Tổng tiền & Số tiền đã thanh toán
   * Trạng thái đơn (`Đang chuẩn bị hoa`, `Đang giao hàng`, `Hoàn thành`...)

---

## 🚚 TÌNH HUỐNG 3: CẬP NHẬT TIẾN ĐỘ ĐƠN HÀNG / GIAO HÀNG / HỦY ĐƠN

1. Tại Menu `5`, chọn mục `[4] Cập nhật trạng thái đơn`.
2. Màn hình hiển thị danh sách tất cả các đơn hàng đang xử lý dở dang:
   ```text
   DANH SÁCH ĐƠN HÀNG ĐANG XỬ LÝ:
   STT  | Mã Đơn   | Khách Hàng       | Trạng Thái        
   ----------------------------------------------------------
   1    | ORD013   | Nguyễn Văn A     | Đang chuẩn bị hoa 
   ```
   👉 Gõ số `1` để chọn đơn.
3. Hệ thống hiển thị các trạng thái hợp lệ tiếp theo:
   * **1. DANG_GIAO** *(Shipper bắt đầu đi giao hàng)*:
     - *Chốt chặn an toàn*: Đơn bắt buộc phải thu đủ tối thiểu 20% tiền cọc mới cho phép giao hàng.
   * **2. HOAN_THANH** *(Khách đã nhận hoa thành công)*:
     - *Chốt chặn an toàn*: Khách bắt buộc phải thanh toán đủ 100% tiền đơn mới được bấm hoàn thành.
   * **3. GIAO_THAT_BAI** *(Shipper không gọi được khách / khách bom hàng)*:
     - Hệ thống **tự động hoàn trả 100% số lượng hoa trong đơn về lại kho** để không làm thất thoát hoa.
     - Cho phép thu ngân chọn: hoàn lại tiền cho khách hoặc giữ lại tiền cọc theo chính sách shop.
   * **4. DA_HUY** *(Khách gọi điện hủy đơn khi chưa giao)*:
     - Hệ thống **tự động hoàn trả toàn bộ hoa về lại kho**.
     - Giữ lại số tiền cọc khách đã trả (theo hợp đồng đặt cọc).

---

## 📦 TÌNH HUỐNG 4: NHÀ CUNG CẤP TỚI GIAO HOA (NHẬP HÀNG VÀO KHO)

Khi xe chở hoa từ nhà vườn tới cửa hàng:
1. Tại Menu chính, bấm phím `8` (**Quản lý Phiếu nhập hàng**) -> Chọn `[3] Tạo phiếu nhập mới` (hoặc `[2]` ở bản C#).
2. Hệ thống tự động cấp Mã phiếu mới (VD: `PN006`).
3. **Chọn Nhà cung cấp**: Màn hình liệt kê danh sách đối tác:
   ```text
   --- CHỌN NHÀ CUNG CẤP ---
     1. Vườn Hoa Đà Lạt Farm    (Mã: NCC01)
     2. Hoa Tươi Miền Tây Co.   (Mã: NCC02)
     3. Công ty Hoa Nhập Khẩu   (Mã: NCC03)
   Chọn NCC (1-3): 1
   ```
   👉 Gõ số `1` để chọn Đà Lạt Farm *(không cần gõ mã `NCC01`)*.
4. **Chọn Nhân viên nhận hàng**: Màn hình liệt kê danh sách nhân viên kho, gõ số `1` hoặc `2` để chọn.
5. **Chọn loài hoa nhập vào kho**:
   ```text
   --- CHỌN LOẠI HOA NHẬP ---
     1. Bó Hoa Hồng Đỏ Cổ Điển (Mã: FL01 - Tồn: 33)
     2. Bó Hoa Hướng Dương     (Mã: FL05 - Tồn: 18)
     0. Thêm loại Hoa mới (Nếu cửa hàng chưa từng bán giống hoa này)
   Chọn hoa (1-2): 1
   ```
   👉 Gõ số `1` để chọn Hoa Hồng Đỏ.
6. Nhập **Số lượng nhập** (VD: `100` cành) và **Giá vốn mỗi cành** (VD: `3000` đ).
7. Hệ thống tự động:
   * Sinh một Mã Lô hoa mới (VD: `L013`).
   * **Tự động cộng dồn ngay 100 cành vào số lượng tồn kho** của loài hoa đó.
   * Tự động thiết lập Hạn sử dụng tươi (`+7 ngày`).
8. Nếu chuyến xe còn các loại hoa khác, tiếp tục chọn số để nhập tiếp. Bấm `Enter` để kết thúc nhập hoa.
9. Hệ thống tự động tính tổng tiền phiếu nhập và chốt hoàn thành phiếu nhập `HOAN_THANH`.

---

## 🌿 TÌNH HUỐNG 5: CÔNG VIỆC CHĂM SÓC HOA HÀNG NGÀY (ĐẦU CA SÁNG)

Mỗi buổi sáng khi mở cửa hàng, nhân viên chăm sóc hoa thực hiện:
1. Tại Menu chính, bấm phím `7` (**Quản lý Lịch chăm sóc**).
2. Chọn mục `[1] Bảng tình trạng chăm sóc`.
   * Màn hình hiển thị bảng toàn bộ các loài hoa trong tiệm kèm: Lần kiểm tra cuối, Độ tươi, Nước tưới, Biểu tượng cảnh báo trực quan (`✅ Ổn`, `🟠 Cần chú ý`, `🔴 Cần xử lý`, `🟡 Lâu chưa kiểm tra`).
3. Chọn mục `[2] Cập nhật tình trạng`:
   * Màn hình hiển thị danh sách hoa kèm số thứ tự (STT 1..N). Gõ số chọn loài hoa vừa chăm sóc.
   * Màn hình hiển thị danh sách nhân viên. Gõ số chọn nhân viên thực hiện.
   * Chọn **Độ tươi**: Gõ `1. TƯƠI` | `2. HƠI HÉO` | `3. HÉO` | `4. HỎNG`.
   * Chọn **Tình trạng nước**: Gõ `1. ĐỦ` | `2. THIẾU`.
   * Gõ hành động: Bấm `Enter` để dùng mặc định là `"Tưới nước"` hoặc gõ ví dụ: `"Thay nước bình, tỉa bớt lá úa"`.
   * *Nếu chọn hoa bị HÉO hoặc HỎNG*: Hệ thống lập tức cảnh báo nhân viên cần tiến hành thanh lý hủy bỏ lô hoa hỏng để không bán nhầm cho khách!

---

## 🗑️ TÌNH HUỐNG 6: THANH LÝ / HỦY LÔ HOA HẾT HẠN HOẶC BỊ HỎNG

Khi phát hiện một lô hoa đã để quá lâu, hoa bị héo úa hoặc dập nát:
1. Tại Menu chính, bấm phím `9` (**Quản lý Lô hoa trong kho**).
2. Chọn mục `[3] Thanh lý/Hủy lô hoa (Hết hạn)`.
3. Màn hình in danh sách tất cả các lô hoa đang có trong tiệm kèm STT, Tên hoa, Số lượng còn và Hạn sử dụng:
   ```text
   DANH SÁCH LÔ HOA HIỆN CÓ:
   STT  | Mã Lô    | Tên Hoa                | SL Còn   | Hạn SD     | Trạng Thái 
   -----------------------------------------------------------------------------
   1    | L003     | Lẵng Hoa Cúc Tơ Trắng  | 8        | 02/10/2026 | AVAILABLE  
   2    | L002     | Bó Hoa Hồng Vàng Ấm Áp | 1        | 03/10/2026 | AVAILABLE  
   ```
   👉 Gõ số `1` để chọn lô `L003`.
4. Màn hình hỏi: `Bạn có chắc chắn muốn thanh lý toàn bộ số hoa còn lại trong lô L003? (y/n):` -> Bấm `y`.
5. Hệ thống thực hiện 2 việc cùng lúc:
   * Chuyển trạng thái lô hoa thành `EXPIRED` (Hết hạn/Hỏng).
   * **Tự động trừ sạch số lượng hoa hỏng này ra khỏi tồn kho** của cửa hàng để nhân viên bán hàng không lấy phải hoa hỏng.

---

## 📊 TÌNH HUỐNG 7: QUẢN LÝ XEM BÁO CÁO DOANH THU & KHO CUỐI NGÀY

Vào cuối ngày trước khi đóng cửa, quản trị viên mở các mục báo cáo sau:

| Báo cáo cần xem | Thao tác trên Menu | Kết quả nhận được |
|:---|:---|:---|
| **Doanh thu hôm nay** | Menu `5` -> chọn `[10]` | Tổng tiền thu được trong ngày (VND) |
| **Đơn hàng chưa giao hôm nay** | Menu `5` -> chọn `[9]` | Danh sách đơn hẹn giao hôm nay mà chưa chuyển trạng thái sang HOAN_THANH |
| **Thống kê đơn theo trạng thái** | Menu `5` -> chọn `[12]` | Số đơn đang chuẩn bị, đang giao, đã xong, đã hủy |
| **Hoa sắp hết hàng (< 10 bông)** | Menu `4` -> chọn `[6]` | Danh sách hoa cần đặt nhà vườn nhập bổ sung cho ngày mai |
| **Lô hoa sắp hết hạn (<= 3 ngày)** | Menu `9` -> chọn `[5]` | Danh sách các lô hoa cần ưu tiên bán trước hoặc làm chương trình giảm giá |
| **Khách hàng thân thiết nhiều đơn nhất** | Menu `1` -> chọn `[8]` | Danh sách khách hàng VIP mua nhiều nhất |
| **Doanh số theo từng nhân viên** | Menu `5` -> chọn `[17]` | Xếp hạng nhân viên bán được nhiều đơn nhất |

---

## 🛡️ TỔNG HỢP CÁC CHỐT CHẶN AN TOÀN TRONG HỆ THỐNG

1. **Không cho bán âm kho**: Khi chọn hoa vào đơn, nếu nhập số lượng lớn hơn tồn kho hiện tại, hệ thống lập tức từ chối và thông báo số lượng tối đa đang có.
2. **Khóa đơn khi đã thu tiền**: Đơn hàng khi đã phát sinh thu tiền (cọc hoặc toàn bộ) thì không cho phép thêm hoặc bớt hoa tùy tiện (tránh làm sai lệch số tiền khách đã trả). Muốn sửa phải vào Hủy giao dịch hoàn tiền trước.
3. **Bắt buộc cọc mới cho đi giao**: Đơn hàng muốn chuyển sang trạng thái `DANG_GIAO` bắt buộc phải thu tối thiểu 20% tiền cọc.
4. **Bắt buộc trả đủ 100% mới cho hoàn thành**: Đơn hàng muốn chuyển sang `HOAN_THANH` bắt buộc tiền thu phải bằng tổng tiền đơn.
5. **Tự động hoàn kho khi đơn hỏng/hủy**: Bất kỳ đơn nào bị hủy hoặc giao thất bại đều tự động hoàn trả 100% số lượng hoa về kho, không bao giờ làm mất mát tồn kho của tiệm.
6. **Xuất kho theo hạn sử dụng (FEFO)**: Khi bán hoa, hệ thống tự động trừ từ lô hoa có hạn dùng gần nhất trước, giúp hoa trong tiệm luôn tươi mới.
