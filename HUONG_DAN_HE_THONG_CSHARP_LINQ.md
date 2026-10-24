# HƯỚNG DẪN HỆ THỐNG FLOWER SHOP (PHIÊN BẢN C# & LINQ / LAMBDA)

Hệ thống được phát triển song song với bản Java (`FlowerShopV2`), kết nối chung vào cùng một cơ sở dữ liệu MySQL (`flower_shop` trên `localhost:3306`).
Cấu trúc thư mục và tên package/namespace được đồng bộ hoàn toàn 1:1 với dự án Java!

---

## 1. Đồng Bộ Cấu Trúc: Package (Java) $\leftrightarrow$ Namespace & Folder (C#)

Trong C#, khái niệm **Package** của Java tương đương chính xác với **Namespace** và các **Folder**.
Hệ thống C# đã được đổi tên và gom nhóm thành đúng 5 folder tương ứng 100% với các package trong Java:

| Folder trong dự án | Package trong Java | Namespace trong C# | Chức năng trong kiến trúc 3 lớp |
| :--- | :--- | :--- | :--- |
| [`model/`](file:///D:/OOP/FlowerShop_CSharp/model) | `package vn.edu.crud.model;` | `namespace vn.edu.crud.model;` | 10 thực thể dữ liệu (POCO / Entity) |
| [`dao/`](file:///D:/OOP/FlowerShop_CSharp/dao) | `package vn.edu.crud.dao;` | `namespace vn.edu.crud.dao;` | Tầng Data Access Object (truy vấn MySQL) |
| [`service/`](file:///D:/OOP/FlowerShop_CSharp/service) | `package vn.edu.crud.service;` | `namespace vn.edu.crud.service;` | Tầng Nghiệp vụ (LINQ & Lambda Expressions) |
| [`menu/`](file:///D:/OOP/FlowerShop_CSharp/menu) | `package vn.edu.crud.menu;` | `namespace vn.edu.crud.menu;` | Tầng Giao diện Console (UI) |
| [`util/`](file:///D:/OOP/FlowerShop_CSharp/util) | `package vn.edu.crud.util;` | `namespace vn.edu.crud.util;` | Tiện ích nhập liệu regex & an toàn |

---

## 2. Cấu Trúc Chi Tiết Thư Mục C# (`D:\OOP\FlowerShop_CSharp`)

```
FlowerShop_CSharp/
├── model/               # 1. Tầng Model: Chứa 10 thực thể dữ liệu (POCO)
│   ├── Customer.cs
│   ├── Supplier.cs
│   ├── Employee.cs
│   ├── EmployeeRole.cs
│   ├── Flower.cs
│   ├── Order.cs
│   ├── OrderDetail.cs
│   ├── Payment.cs
│   ├── CareSchedule.cs
│   ├── PurchaseOrder.cs
│   └── FlowerBatch.cs
│
├── dao/                 # 2. Tầng DAO / Data Access: Tương tác trực tiếp DB MySQL
│   ├── DBConnection.cs
│   ├── DBBase.cs
│   ├── CustomerDAO.cs
│   ├── SupplierDAO.cs
│   ├── EmployeeDAO.cs
│   ├── FlowerDAO.cs
│   ├── OrderDAO.cs
│   ├── PaymentDAO.cs
│   ├── CareScheduleDAO.cs
│   ├── PurchaseOrderDAO.cs
│   └── FlowerBatchDAO.cs
│
├── service/             # 3. Tầng Service: Xử lý nghiệp vụ, LINQ & Lambda Expressions
│   ├── CustomerService.cs
│   ├── SupplierService.cs
│   ├── EmployeeService.cs
│   ├── FlowerService.cs
│   ├── OrderService.cs
│   ├── PaymentService.cs
│   ├── CareScheduleService.cs
│   ├── PurchaseOrderService.cs
│   └── FlowerBatchService.cs
│
├── util/                # Các tiện ích hệ thống
│   ├── NhapLieu.cs      # Kiểm tra hợp lệ (Regex SĐT 10 số, Email, số tiền, ...)
│   └── AnToan.cs        # Xử lý ngoại lệ, thông báo thân thiện tiếng Việt
│
├── menu/                # 4. Tầng Giao diện Console (UI)
│   ├── CustomerMenu.cs
│   ├── SupplierMenu.cs
│   ├── EmployeeMenu.cs
│   ├── FlowerMenu.cs
│   ├── OrderMenu.cs
│   ├── PaymentMenu.cs
│   ├── CareScheduleMenu.cs
│   ├── PurchaseOrderMenu.cs
│   └── FlowerBatchMenu.cs
│
├── Program.cs           # Điểm khởi chạy (Main Entry Point)
└── FlowerShop_CSharp.csproj
```

---

## 3. Bảng Đối Chiếu Cú Pháp: Java Stream API vs C# LINQ

| Nghiệp vụ / Thao tác | Java (Stream API & Lambda) | C# (.NET LINQ & Lambda) | Ý nghĩa / Cách vận hành |
| :--- | :--- | :--- | :--- |
| **Lọc dữ liệu** | `.filter(x -> x.getProp().equals(...))` | `.Where(x => x.Prop == ...)` | Giữ lại các phần tử thỏa mãn biểu thức điều kiện Lambda |
| **Ánh xạ thuộc tính** | `.map(x -> x.getProp())` | `.Select(x => x.Prop)` | Chiết xuất thuộc tính từ đối tượng sang kiểu mới |
| **Sắp xếp tăng dần** | `.sorted(Comparator.comparing(...))` | `.OrderBy(x => x.Prop)` | Sắp xếp danh sách theo thứ tự tăng dần |
| **Sắp xếp giảm dần** | `.sorted((a, b) -> b.get().compareTo(a.get()))` | `.OrderByDescending(x => x.Prop)` | Sắp xếp danh sách theo thứ tự giảm dần |
| **Tính tổng số** | `.mapToInt(x -> x.getQty()).sum()` | `.Sum(x => x.Qty)` | Cộng dồn giá trị số nguyên/thực của các phần tử |
| **Gom nhóm** | `.collect(Collectors.groupingBy(...))` | `.GroupBy(x => x.Key)` | Gom nhóm phần tử theo thuộc tính chỉ định |
| **Chuyển thành danh sách** | `.collect(Collectors.toList())` | `.ToList()` | Ép luồng kết quả LINQ thành `List<T>` |
| **Kiểm tra tồn tại** | `.anyMatch(x -> ...)` | `.Any(x => ...)` | Trả về `true` nếu có ít nhất 1 phần tử thỏa mãn |

---

## 4. Cách Chạy Dự Án

Mở PowerShell tại thư mục `D:\OOP\FlowerShop_CSharp`:

1. **Chạy chế độ kiểm thử tự động (Smoke Test - kiểm tra 9 service với MySQL):**
   ```powershell
   dotnet run -- --smoke-test
   ```
2. **Chạy ứng dụng tương tác Console (Menu 1 đến 9):**
   ```powershell
   dotnet run
   ```
3. **Build biên dịch lại dự án:**
   ```powershell
   dotnet build
   ```

---

## 5. Câu Hỏi Thường Gặp Khi Bảo Vệ Đồ Án (Dành Cho Sinh Viên Năm 2)

### Q1: Tại sao trong C# lại dùng `namespace vn.edu.crud.service` thay vì `package`?
> **Trả lời:**
> Trong ngôn ngữ C#, từ khóa để gom nhóm mã nguồn thành các gói logic tương đương với `package` của Java chính là **`namespace`**. Cả 2 dự án em đều tổ chức đồng nhất 5 thư mục: `model`, `dao`, `service`, `menu`, `util` và các namespace/package tương ứng `vn.edu.crud.*`.

### Q2: Tại sao tách riêng DAO và Service? Sao không viết LINQ trong DAO?
> **Trả lời:**
> - **DAO (Data Access Object):** Chỉ chuyên trách 1 việc duy nhất là kết nối và đọc/ghi dữ liệu thô với Database MySQL (Single Responsibility Principle trong SOLID).
> - **Service:** Chịu trách nhiệm về Business Logic (Nghiệp vụ), áp dụng các bộ lọc, kiểm tra ràng buộc, sắp xếp và tính toán bằng LINQ trên bộ nhớ RAM.
> - **Menu (UI):** Chỉ giao tiếp với Service, không được phép gọi trực tiếp DAO. Mô hình này giúp code tách bạch (Decoupling), dễ bảo trì và dễ viết unit test.

### Q3: Biểu thức `x => ...` trong C# hoạt động như thế nào?
> **Trả lời:**
> `x => ...` là một **Lambda Expression** (hàm ẩn danh). Ở đây `x` đại diện cho từng phần tử trong tập hợp đang duyệt, và vế sau `=>` là biểu thức logic trả về kiểu dữ liệu tương ứng (ví dụ `bool` cho `.Where()` hoặc một giá trị cho `.Select()` / `.Sum()`).
