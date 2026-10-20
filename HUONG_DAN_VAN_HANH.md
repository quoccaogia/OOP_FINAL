# SỔ TAY VẬN HÀNH — FLOWERSHOP V2

> Viết theo **tình huống thực tế**: khách bước vào thì làm gì, nhập hoa thì kiểm tra gì trước.
> Cách đọc: `5 → 2` = vào menu chính **[5]**, chọn tiếp **[2]**.
> **Hệ thống:** là những gì màn hình in ra · **Bạn:** là việc bạn gõ.

---

# A. KHÁCH ĐẾN CỬA HÀNG

## A1. Khách đã từng mua (khách quen)

**1.** Chào khách và hỏi:
> *"Anh/chị đã mua hoa ở shop mình lần nào chưa ạ? Cho em xin số điện thoại để kiểm tra."*

**2.** Vào `1 → 2. Tiếp đón khách hàng`, nhập SĐT (nhập **đủ 10 số**).

**3.** **Hệ thống** in một trong hai kết quả:

| Hệ thống báo | Nghĩa là | Bạn làm tiếp |
|---|---|---|
| `✅ Tìm thấy khách hàng:` + bảng | Khách đã có trong hệ thống | Sang bước 4 |
| `❌ Không tìm thấy khách hàng nào dùng SĐT này.` | Khách mới (hoặc gõ sai số) | Sang **A2** |

**4.** Hệ thống hỏi: `👉 Bạn có muốn cập nhật (sửa) thông tin khách hàng này không? (y/n)`

- Khách không đổi gì → gõ `n`.
- Khách đổi địa chỉ/email → gõ `y`, rồi nhập:
  - `Tên mới (Enter = giữ nguyên)`
  - `Địa chỉ mới (Enter = giữ nguyên)`
  - `Email mới (Enter = giữ nguyên)`

**5.** Sang **B1** để tạo đơn.

> ⚠️ Nếu gõ thiếu số (VD chỉ `0909`), hệ thống tìm **gần đúng** và có thể ra nhiều khách → dễ nhầm người. Luôn nhập đủ 10 số.

## A2. Khách chưa từng mua (khách mới)

**1.** Ở bước A1 hệ thống báo không tìm thấy, nó hỏi:
`👉 Tạo tài khoản khách hàng mới? (y/n)` → gõ `y`.

**2.** Nhập lần lượt:

| Ô nhập | Gợi ý |
|---|---|
| `SĐT khách hàng` | 10 số, VD `0901234567` (chỉ hỏi lại nếu số vừa nhập chưa hợp lệ) |
| `Tên KH` | VD `Nguyễn Văn A` |
| `Địa chỉ` | VD `Quận 1, TP.HCM` (có thể Enter bỏ trống) |
| `Email` | VD `ten@gmail.com` (Enter nếu không có) |

**3.** **Hệ thống:** `✅ Thêm khách hàng thành công!` → sang **B1**.

> Khách mới cũng có thể tạo luôn trong lúc tạo đơn (B1 bước 3), không cần làm A1/A2 trước.

## A3. Khách chỉ hỏi giá, chưa mua

- Tìm theo tên: `4 → 7` → nhập từ khóa (VD `Hồng`).
- Tìm theo mã: `4 → 2`.
- Lọc theo giá: `4 → 8` (nhập giá min, giá max).
- Lọc theo màu: `4 → 9`.

Không cần tạo đơn.

---

# B. TẠO ĐƠN & THU TIỀN

## B1. Tạo đơn hàng

**1.** Vào `5 → 1. Tạo đơn hàng mới`.

**2.** **Hệ thống:** `Mã đơn tự sinh: ORD0xx` → ghi lại mã này.

**3.** **Hệ thống:** `(Bấm Enter nếu là khách mới, chưa có SĐT)` rồi hỏi `SĐT khách hàng (10 số, ...)`.

| Tình huống | Bạn gõ |
|---|---|
| Khách đã có trong hệ thống | SĐT của khách |
| Khách mới, chưa nhập ở A2 | Enter (hệ thống sẽ hỏi tạo khách mới) |

**4.** Nếu khách chưa có, hệ thống hỏi `👉 Tạo khách hàng mới với SĐT này? (y/n)` → `y` → nhập tên/địa chỉ/email (như A2 bước 2).

**5.** **Hệ thống** in danh sách nhân viên → `Nhập số chọn nhân viên (1-N)` → gõ số của người phụ trách đơn.

**6.** `Ngày giao (yyyy-MM-dd HH:mm:ss, Enter = +2h)`:

- Enter → hệ thống lấy **bây giờ + 2 giờ**.
- Hoặc gõ VD `2026-10-25 15:00:00`.
- ⚠️ Ngày giao **không được trước ngày đặt** — gõ sai hệ thống bắt nhập lại.
- Sai định dạng cũng bắt nhập lại (không tự lấy mặc định nữa).

**7.** `Địa chỉ giao (Enter = lấy địa chỉ khách)`:

- Enter → lấy địa chỉ đã lưu của khách.
- Khách đổi điểm giao → gõ địa chỉ mới.

**8.** **Hệ thống:** `✅ Tạo đơn thành công! Mã đơn: ORD0xx` → `👉 Hãy vào chức năng [2] để thêm hoa vào đơn.`

> Đơn vừa tạo **chưa có hoa** nên chưa giao/hoàn thành được. Phải sang B2.

## B2. Thêm hoa vào đơn

**1.** Vào `5 → 2. Thêm hoa vào đơn`.

**2.** **Hệ thống** in danh sách đơn (đơn mới nhất ở trên) → `Nhập STT đơn muốn thêm hoa (0 để hủy)` → gõ số thứ tự của đơn vừa tạo.

**3.** Hệ thống kiểm tra, có thể **từ chối** và in lý do:

| Hệ thống báo | Nguyên nhân | Cách xử lý |
|---|---|---|
| `❌ TỪ CHỐI THÊM HOA!` + trạng thái | Đơn đã sang *Đang giao / Hoàn thành / Đã hủy / Giao thất bại* | Chỉ thêm được khi đơn *Đang chuẩn bị* |
| `👉 Đơn đã thu ... VND nên không thể thêm/sửa hoa.` | Đơn đã thu tiền | Hoàn tiền ở **C5** rồi thêm lại |

**4.** **Hệ thống** in danh sách hoa còn tồn → `Nhập STT hoa muốn thêm (0 để thoát)` → gõ số.

> Hoa nào **giá ≤ 0** sẽ bị ẩn kèm cảnh báo — vào menu Hoa sửa giá trước.

**5.** `Số lượng (1-N, VD: 10, 0 để hủy)` → gõ số lượng.

- Vượt tồn kho → hệ thống báo và hỏi lại.
- Gõ chữ → báo lỗi, hỏi lại (không tắt app).

**6.** **Hệ thống:** `✅ Đã thêm ... vào đơn!` → hỏi `Thêm hoa khác vào đơn? (c/K, Enter = c)`.

- Thêm tiếp → Enter rồi lặp lại bước 4–5.
- Xong → gõ `k`.

**7.** Kiểm tra lại bằng `5 → 7` (xem chi tiết đơn).

## B3. Thu tiền — chỉ có 2 mức

**1.** Vào `5 → 3. Thanh toán đơn hàng`.

**2.** **Hệ thống** in bảng đơn còn nợ, có cột **Thanh Toán**: `Chưa đặt cọc` / `Đã trả đủ cọc`.

**3.** `👉 Nhập STT đơn hàng cần thanh toán (hoặc 0 để hủy)` → gõ STT.

**4.** **Hệ thống** in: Tổng tiền / Đã trả / Còn lại / Tình trạng.

**5.** Chọn hình thức:

| Tình trạng đơn | Hệ thống hỏi | Bạn chọn |
|---|---|---|
| **Chưa trả gì** | `1. Trả đủ tiền cọc (20%)` / `2. Trả hết cả tiền đơn` | Khách cọc → `1`; khách trả luôn → `2` |
| **Đã cọc** | `Xác nhận khách trả hết ... VND? (y/n)` | `y` nếu thu đủ |

**6.** `Phương thức (TIEN_MAT/CHUYEN_KHOAN/MOMO/THE_TIN_DUNG, Enter = TIEN_MAT)` → gõ hoặc Enter.

**7.** **Hệ thống:** `✅ Thanh toán thành công! Mã GD: P00x` + `Đã trả: ... / ... VND - Đã trả đủ cọc` (hoặc `Đã trả hết đơn`).

> Sau khi thu tiền, **chi tiết hoa bị khóa**. Muốn sửa thì phải hoàn tiền (C5).

## B4. Giao hàng

**1.** Vào `5 → 4. Cập nhật trạng thái` → `Nhập Mã đơn cần cập nhật (VD: ORD001)`.

**2.** **Hệ thống** in khối thông tin đơn (**khách hàng + SĐT**, nhân viên, ngày giao, tiền đã trả) rồi mới hiện trạng thái + danh sách trạng thái được phép — để bạn chắc chắn đang đổi đúng đơn.

**3.** Chọn `Đang giao`.

**4.** Hệ thống kiểm tra:

| Kết quả | Nghĩa |
|---|---|
| `❌ CHƯA THU ĐỦ TIỀN CỌC, KHÔNG THỂ GIAO HÀNG!` | Phải thu cọc ở B3 trước |
| `✅ Cập nhật thành công sang: Đang giao hàng` | Được phép giao |

## B5. Giao xong — thu nốt & hoàn thành

**1.** Thu phần còn lại: `5 → 3` → chọn đơn → `y` (trả hết phần còn lại) → chọn phương thức.

**2.** `5 → 4` → chọn đơn → chọn `Hoàn thành`.

**3.** Hệ thống kiểm tra: phải **có giao dịch thành công** và **đã trả đủ**, nếu không sẽ báo:

- `❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG! ... chưa có giao dịch thanh toán nào thành công.`
- `❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG! ... chưa thanh toán đủ.`

**4.** Đúng điều kiện → `✅ Cập nhật thành công sang: Hoàn thành`.

---

# C. SỰ CỐ ĐƠN HÀNG

## C1. Khách đổi ý — **chưa cọc**
`5 → 4` → chọn đơn → `Đã hủy` → xác nhận `y`.
→ Hệ thống hủy đơn và **trả hoa về kho**.

## C2. Khách đổi ý — **đã cọc (chưa trả hết)**
`5 → 4` → chọn đơn → `Đã hủy`.
→ Hệ thống cảnh báo: *"Khách đã trả X VND … MẤT toàn bộ số tiền đã trả"* → xác nhận `y`.
→ Hoa về kho, khách **mất cọc**.

> Không hủy được khi khách đã trả **đủ** — làm C3.

## C3. Khách đổi ý — **đã trả đủ**
1. `6 → 4. Hủy giao dịch / Hoàn tiền` → nhập mã GD (VD `P001`) → `y`.
   → Hệ thống báo `✅ Đã hoàn tiền!` (chỉ làm được khi đơn **chưa giao**).
2. Quay lại `5 → 4` → chọn `Đã hủy`.

## C4. Giao hàng thất bại (khách không nhận)
`5 → 4` → chọn đơn đang `Đang giao` → `Giao thất bại`.

| Hệ thống hỏi | Chọn |
|---|---|
| `1. Giao thất bại + HOÀN lại X VND cho khách` | Khách không lấy hàng, shop trả tiền |
| `2. Giao thất bại + GIỮ lại X VND` | Shop giữ tiền (coi như khách mất) |

→ Hoa tự **trả về kho**, đơn kết thúc. Muốn giao lại thì tạo đơn mới.

## C5. Thu nhầm tiền / khách xin hoàn
`6 → 4` → nhập mã giao dịch (xem ở `6 → 1`) → `y`.

- Đơn **chưa giao** → hủy giao dịch (đơn quay về "chưa cọc").
- Đơn **đã đóng** (hủy / giao thất bại) → hệ thống hiểu là **hoàn tiền**.

## C6. Khách đổi địa chỉ / giờ giao
`5 → 19. Sửa thông tin đơn` → nhập mã đơn → nhập địa chỉ mới (Enter = giữ nguyên) → ngày giao mới (Enter = giữ nguyên, phải ≥ ngày đặt) → có đổi nhân viên không (`y/n`).

> Chỉ sửa được khi đơn **chưa giao**.

## C7. Muốn thêm hoa sau khi đã cọc
1. Hoàn khoản đã thu: `6 → 4`.
2. `5 → 2` thêm hoa.
3. `5 → 3` thu cọc lại.

---

# D. NHẬP HOA TỪ NHÀ CUNG CẤP

## D1. Kiểm tra NCC đã tồn tại chưa (làm trước tiên)

**Cách 1 — xem danh sách:** `2 → 1` → tìm tên NCC trong bảng.

**Cách 2 — tra bằng SĐT (nhanh):** `2 → 2. Tiếp nhận NCC` → nhập SĐT.

| Hệ thống báo | Nghĩa | Làm tiếp |
|---|---|---|
| `✅ Tìm thấy nhà cung cấp:` | NCC đã có | Sang D3 |
| `❌ Không tìm thấy NCC nào dùng SĐT này.` | NCC mới (hoặc gõ sai) | Sang D2 |

## D2. Tạo NCC mới

Ở bước D1, hệ thống hỏi `👉 Tạo hồ sơ nhà cung cấp mới? (y/n)` → `y` → nhập:

| Ô | Gợi ý |
|---|---|
| `SĐT NCC` | 10–11 số, VD `0263388899` |
| `Tên NCC` | VD `Vựa Hoa Đà Lạt` |
| `Địa chỉ` | VD `Đà Lạt, Lâm Đồng` |
| `Người đại diện` | VD `Ông Bảy` |

→ `✅ Thêm nhà cung cấp thành công!`

## D3. Kiểm tra loại hoa đã có trong danh mục chưa

`4 → 1` (xem tất cả) hoặc `4 → 2` (nhập mã hoa).

- **Đã có** → sang D4, chọn đúng loại hoa đó.
- **Chưa có** → không cần tạo trước; trong D4 chọn `0. Thêm loại Hoa mới` để tạo ngay.

## D4. Tạo phiếu nhập

**1.** `8 → 3. Tạo Phiếu Nhập Mới`.

**2.** **Hệ thống:** `Mã Phiếu Nhập tự sinh: PN00x`.

**3.** Chọn NCC: in danh sách → `Chọn NCC (1-N)` → gõ số.

**4.** Chọn nhân viên nhập hàng → gõ số.

**5.** **Hệ thống:** `✅ Đã tạo Phiếu Nhập gốc. Bắt đầu nhập hoa...` rồi in danh sách loại hoa:

```
0. Thêm loại Hoa mới (Nếu chưa có trong danh sách)
Enter. Kết thúc nhập hoa
```

**6.** Với **từng loại hoa** nhập:

| Bạn gõ | Hệ thống làm |
|---|---|
| STT loại hoa (VD `1`) | Chọn loại đó |
| `0` | Hỏi tên/danh mục/giá bán/màu → tạo loại hoa mới → quay lại chọn |
| Enter | Kết thúc việc nhập |

**7.** `Số lượng nhập (VD: 100)` → gõ số (> 0).

**8.** `Đơn giá nhập (VD: 3000)` → gõ số (> 0).

**9.** **Hệ thống:** `✅ Đã sinh Lô Hoa L00x (Hạn SD: ...)` → **tự cộng tồn kho** → quay lại bước 5 để nhập tiếp.

**10.** Nhập xong hết → gõ **Enter** → `🎉 TẠO PHIẾU NHẬP THÀNH CÔNG! Tổng tiền: ...`

> Nếu **không nhập được lô nào** mà bấm Enter, hệ thống **tự hủy phiếu rỗng** (không tạo phiếu 0 đồng).

## D5. Kiểm tra sau khi nhập

| Việc | Menu |
|---|---|
| Xem lô vừa sinh (số lượng, hạn dùng, trạng thái) | `9 → 1` |
| Xem tồn kho loại hoa vừa nhập | `4 → 2` |
| Xem lại phiếu nhập (tổng tiền, NCC) | `8 → 1` hoặc `8 → 2` |
| Tổng tiền nhập theo NCC | `8 → 6` |

> Hạn dùng mặc định = **ngày nhập + 7 ngày** (hệ thống tự tính).

## D6. Nhập dở dang (app bị tắt giữa chừng)

Phiếu sẽ kẹt ở `CHO_XU_LY` và **không tự chốt**. Xử lý:

**1.** `8 → 9. Chốt phiếu nhập dở dang` → thấy danh sách phiếu treo + số lô đã nhập.

**2.** Chọn STT phiếu → `y` xác nhận.

→ Hệ thống **tính lại tổng tiền từ các lô** và chuyển `HOAN_THANH`.

> Phiếu chưa có lô nào thì không chốt được — dùng `8 → 5` để xóa phiếu rỗng.

---

# E. CHĂM SÓC HOA

## E1. Buổi sáng — xem hôm nay chăm gì

`7 → 5. Cần chăm hôm nay`.

**Hệ thống** in danh sách những loài **không** ở trạng thái Ổn + dòng đếm `CẦN CHĂM HÔM NAY: x/y loài`.

| Nhãn | Nghĩa | Ưu tiên |
|---|---|---|
| 🔴 Cần xử lý | Hoa đã héo/hỏng | Xử lý ngay (E3) |
| 🟠 Cần chú ý | Hơi héo hoặc thiếu nước | Chăm trong ngày |
| 🟡 Lâu chưa kiểm tra | Quá 3 ngày không kiểm tra | Kiểm tra |
| ⚠️ Chưa kiểm tra | Chưa từng có bản ghi | Kiểm tra lần đầu |
| ✅ Ổn | Vừa kiểm tra, còn tươi | Bỏ qua |

## E2. Ghi nhận chăm sóc

**1.** `7 → 2. Cập nhật tình trạng` → bảng hiện ra → `Chọn STT loài hoa cần cập nhật`.

**2.** **Hệ thống** hiện: tên loài, trạng thái hiện tại, **ghi chú lần trước** (nếu có).

**3.** Chọn nhân viên thực hiện: in danh sách → `Chọn số (0 = hủy)`.

**4.** `Độ tươi (1-4)`: `1. TƯƠI` · `2. HƠI HÉO` · `3. HÉO` · `4. HỎNG`.

**5.** `Tình trạng nước (1-2)`: `1. ĐỦ` · `2. THIẾU`.

**6.** `Hành động đã làm (1-6)`: Tưới nước / Thay nước / Tỉa lá / Tỉa cành / Bón phân / **Khác**.
→ Chọn `6. Khác` thì gõ hành động của bạn (VD `Cắt gốc`).

**7.** `Ghi chú (Enter để bỏ trống, VD: Hoa hơi héo)` → gõ ghi chú.

**8.** **Hệ thống:** `✅ Đã ghi nhận CS00x cho FL0x — TƯƠI / ĐỦ` + `📝 Ghi chú: ...`.

> Mỗi lần cập nhật là **một dòng mới**, không sửa dòng cũ → giữ được lịch sử.

## E3. Hoa héo/hỏng → thanh lý lô

Sau bước E2, nếu chọn `HÉO` hoặc `HỎNG`, hệ thống tự hỏi:

**1.** In các lô còn hàng của loài đó: `1. Lô L001 còn 150 hạn 2026-10-25`.

**2.** `Thanh lý lô nào? (0 = bỏ qua)` → gõ STT lô (thường là lô đang hỏng).

**3.** `Xác nhận thanh lý lô L00x? (y/n)` → `y`.

**4.** **Hệ thống:** `✅ Đã thanh lý lô và trừ tồn kho tương ứng.`

> Lô chuyển sang `EXPIRED`, số còn về 0, tồn kho của hoa giảm đúng số đó → **không bán nhầm hoa hỏng**.

## E4. Gõ sai ghi chú

`7 → 3. Sửa ghi chú`:

- Gõ **mã CS** (VD `CS003`) → sửa ghi chú của đúng bản ghi đó.
- Hoặc **Enter** → chọn loài hoa → hệ thống lấy **bản ghi mới nhất** của loài đó.

→ Nhập ghi chú mới (Enter = giữ nguyên, không đổi).

## E5. Tra lịch sử

- Theo loài hoa: `7 → 6` → chọn loài.
- Theo nhân viên: `7 → 7` → chọn nhân viên.

---

# F. CUỐI NGÀY / CHỦ SHOP KIỂM TRA

| Việc | Menu | Ghi chú |
|---|---|---|
| Hôm nay thu bao nhiêu tiền | `6 → 3` | Chỉ tính giao dịch thành công |
| Đơn hôm nay chưa giao | `5 → 9` | Trạng thái `Đang giao` / `Đang chuẩn bị` |
| Đơn giá trị cao (> 500k) | `5 → 8` | |
| Hoa sắp hết (tồn < 10) | `4 → 6` | |
| Lô sắp hết hạn (≤ 3 ngày) | `9 → 5` | |
| Lô còn hàng để bán | `9 → 4` | |
| Thống kê đơn theo trạng thái | `5 → 12` | |
| Khách chi nhiều nhất | `5 → 18` | |
| Hoa bán chạy / chậm | `4 → 10` / `4 → 11` | |

## Khi nghi số liệu tồn kho sai
`9 → 6. Đồng bộ tồn kho từ lô` → `y`.
→ Hệ thống đặt `tồn kho = tổng số còn trong các lô AVAILABLE` cho mọi loài **đã có lô**.

> ⚠️ Phần tồn kho **không ứng với lô nào** sẽ bị bỏ. Chỉ chạy khi thật sự cần.

---

# G. BẢNG TRA NHANH — GẶP LỖI THÌ LÀM GÌ

| Hệ thống báo | Nguyên nhân | Xử lý |
|---|---|---|
| `❌ KHÔNG THỂ XÓA! Nhân viên này đã phụ trách Đơn hàng.` | NV còn đơn | Chuyển đơn sang NV khác |
| `❌ Thất bại!` khi xóa NV/NCC/hoa | Còn dữ liệu tham chiếu (phiếu nhập, lịch chăm, lô…) | Xóa/chuyển dữ liệu con trước |
| `❌ KHÔNG THỂ CHUYỂN TRẠNG THÁI: đơn hàng chưa có loại hoa nào.` | Đơn rỗng | Thêm hoa (B2) |
| `❌ CHƯA THU ĐỦ TIỀN CỌC...` | Chưa cọc | Thu cọc (B3) |
| `❌ TỪ CHỐI THÊM HOA!` | Đơn đã giao/hoàn thành/hủy, hoặc đã thu tiền | Tạo đơn mới hoặc hoàn tiền (C5) |
| `❌ KHÔNG THỂ HOÀN THÀNH ĐƠN HÀNG!` | Chưa trả đủ / chưa có giao dịch | Thu nốt tiền (B5) |
| `❌ Chỉ xóa hoa được khi đơn CHƯA GIAO` | Đơn đang giao | Không sửa được nữa |
| `❌ Đơn ... đang ở trạng thái ... — không thể hủy/hoàn giao dịch.` | Đơn đang giao/hoàn thành | Chỉ hủy GD khi đơn chưa giao hoặc đã đóng |
| `⚠️ Tồn kho và lô đang lệch...` | Dữ liệu cũ lệch | `9 → 6` đồng bộ |
| `❌ Hệ thống chưa có nhân viên nào!` | Chưa tạo NV | Menu `3` tạo nhân viên |

---

# H. PHỤ LỤC

## H1. Bản đồ menu

| Menu chính | Các mục thường dùng |
|---|---|
| **1. Khách hàng** | 1 xem tất cả · **2 tiếp đón** · 6–10 báo cáo |
| **2. Nhà cung cấp** | 1 xem · **2 tiếp nhận** · 3 xóa · 6–9 báo cáo |
| **3. Nhân viên** | 1 xem · 3 thêm · 4 sửa · 5 xóa · 6–8 báo cáo |
| **4. Hoa** | 1 xem · 2 tìm mã · 3 thêm · 4 sửa · 5 xóa · 6–15 báo cáo |
| **5. Đơn hàng** | **1 tạo** · **2 thêm hoa** · **3 thanh toán** · **4 trạng thái** · 5 xóa hoa · 6 xóa đơn · 7 xem · 8–18 báo cáo · **19 sửa đơn** |
| **6. Thanh toán** | 1 xem · 2 theo phương thức · **3 doanh thu hôm nay** · **4 hủy/hoàn** |
| **7. Chăm sóc** | **1 bảng tình trạng** · **2 cập nhật** · **3 sửa ghi chú** · 4 xóa · **5 cần chăm** · 6–7 lịch sử |
| **8. Phiếu nhập** | 1 xem · 2 tìm · **3 tạo** · 4 sửa · 5 xóa · **9 chốt phiếu dở** · 6–8 báo cáo |
| **9. Lô hoa** | 1 xem · 2 đổi trạng thái · **3 thanh lý** · **6 đồng bộ tồn** · 4–5 báo cáo |

## H2. Quy tắc hệ thống tự chặn (nhớ nhanh)

1. Đơn phải **có hoa** mới giao/hoàn thành được.
2. Muốn **giao** phải **cọc đủ 20%**.
3. **Đã thu tiền ⇒ khóa chi tiết hoa**.
4. **Hoàn thành** = **trả đủ** + **có giao dịch**.
5. Chỉ **sửa đơn / xóa hoa** khi **chưa giao**.
6. **Hủy đơn khi chưa trả hết** ⇒ khách mất phần đã trả.
7. **Tồn kho = tổng số còn của lô AVAILABLE**.

---

# I. CHECKLIST DEMO (5 phút)

1. `1 → 2`: nhập `0901234567` → thấy khách có sẵn.
2. `5 → 1`: tạo đơn, chọn NV, ngày giao.
3. `5 → 2`: thêm 2 loại hoa.
4. `5 → 4` → `Đang giao`: **bị chặn** (chưa cọc).
5. `5 → 3` → `1. Trả đủ tiền cọc`.
6. `5 → 4` → `Đang giao`: OK.
7. `5 → 4` → `Hoàn thành`: **bị chặn** (chưa trả đủ).
8. `5 → 3` → trả hết → `5 → 4` → `Hoàn thành`: OK.
9. `7 → 1` xem bảng → `7 → 2` ghi `HÉO` → thanh lý lô → `4 → 1` thấy tồn giảm.
10. `6 → 3`: doanh thu hôm nay.

---

# J. GIỚI HẠN HIỆN TẠI — nói trước khi bị hỏi

| # | Giới hạn | Cách nói / xử lý |
|---|---|---|
| 1 | Chưa có **đăng nhập / phân quyền** | Phạm vi đồ án tập trung vào nghiệp vụ + CRUD |
| 2 | **Không transaction** — app tắt giữa chừng có thể lệch kho | Chữa bằng `9 → 6` đồng bộ tồn kho |
| 3 | **Hạn dùng chưa chặn bán theo ngày** (chỉ theo trạng thái lô) | Thanh lý lô khi sắp hết hạn (`9 → 3`) |
| 4 | Sửa **tồn kho tay** ở menu Hoa sẽ lệch với lô | Hạn chế sửa tay; dùng nhập hàng / thanh lý |
| 5 | **Hoàn tiền không có phiếu riêng** (chỉ đổi trạng thái giao dịch) | Ghi chú thủ công nếu cần đối soát |
| 6 | Chưa có **đổi/trả hàng** sau khi đơn Hoàn thành | Tạo đơn mới |
| 7 | **Mã tự sinh** dạng `ORD001` vượt 999 sẽ trùng khóa | Giới hạn dùng dưới 999 đơn/phiếu |
| 8 | Tìm khách theo SĐT là tìm **gần đúng** (`LIKE`) | Nhập đủ 10 số |
| 9 | Xóa nhân viên / hoa còn dữ liệu con thì **bị chặn** | Đúng thiết kế — xử lý dữ liệu con trước |

> Khi bảo vệ: nên chủ động nêu 2–3 giới hạn (transaction, phân quyền, hạn dùng theo ngày) kèm hướng khắc phục — sẽ được đánh giá cao hơn là để giảng viên phát hiện.
