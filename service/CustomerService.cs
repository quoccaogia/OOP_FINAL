using System;
using System.Collections.Generic;
using System.Linq;
using vn.edu.crud.dao;
using vn.edu.crud.model;

namespace vn.edu.crud.service;

/// <summary>
/// Tầng Service xử lý nghiệp vụ Khách hàng (Mục 1).
/// Đóng gói logic tìm kiếm, lọc bằng biểu thức LINQ & Lambda.
/// </summary>
public class CustomerService
{
    private readonly CustomerDAO _customerDAO;

    public CustomerService()
    {
        _customerDAO = new CustomerDAO();
    }

    public CustomerService(CustomerDAO customerDAO)
    {
        _customerDAO = customerDAO;
    }

    // =========================================================================
    // HÀM NGHIỆP VỤ SỬ DỤNG LINQ & BIỂU THỨC LAMBDA (MỤC 1)
    // =========================================================================

    /// <summary>
    /// Tìm kiếm và lọc khách hàng bằng LINQ & Lambda theo tên, SĐT hoặc email.
    /// </summary>
    public List<Customer> FilterCustomersByLambda(string keyword)
    {
        var allCustomers = _customerDAO.FindAll();
        if (string.IsNullOrWhiteSpace(keyword))
        {
            return allCustomers;
        }

        string lowerKeyword = keyword.ToLower().Trim();

        // Sử dụng LINQ Where với biểu thức Lambda: c => ...
        return allCustomers
            .Where(c => (!string.IsNullOrEmpty(c.Name) && c.Name.ToLower().Contains(lowerKeyword)) ||
                        (!string.IsNullOrEmpty(c.Phone) && c.Phone.Contains(lowerKeyword)) ||
                        (!string.IsNullOrEmpty(c.Email) && c.Email.ToLower().Contains(lowerKeyword)))
            .ToList();
    }

    // =========================================================================
    // CÁC HÀM ĐIỀU PHỐI CRUD QUA DAO
    // =========================================================================

    public List<Customer> FindAll() => _customerDAO.FindAll();

    public Customer? FindById(string id) => _customerDAO.FindById(id);

    public List<Customer> TimTheoSdt(string sdt) => _customerDAO.TimTheoSdt(sdt);

    public bool Insert(Customer c) => _customerDAO.Insert(c);

    public bool Update(Customer c) => _customerDAO.Update(c);

    public string SinhMaKhachMoi() => _customerDAO.SinhMaKhachMoi();

    public string ChuanHoaSdt(string sdt) => _customerDAO.ChuanHoaSdt(sdt);
}
