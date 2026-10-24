namespace vn.edu.crud.model;

public class Employee
{
    public string EmployeeId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Role { get; set; } = string.Empty;
    public string Phone { get; set; } = string.Empty;
    public decimal Salary { get; set; }

    public Employee() { }

    public Employee(string id, string name, string role, string phone, decimal salary)
    {
        EmployeeId = id;
        Name = name;
        Role = role;
        Phone = phone;
        Salary = salary;
    }
}
