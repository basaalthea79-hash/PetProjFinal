package model;

public class Customer extends User {
    private String customerID;
    private String phoneNumber;
    private String address;
    public Customer(String customerID, String name, String username, String password, String phoneNumber, String address) {
        super(name, username, password);
        this.customerID = customerID;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    public String getCustomerID() {
        return customerID;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public String toFileLine() {
        return "CUSTOMER|" + customerID
                + "|" + getName()
                + "|" + getUsername()
                + "|" + getPassword()
                + "|" + phoneNumber
                + "|" + address;
    }
}