package vn.edu.crud.model;

import java.math.BigDecimal;

public class Employee {
    private String employeeId;
    private String name;
    private String role;
    private String phone;
    private BigDecimal salary;

    public Employee() {}

    public Employee(String employeeId, String name, String role, String phone, BigDecimal salary) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.salary = salary;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Employee{" + employeeId + ", " + name + ", " + role + "}";
    }
}