package models;


public class Buyer extends User {
    public Buyer(String username, String role) {
        super(username, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n===== Buyer Menu =====");
        System.out.println("1. Search Properties");
        System.out.println("2. Contact Seller/Agent");
        System.out.println("3. Make Payment");
        System.out.println("4. Logout");
    }
}