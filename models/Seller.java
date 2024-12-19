package models;

public class Seller extends User {

    public Seller(String username, String role) {
        super(username, "seller");
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== Seller Menu =====");
        System.out.println("1. Create Property");
        System.out.println("2. Edit Property");
        System.out.println("3. Archive Property");
        System.out.println("4. Sign Contract");
        System.out.println("5. Logout");
    }
}

