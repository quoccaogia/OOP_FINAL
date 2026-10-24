namespace vn.edu.crud.model;

public class Customer
{
    public string CustomerId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Phone { get; set; } = string.Empty;
    public string Address { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;

    public Customer() { }

    public Customer(string id, string name, string phone, string address, string email)
    {
        CustomerId = id;
        Name = name;
        Phone = phone;
        Address = address;
        Email = email;
    }
}
