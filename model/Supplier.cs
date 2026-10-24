namespace vn.edu.crud.model;

public class Supplier
{
    public string SupplierId { get; set; } = string.Empty;
    public string Name { get; set; } = string.Empty;
    public string Phone { get; set; } = string.Empty;
    public string Address { get; set; } = string.Empty;
    public string Representative { get; set; } = string.Empty;

    public Supplier() { }

    public Supplier(string id, string name, string phone, string address, string rep)
    {
        SupplierId = id;
        Name = name;
        Phone = phone;
        Address = address;
        Representative = rep;
    }
}
