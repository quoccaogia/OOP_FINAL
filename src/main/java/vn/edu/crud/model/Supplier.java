package vn.edu.crud.model;

public class Supplier {
    private String supplierId;
    private String name;
    private String phone;
    private String address;
    private String representative;

    public Supplier() {}

    public Supplier(String supplierId, String name, String phone, String address, String representative) {
        this.supplierId = supplierId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.representative = representative;
    }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRepresentative() { return representative; }
    public void setRepresentative(String representative) { this.representative = representative; }

    @Override
    public String toString() {
        return "Supplier{" + supplierId + ", " + name + "}";
    }
}