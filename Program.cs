using System;
using System.Text;
using vn.edu.crud.menu;
using vn.edu.crud.service;

namespace vn.edu.crud;

class Program
{
    static void Main(string[] args)
    {
        Console.OutputEncoding = Encoding.UTF8;
        Console.InputEncoding = Encoding.UTF8;

        if (args.Length > 0 && args[0] == "--smoke-test")
        {
            RunSmokeTest();
            return;
        }

        int chon;
        do
        {
            Console.WriteLine("\n╔══════════════════════════════════════════════╗");
            Console.WriteLine("║   HỆ THỐNG QUẢN LÝ CỬA HÀNG BÁN HOA (C#)    ║");
            Console.WriteLine("╠══════════════════════════════════════════════╣");
            Console.WriteLine("║  1. Quản lý Khách hàng                       ║");
            Console.WriteLine("║  2. Quản lý Nhà cung cấp                     ║");
            Console.WriteLine("║  3. Quản lý Nhân viên                        ║");
            Console.WriteLine("║  4. Quản lý Hoa                              ║");
            Console.WriteLine("║  5. Quản lý Đơn hàng                         ║");
            Console.WriteLine("║  6. Quản lý Thanh toán                       ║");
            Console.WriteLine("║  7. Quản lý Lịch chăm sóc                    ║");
            Console.WriteLine("║  8. Quản lý Phiếu nhập hàng                  ║");
            Console.WriteLine("║  9. Quản lý Lô hoa                           ║");
            Console.WriteLine("║  0. Thoát                                    ║");
            Console.WriteLine("╚══════════════════════════════════════════════╝");
            Console.Write("Chọn chức năng: ");

            if (!int.TryParse(Console.ReadLine()?.Trim(), out chon))
            {
                chon = -1;
            }

            switch (chon)
            {
                case 1: new CustomerMenu().HienThi(); break;
                case 2: new SupplierMenu().HienThi(); break;
                case 3: new EmployeeMenu().HienThi(); break;
                case 4: new FlowerMenu().HienThi(); break;
                case 5: new OrderMenu().HienThi(); break;
                case 6: new PaymentMenu().HienThi(); break;
                case 7: new CareScheduleMenu().HienThi(); break;
                case 8: new PurchaseOrderMenu().HienThi(); break;
                case 9: new FlowerBatchMenu().HienThi(); break;
                case 0: Console.WriteLine("Tạm biệt! Cảm ơn bạn đã sử dụng hệ thống."); break;
                default: Console.WriteLine("Chức năng không hợp lệ! Vui lòng chọn từ 0 đến 9."); break;
            }
        } while (chon != 0);
    }

    private static void RunSmokeTest()
    {
        Console.WriteLine("=================================================");
        Console.WriteLine("KIỂM THỬ TÍCH HỢP HỆ THỐNG C# & LINQ / LAMBDA");
        Console.WriteLine("=================================================");

        try
        {
            // 1. CustomerService LINQ
            var customerService = new CustomerService();
            var customers = customerService.FilterCustomersByLambda("a");
            Console.WriteLine($"[1/9] CustomerService LINQ filter 'a': {customers.Count} khách hàng.");

            // 2. SupplierService LINQ
            var supplierService = new SupplierService();
            var suppliers = supplierService.SearchSuppliersByLambda("hoa");
            Console.WriteLine($"[2/9] SupplierService LINQ search 'hoa': {suppliers.Count} nhà cung cấp.");

            // 3. EmployeeService LINQ
            var employeeService = new EmployeeService();
            var employees = employeeService.SearchEmployeesByLambda("NV");
            Console.WriteLine($"[3/9] EmployeeService LINQ search 'NV': {employees.Count} nhân viên.");

            // 4. FlowerService LINQ
            var flowerService = new FlowerService();
            var flowers = flowerService.SearchFlowersByLambda("hồng");
            int tongTon = flowerService.TinhTongTonKhoLambda();
            var giamDan = flowerService.HoaGiaGiamDanLambda();
            Console.WriteLine($"[4/9] FlowerService LINQ search: {flowers.Count} hoa, Tổng tồn kho: {tongTon}, Sắp xếp giảm dần: {giamDan.Count} hoa.");

            // 5. OrderService LINQ
            var orderService = new OrderService();
            var orders = orderService.SearchOrdersByLambda("ORD");
            var donChuaGiao = orderService.DonChuaGiaoHomNayLambda();
            var donMoi = orderService.DonHangMoiNhatLambda();
            Console.WriteLine($"[5/9] OrderService LINQ: {orders.Count} đơn, Đơn chưa giao hôm nay: {donChuaGiao.Count}, Đơn mới nhất: {donMoi.Count}.");

            // 6. PaymentService LINQ
            var paymentService = new PaymentService();
            var payments = paymentService.TheoPhuongThucLambda("TIEN_MAT");
            decimal doanhThu = paymentService.DoanhThuHomNayLambda();
            Console.WriteLine($"[6/9] PaymentService LINQ: {payments.Count} GD TIEN_MAT, Doanh thu hôm nay: {doanhThu:N0} đ.");

            // 7. CareScheduleService LINQ
            var careService = new CareScheduleService();
            var careByFlower = careService.LichSuTheoHoaLambda("FL01");
            Console.WriteLine($"[7/9] CareScheduleService LINQ theo FL01: {careByFlower.Count} lần chăm.");

            // 8. PurchaseOrderService LINQ
            var poService = new PurchaseOrderService();
            var poByNCC = poService.PhieuCuaNCCLambda("NCC01");
            var poTong = poService.TongNhapTheoNCCLambda();
            Console.WriteLine($"[8/9] PurchaseOrderService LINQ: {poByNCC.Count} phiếu của NCC01, {poTong.Count} nhóm NCC.");

            // 9. FlowerBatchService LINQ
            var batchService = new FlowerBatchService();
            var batchesCon = batchService.LoConHangLambda();
            var batchesQua7 = batchService.LoNhapQua7NgayLambda();
            Console.WriteLine($"[9/9] FlowerBatchService LINQ: {batchesCon.Count} lô còn hàng, {batchesQua7.Count} lô quá 7 ngày.");

            Console.WriteLine("=================================================");
            Console.WriteLine("🎉 TẤT CẢ 9 DỊCH VỤ C# & LINQ ĐÃ HOẠT ĐỘNG HOÀN HẢO VỚI MYSQL!");
            Console.WriteLine("=================================================");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"❌ LỖI TRONG QUÁ TRÌNH KIỂM THỬ: {ex.Message}");
            Console.WriteLine(ex.StackTrace);
        }
    }
}
