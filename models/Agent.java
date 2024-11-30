package models;
public class Agent extends User {
    public Agent(String username, String role) {
        super(username, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n===== Agent Menu =====");
        System.out.println("1. Manage Contracts");
        System.out.println("2. View Interested Buyers");
        System.out.println("3. Logout");
    }
}