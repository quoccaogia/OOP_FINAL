# HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ CỬA HÀNG HOA (FLOWER SHOP)

> **Tài liệu hướng dẫn thao tác chi tiết theo từng module trong Menu chính của hệ thống.**  
> Áp dụng đồng bộ cho cả 2 phiên bản: **Java** (`FlowerShopV2`) và **C#** (`FlowerShop_CSharp`).  
> **Nguyên tắc vận hành**: Người dùng không cần nhớ hay ghi chép mã ID (`KH...`, `FL...`, `ORD...`, `NCC...`, `NV...`). Tất cả đều tra cứu bằng **Số điện thoại (SĐT)** hoặc **chọn Số thứ tự (STT 1, 2, 3...)** trực tiếp trên màn hình.

---

## 📌 BẢNG TRA CỨU 9 MODULE TRONG MENU CHÍNH (0 - 9)

Khi khởi động chương trình, màn hình chính hiển thị 9 phân hệ chức năng:
* `[1]` Quản lý Khách hàng *(Tiếp đón khách, tra cứu lịch sử mua hàng theo SĐT)*
* `[2]` Quản lý Nhà cung cấp *(Tiếp nhận NCC, lưu danh bạ nhà vườn theo SĐT)*
* `[3]` Quản lý Nhân viên *(Xem danh sách, thêm/sửa nhân viên, tính lương & hoa hồng)*
* `[4]` Quản lý Hoa *(Bảng giá, tồn kho tổng, tìm kiếm hoa theo tên/danh mục)*
* `[5]` Quản lý Đơn hàng *(Tạo đơn bằng SĐT, chọn hoa theo STT, thanh toán, giao hàng)*
* `[6]` Quản lý Thanh toán *(Lịch sử giao dịch, hoàn tiền, doanh thu hôm nay)*
* `[7]` Quản lý Lịch chăm sóc *(Bảng tình trạng độ tươi, cập nhật nước tưới hàng ngày)*
* `[8]` Quản lý Phiếu nhập hàng *(Tạo phiếu nhập từ NCC, tự động sinh lô hoa)*
* `[9]` Quản lý Lô hoa trong kho *(Theo dõi hạn dùng FEFO, thanh lý lô hoa hỏng)*
* `[0]` Thoát chương trình

---

## MỤC 1: QUẢN LÝ KHÁCH HÀNG (TIẾP ĐÓN KHÁCH HÀNG) - PHÍM 1

Khi khách hàng đến cửa hàng hoặc gọi điện thoại đến:
1. Tại Menu chính, bấm phím `1` để vào **Quản lý Khách hàng**.
2. **Nghiệp vụ tiếp đón khách hàng (Bấm phím `2`)**:
   - Nhân viên xin **Số điện thoại (SĐT)** của khách hàng (10 chữ số, VD: `0901234567`) để nhập vào hệ thống.
   - **Nếu là khách cũ**: Màn hình hiển thị ngay thông tin của khách (Mã KH, Tên, SĐT, Địa chỉ, Email) và hỏi: *"Bạn có muốn cập nhật (sửa) thông tin khách hàng này không? (y/n)"*. Nếu khách đổi địa chỉ thì bấm `y` để sửa, không thì bấm `n`.
   - **Nếu là khách mới**: Hệ thống thông báo *"Không tìm thấy khách hàng nào dùng SĐT này"* và hỏi: *"👉 Tạo tài khoản khách hàng mới? (y/n)"* -> Bấm `y` -> Nhập Tên khách hàng, Địa chỉ, Email. Hệ thống sẽ tự cấp mã khách mới (VD: `KH016`) để quản lý.
3. **Khách muốn tra cứu lại các đơn hàng mình đã từng mua (Bấm phím `10`)**:
   - Khách không cần nhớ mã đơn hay mã khách! Thu ngân chỉ cần nhập **SĐT của khách**.
   - Màn hình lập tức liệt kê toàn bộ lịch sử các đơn hàng khách từng đặt: Mã đơn, Ngày giờ đặt, Ngày hẹn giao, Địa chỉ nhận, Tổng tiền, Số tiền đã thanh toán và Trạng thái đơn hàng.
4. **Các thao tác tra cứu & báo cáo khác trong Menu 1**:
   - Bấm `1`: Xem tất cả danh sách khách hàng trong hệ thống.
   - Bấm `3`: Tìm kiếm khách hàng theo tên hoặc số điện thoại.
   - Bấm `6`: Báo cáo danh sách khách đã từng đặt hàng.
   - Bấm `7`: Báo cáo khách hàng chưa từng đặt hàng.
   - Bấm `8`: Xem top khách hàng đặt nhiều đơn nhất (khách VIP).
   - Bấm `9`: Xem tổng chi tiêu của từng khách hàng từ trước đến nay.
   - Bấm `0`: Quay lại Menu chính.

---

## MỤC 2: QUẢN LÝ NHÀ CUNG CẤP (KHI NGƯỜI ĐẾN LÀ NHÀ CUNG CẤP) - PHÍM 2

Khi đại diện nhà vườn / đối tác cung cấp hoa tới chào hàng hoặc giao dịch:
1. Tại Menu chính, bấm phím `2` để vào **Quản lý Nhà cung cấp**.
2. **Nghiệp vụ tiếp nhận Nhà cung cấp (Bấm phím `2`)**:
   - Nhân viên xin **Số điện thoại của Nhà cung cấp** (VD: `0263388899`) để nhập vào hệ thống.
   - **Nếu là NCC cũ**: Màn hình hiện thông tin nhà vườn (Tên NCC, SĐT, Địa chỉ, Người đại diện) và hỏi có muốn cập nhật thông tin không.
   - **Nếu là đối tác mới**: Hệ thống hỏi *"👉 Tạo hồ sơ nhà cung cấp mới? (y/n)"* -> Bấm `y` -> Nhập Tên nhà cung cấp, Địa chỉ, Người đại diện để lưu vào danh bạ đối tác của cửa hàng.
3. **Các thao tác tra cứu & báo cáo khác trong Menu 2**:
   - Bấm `1`: Xem tất cả danh sách nhà cung cấp đang hợp tác.
   - Bấm `3`: Tìm kiếm nhà cung cấp theo tên đối tác.
   - Bấm `4`: Xóa nhà cung cấp (chỉ xóa được nếu NCC chưa phát sinh phiếu nhập hàng).
   - Bấm `6`: Báo cáo tổng tiền cửa hàng đã nhập hàng từ từng nhà cung cấp.
   - Bấm `7`: Xem danh sách các NCC hiện đang cung cấp hoa cho tiệm.
   - Bấm `8`: Xem NCC nào cung cấp đa dạng nhiều loại hoa nhất cho shop.
   - Bấm `0`: Quay lại Menu chính.

---

## MỤC 3: QUẢN LÝ NHÂN VIÊN - PHÍM 3

Dành cho Quản lý cửa hàng quản lý nhân sự, phân quyền vai trò và tính lương:
1. Tại Menu chính, bấm phím `3` để vào **Quản lý Nhân viên**.
2. **Xem danh sách toàn bộ nhân viên (Bấm phím `1`)**:
   - Hiển thị danh sách nhân viên kèm chức vụ: `BAN_HANG` (Bán hàng), `THU_NGAN` (Thu ngân), `KHO` (Thủ kho), `CHAM_SOC` (Chăm sóc hoa).
3. **Thêm nhân viên mới (Bấm phím `4`)**:
   - Nhập Họ tên, Số điện thoại, Chọn chức vụ, Mức lương cơ bản. Hệ thống tự cấp mã nhân viên (VD: `NV06`).
4. **Cập nhật thông tin / Chức vụ nhân viên (Bấm phím `5`)**:
   - Nhập Mã nhân viên cần sửa -> Cập nhật lại họ tên, SĐT, chức vụ mới, mức lương mới.
5. **Xóa nhân viên (Bấm phím `6`)**:
   - Nhập Mã nhân viên cần xóa *(hệ thống có chốt chặn an toàn: không cho xóa nhân viên đang chịu trách nhiệm đơn hàng hay phiếu nhập)*.
6. **Báo cáo & Tính lương tháng + Hoa hồng (Bấm phím `9`)**:
   - Tự động tính tổng thu nhập của từng nhân viên = Lương cơ bản + Tiền hoa hồng (dựa trên số đơn hàng và doanh số bán được trong tháng).
7. Bấm `0`: Quay lại Menu chính.

---

## MỤC 4: QUẢN LÝ HOA (BẢNG GIÁ & TỒN KHO) - PHÍM 4

Khi khách hàng hỏi giá, kiểm tra các loại hoa hoặc nhân viên kiểm tra kho hoa:
1. Tại Menu chính, bấm phím `4` để vào **Quản lý Hoa**.
2. **Xem toàn bộ bảng giá và tồn kho hoa (Bấm phím `1`)**:
   - Hiển thị bảng tất cả loài hoa trong tiệm: Mã hoa, Tên hoa, Màu sắc, Danh mục, Đơn giá bán, Số lượng tồn kho và tổng tồn kho toàn cửa hàng.
3. **Tìm kiếm hoa theo Tên / Danh mục (Bấm phím `7`)**:
   - Nhập từ khóa (VD: `Hồng`, `Lan`, `Cúc`...) -> Hệ thống lọc ra ngay các loại hoa phù hợp kèm giá bán và tồn kho.
4. **Xem các loại hoa sắp hết hàng (Bấm phím `6`)**:
   - Liệt kê các loại hoa có tồn kho `< 10` bông để nhân viên báo bộ phận kho lên đơn nhập thêm cho ngày hôm sau.
5. **Thêm giống hoa mới vào danh mục (Bấm phím `3`)**:
   - Nhập Tên hoa, Danh mục, Màu sắc, Đơn vị tính, Đơn giá bán.
6. **Cập nhật giá bán / thông tin hoa (Bấm phím `4`)**:
   - Nhập Mã hoa cần chỉnh sửa -> Cập nhật lại giá niêm yết hoặc thông tin mô tả.
7. **Báo cáo kinh doanh theo hoa**:
   - Bấm `10`: Xem top hoa bán chạy nhất.
   - Bấm `11`: Xem hoa bán chậm / tồn kho nhiều.
   - Bấm `14`: Xem các loại hoa chưa từng có người mua.
8. Bấm `0`: Quay lại Menu chính.

---

## MỤC 5: QUẢN LÝ ĐƠN HÀNG (QUY TRÌNH BÁN HÀNG TẠI QUẦY) - PHÍM 5

Quy trình nghiệp vụ cốt lõi khi có khách mua hoa hoặc đặt hoa giao tận nơi:
1. Tại Menu chính, bấm phím `5` để vào **Quản lý Đơn hàng**.
2. **Bước 1: Tạo đơn hàng mới (Bấm phím `1`)**:
   - Hệ thống tự sinh Mã đơn hàng mới (VD: `ORD013`).
   - Màn hình yêu cầu: `SĐT khách hàng (10 số, VD: 0901234567, Enter để bỏ trống):`
     - Nếu khách cũ: Hệ thống tự động nhận diện và hiển thị tên khách hàng.
     - Nếu khách mới: Hệ thống hỏi tạo mới (`y/n`) -> Gõ Tên, Địa chỉ, Email ngay tại chỗ mà không cần thoát ra Menu 1. Khách vãng lai bấm Enter để bỏ qua.
   - **Chọn Nhân viên phụ trách**: Màn hình hiện danh sách nhân viên đánh số `1, 2...` -> Gõ số chọn nhân viên (không cần nhớ mã NV).
   - **Ngày giờ hẹn giao**: Bấm `Enter` để hệ thống tự chọn giao sau 2 tiếng (`+2h`), hoặc nhập ngày giờ hẹn trước.
   - **Địa chỉ giao hàng**: Bấm `Enter` để tự lấy địa chỉ nhà khách hàng (hoặc gõ địa chỉ người nhận nếu gửi tặng).
   - Đơn hàng được tạo thành công với trạng thái `DANG_CHUAN_BI` (Đang chuẩn bị hoa).
3. **Bước 2: Thêm hoa vào đơn hàng (Bấm phím `2`)**:
   - Màn hình in danh sách các đơn hàng hiện có kèm số thứ tự (STT). Gõ số thứ tự chọn đơn vừa tạo (không cần gõ mã ORD).
   - Màn hình in bảng các loại hoa còn trong kho kèm giá bán và tồn kho.
   - Gõ STT hoa khách chọn (VD: `1` cho Hồng đỏ) và nhập **Số lượng mua**.
   - *Hệ thống tự động trừ kho từ lô hoa có hạn dùng gần nhất (FEFO) và tính tổng tiền.*
   - Tiếp tục chọn hoa khác hoặc bấm `k` để hoàn tất thêm hoa.
4. **Bước 3: Thu tiền & Thanh toán đơn hàng (Bấm phím `3`)**:
   - Màn hình in danh sách các đơn đang nợ tiền kèm STT. Gõ STT chọn đơn cần thanh toán.
   - Chọn hình thức:
     - Gõ `1`: Thu đủ tiền đặt cọc 20% (khi khách đặt hoa trước giao sau).
     - Gõ `2`: Thu đủ 100% tiền đơn.
   - Phương thức: Bấm `Enter` để chọn mặc định là `TIEN_MAT` (hoặc gõ CHUYEN_KHOAN, MOMO).
   - Hệ thống cấp Mã giao dịch thành công (VD: `P014`).
5. **Bước 4: Cập nhật trạng thái đơn & Giao hàng (Bấm phím `4`)**:
   - Màn hình hiện danh sách các đơn đang xử lý kèm STT. Gõ số chọn đơn cần cập nhật:
     - Chuyển `DANG_GIAO`: Khi shipper nhận hoa đi giao *(chốt chặn: bắt buộc đã thu cọc tối thiểu 20%)*.
     - Chuyển `HOAN_THANH`: Khi khách đã nhận hoa thành công *(chốt chặn: bắt buộc đã thanh toán đủ 100%)*.
     - Chuyển `GIAO_THAT_BAI` (Khách bom hàng): Hệ thống tự động hoàn trả 100% số hoa về kho, thu ngân chọn hoàn tiền hoặc giữ cọc.
     - Chuyển `DA_HUY` (Khách hủy đơn): Tự động hoàn trả 100% hoa về kho, giữ tiền cọc.
6. **Bước 5: Xem đơn hàng của khách bằng SĐT (Bấm phím `11`)**:
   - Nhập SĐT khách hàng -> Tra cứu nhanh toàn bộ các đơn của khách.
7. Bấm `0`: Quay lại Menu chính.

---

## MỤC 6: QUẢN LÝ THANH TOÁN (SỔ SÁCH & HOÀN TIỀN) - PHÍM 6

Dành cho Thu ngân theo dõi giao dịch và xử lý hoàn tiền:
1. Tại Menu chính, bấm phím `6` để vào **Quản lý Thanh toán**.
2. **Xem tất cả lịch sử giao dịch (Bấm phím `1`)**:
   - Bảng kê chi tiết: Mã giao dịch (P001...), Mã đơn hàng, Số tiền, Phương thức thanh toán, Thời gian và Trạng thái.
3. **Lọc giao dịch theo phương thức (Bấm phím `2`)**:
   - Chọn xem riêng giao dịch: `1. TIEN_MAT`, `2. CHUYEN_KHOAN`, `3. MOMO`, `4. THE_TIN_DUNG`.
4. **Xem tổng doanh thu hôm nay (Bấm phím `3`)**:
   - Hệ thống tính tổng tiền thực thu thành công trong ngày hôm nay.
5. **Hủy giao dịch / Hoàn tiền lại cho khách (Bấm phím `4`)**:
   - Khi thu ngân nhập nhầm hoặc khách yêu cầu hoàn tiền: Nhập Mã giao dịch (VD: `P001`).
   - Chọn `1. Hủy giao dịch` hoặc `2. Hoàn tiền lại cho khách` (chuyển sang `DA_HOAN_TIEN`).
6. Bấm `0`: Quay lại Menu chính.

---

## MỤC 7: QUẢN LÝ LỊCH CHĂM SÓC HOA (ĐẦU CA SÁNG) - PHÍM 7

Dành cho nhân viên chăm sóc hoa hàng ngày tại cửa hàng:
1. Tại Menu chính, bấm phím `7` để vào **Quản lý Lịch chăm sóc**.
2. **Xem bảng tổng quan tình trạng chăm sóc hoa (Bấm phím `1`)**:
   - Màn hình hiện bảng tất cả các loài hoa kèm: Lần chăm sóc cuối, Độ tươi hiện tại, Nước tưới, Biểu tượng cảnh báo trực quan (`✅ Ổn`, `🟠 Cần chú ý`, `🔴 Cần xử lý`, `🟡 Lâu chưa kiểm tra`).
3. **Cập nhật tình trạng sau khi chăm sóc (Bấm phím `2`)**:
   - Màn hình hiện danh sách hoa kèm STT. Bấm số chọn loài hoa vừa chăm sóc.
   - Màn hình hiện danh sách nhân viên kèm STT. Bấm số chọn nhân viên thực hiện.
   - Chọn **Độ tươi**: `1. TƯƠI` | `2. HƠI HÉO` | `3. HÉO` | `4. HỎNG`.
   - Chọn **Tình trạng nước**: `1. ĐỦ` | `2. THIẾU`.
   - Gõ hành động: Bấm `Enter` để lấy mặc định là `"Tưới nước"` hoặc gõ cụ thể (VD: `"Tưới nước, thay bình, tỉa lá úa"`).
   - *Nếu hoa bị HÉO hoặc HỎNG*: Hệ thống cảnh báo ngay lập tức để chuyển sang thanh lý hủy lô hoa, tránh bán nhầm cho khách!
4. **Xem các loại hoa cần chăm sóc hôm nay (Bấm phím `5`)**:
   - Lọc nhanh các loài hoa đang bị thiếu nước hoặc lâu chưa được tưới.
5. Bấm `0`: Quay lại Menu chính.

---

## MỤC 8: QUẢN LÝ PHIẾU NHẬP HÀNG (KHI NHÀ CUNG CẤP GIAO HOA) - PHÍM 8

Dành cho nhân viên kho khi xe hoa của nhà cung cấp tới giao hàng:
1. Tại Menu chính, bấm phím `8` để vào **Quản lý Phiếu nhập hàng**.
2. **Tạo phiếu nhập mới (Bấm phím `3` ở bản Java / Bấm phím `2` ở bản C#)**:
   - Hệ thống tự sinh Mã phiếu nhập (VD: `PN006`).
   - **Chọn Nhà cung cấp**: Màn hình liệt kê danh sách các nhà vườn kèm số thứ tự `1, 2, 3...` -> Gõ số chọn đối tác (VD: gõ `1` chọn Vườn Hoa Đà Lạt).
   - **Chọn Nhân viên nhận hàng**: Màn hình liệt kê danh sách nhân viên kho -> Gõ số chọn nhân viên.
   - **Chọn loài hoa nhập vào**: Màn hình hiển thị danh sách các loại hoa hiện có trong cửa hàng kèm STT và tồn kho. Gõ số chọn loại hoa nhập (hoặc gõ `0` nếu là giống hoa mới hoàn toàn).
   - Nhập **Số lượng cành nhập** (VD: `100`) và **Đơn giá vốn mỗi cành** (VD: `3000`).
   - *Hệ thống tự động:*
     - Sinh Mã Lô hoa mới (VD: `L013`).
     - **Tự động cộng dồn ngay 100 cành vào số lượng tồn kho** của loài hoa đó.
     - Tự động tính Hạn sử dụng tươi (`+7 ngày`).
   - Tiếp tục chọn hoa khác hoặc bấm `Enter` để kết thúc nhập hoa.
   - Hệ thống tính tổng tiền phiếu nhập và tự động chuyển phiếu sang trạng thái `HOAN_THANH`.
3. **Xem tất cả phiếu nhập hàng (Bấm phím `1`)**:
   - Liệt kê toàn bộ các đợt nhập hàng kèm ngày nhập, nhà cung cấp, tổng tiền và trạng thái.
4. **Tìm phiếu nhập theo mã (Bấm phím `2` ở Java)**:
   - Nhập mã phiếu (VD: `PN001`) để xem chi tiết.
5. Bấm `0`: Quay lại Menu chính.

---

## MỤC 9: QUẢN LÝ LÔ HOA TRONG KHO (HẠN DÙNG & THANH LÝ) - PHÍM 9

Dành cho Thủ kho theo dõi hạn sử dụng từng đợt hoa và thanh lý hoa hỏng:
1. Tại Menu chính, bấm phím `9` để vào **Quản lý Lô hoa trong kho**.
2. **Xem tất cả các lô hoa trong kho (Bấm phím `1`)**:
   - Màn hình hiển thị bảng đầy đủ thông tin từng lô: Mã lô, Mã phiếu nhập, Tên hoa, Nhà cung cấp, Số lượng nhập, Số lượng còn, Giá vốn, Hạn sử dụng và Trạng thái (`AVAILABLE`, `EXPIRED`, `SOLD_OUT`).
3. **Thanh lý / Hủy lô hoa hết hạn hoặc bị hỏng (Bấm phím `3`)**:
   - Màn hình in danh sách các lô hoa kèm số thứ tự (STT).
   - Gõ STT chọn lô hoa hỏng cần thanh lý (VD: gõ `1` chọn lô `L003`).
   - Bấm `y` xác nhận thanh lý toàn bộ số hoa còn lại trong lô.
   - *Hệ thống tự động:*
     - Chuyển trạng thái lô thành `EXPIRED` (Hết hạn).
     - **Tự động trừ sạch số lượng hoa hỏng này ra khỏi tồn kho** của shop để không bán nhầm cho khách.
4. **Xem các lô hoa sắp hết hạn trong vòng 3 ngày (Bấm phím `5`)**:
   - Báo cáo các lô hoa cận date để cửa hàng ưu tiên bán trước (xuất kho FEFO) hoặc bó thành các lẵng hoa khuyến mãi giảm giá.
5. **Đồng bộ tồn kho từ lô hoa (Bấm phím `6`)**:
   - Tự động cộng tổng số lượng hoa còn trong tất cả các lô `AVAILABLE` và cập nhật chuẩn xác vào bảng tồn kho nếu có sai lệch.
6. Bấm `0`: Quay lại Menu chính.

---

## 🛡️ TỔNG HỢP CÁC CHỐT CHẶN AN TOÀN TRONG HỆ THỐNG
1. **Không cho bán âm kho**: Không thể nhập số lượng bán lớn hơn tồn kho hiện tại.
2. **Khóa đơn khi đã phát sinh tiền**: Đơn đã thu tiền cọc hoặc thanh toán thì không được tùy tiện sửa chi tiết hoa nhằm bảo vệ số liệu kế toán.
3. **Bắt buộc cọc tối thiểu 20% mới cho đi giao (`DANG_GIAO`)**.
4. **Bắt buộc thanh toán đủ 100% mới cho hoàn thành (`HOAN_THANH`)**.
5. **Tự động hoàn 100% hoa về kho khi hủy đơn hoặc giao thất bại**.
6. **Xuất kho theo hạn dùng (FEFO)**: Ưu tiên xuất lô có hạn dùng gần nhất trước.
