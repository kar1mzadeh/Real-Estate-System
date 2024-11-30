import java.io.*;
import java.util.Scanner;

import models.Admin;
import models.Agent;
import models.Buyer;
import models.Seller;
import models.User;

public class LoginSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String CSV_FILE = "users.csv";

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
                    User user = login();
                    if (user != null) {
                        showMenu(user);
                    }
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
    private static void register() {
        System.out.print("\nEnter a new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter a password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your role (buyer, seller, agent, admin): ");
        String role = scanner.nextLine().toLowerCase();

        if (!isValidRole(role)) {
            System.out.println("Invalid role. Please choose from the list which is mentioned");
            return;
        }

        if (isUsernameTaken(username)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        try (FileWriter fw = new FileWriter(CSV_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(username + "," + password + "," + role);
            bw.newLine();
            System.out.println("Congratulations! You can log in now.");
        } catch (IOException e) {
            System.err.println("Error writing to user data: " + e.getMessage());
        }
    }


    private static User login() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3 && userData[0].equals(username) && userData[1].equals(password)) {
                    System.out.println("\nLogin successful! Welcome, " + username + ".");
                    return createUserInstance(username, userData[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }

        System.out.println("\nInvalid username or password. Please try again.");
        return null;
    }

    
    private static boolean isUsernameTaken(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    private static boolean isValidRole(String role) {
        return role.equals("buyer") || role.equals("seller") || role.equals("agent") || role.equals("admin");
    }

    private static User createUserInstance(String username, String role) {
        switch (role) {
            case "buyer":
                return new Buyer(username, role);
            case "seller":
                return new Seller(username, role);
            case "agent":
                return new Agent(username, role);
            case "admin":
                return new Admin(username, role);
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    private static void showMenu(User user) {
        while (true) {
            user.displayMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
    
            if (user instanceof Seller && choice.equals("1")) {
                SellerImp sellerImp = new SellerImp();
                sellerImp.createProperty(user);
            } 
            else if (user instanceof Seller && choice.equals("2")) {
                SellerImp sellerImp = new SellerImp();
                sellerImp.editProperty(user.getUsername());
            } 
            else if (user instanceof Seller && choice.equals("5")) {
                System.out.println("\nLogging out...");
                break;
            } else {
                System.out.println("\nThis functionality is not yet implemented. Try again.");
            }
        }
    }
    
}
