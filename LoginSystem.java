import java.util.Scanner;

import models.User;

public class LoginSystem {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("===========================================");
            System.out.println("Welcome to PARALLAX Real Estate Application");
            System.out.println("===========================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    login();
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = FileOperations.readPassword(); // Secure input
        FileOperations fileOps = new FileOperations();
        User user = fileOps.loginUser(username, password);
        
        if (user != null) {
            System.out.println("Login successful! Welcome, " + username + ".");
            user.showMenu();
        } else {
            System.out.println("Invalid username or password. Try again.");
        }
    }

    private static void register() {
        System.out.print("Enter a new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = FileOperations.readPassword(); // Secure input
        System.out.print("Enter your role (buyer, seller, agent, admin): ");
        String role = scanner.nextLine().toLowerCase();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
    
        // Validate email format
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Please enter a valid email.");
            return;
        }
    
        if (FileOperations.isUsernameTaken(username)) {
            System.out.println("Username already exists. Please choose a different username.");
        } else if (!FileOperations.isValidRole(role)) {
            System.out.println("Invalid role. Choose from 'buyer', 'seller', 'agent', 'admin'.");
        } else {
            FileOperations.registerUser(username, password, role, email); // Save email
            System.out.println("Registration successful! You can log in now.");
        }
    }
    
    // A simple email validation method
    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
}
