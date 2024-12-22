import java.io.*;
import java.util.*;

import models.Admin;
import models.Agent;
import models.Buyer;
import models.User;
import seller.Seller;

public class FileOperations {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String CSV_FILE = "users.csv";

    public static boolean isUsernameTaken(String username) {
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

    public static void registerUser(String username, String password, String role, String email) {
        // Save the user data (username, password, role, email) to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.csv", true))) {
            writer.write(username + "," + password + "," + role + "," + email);
            writer.newLine(); // Add a new line after the user data
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    public User loginUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 4 && userData[0].equals(username) && userData[1].equals(password)) {
                    return createUserInstance(username, userData[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }
        return null;
    }

    private User createUserInstance(String username, String role) {
        switch (role) {
            case "admin":
                return new Admin(username, role);
            case "agent":
                return new Agent(username);
            case "buyer":
                return new Buyer(username);
            case "seller":
                return new Seller(username, role);
            default:
                return null;
        }
    }

    public static boolean isValidRole(String role) {
        return role.equals("buyer") || role.equals("seller") || role.equals("agent") || role.equals("admin");
    }
    
    public static String readPassword() {
        char[] passwordArray = null;
        try {
            Console console = System.console();
            if (console == null) {
                System.out.println("Console not available. Please run from a terminal.");
                return scanner.next();
            }
            passwordArray = console.readPassword();
        } catch (Exception e) {
            System.err.println("Error reading password: " + e.getMessage());
        }
        return new String(passwordArray);
    }

    public static boolean isUserValid(String username, String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 4 && userData[0].equals(username) && userData[3].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error validating user: " + e.getMessage());
        }
        return false;
    }
    
}
