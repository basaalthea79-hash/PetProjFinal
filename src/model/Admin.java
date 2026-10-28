package model;

public class Admin extends User {
    private String adminID;

    public Admin(String adminID, String name, String username, String password) {
        super(name, username, password);
        this.adminID = adminID;
    }

    public String getAdminID() {
        return adminID;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String toFileLine() {
        return "ADMIN|" + adminID + "|" + getName() + "|" + getUsername() + "|" + getPassword();
    }
}