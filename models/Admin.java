package models;

public class Admin extends User {
    public Admin(String username, String role) {
        super(username, role);
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. Retrieve Accounts");
        System.out.println("2. Logout");
    }
}