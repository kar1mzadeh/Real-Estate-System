package operations;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.User;

public class LoginSystem {
    private static final Scanner scanner = new Scanner(System.in);
    final static String requestsFilePath = "password_view_requests.txt";
    final static String usersFilePath = "users.csv";

    public static void login() {
        System.out.print("\nEnter username: ");
        String username = scanner.next();
        System.out.print("\nEnter password: ");
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

    public static void register() {
        System.out.print("\nEnter a new username: ");
        String username = scanner.next();
        System.out.print("\nEnter a password: ");
        String password = FileOperations.readPassword(); // Secure input
        System.out.print("\nEnter your role (buyer, seller, agent, admin): ");
        String role = scanner.next().toLowerCase();
        System.out.print("\nEnter your email: ");
        String email = scanner.next();
    
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

public static void forgotPassword() {
    System.out.print("\nEnter your username: ");
    String username = scanner.next();
    System.out.print("\nEnter your email: ");
    String email = scanner.next();

    if (isRequestApproved(username)) {
        String password = getPasswordFromUsersFile(username);
        if (password != null) {
            System.out.println("Your password is: " + password + '\n');
            removeFromApprovedRequests(username);
        } else {
            System.out.println("Error: Could not find your password in the system.");
        }
        return;
    }

    if (FileOperations.isUserValid(username, email)) {
        System.out.println("Sent successfully to admin.");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(requestsFilePath, true))) {
            writer.write(username);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("An error occurred while writing the request to the file.");
        }
    } else {
        System.out.println("Invalid username or email. Please try again.");
    }
}

private static boolean isRequestApproved(String username) {
    String approvedLogFilePath = "approved_requests.txt";
    try (BufferedReader reader = new BufferedReader(new FileReader(approvedLogFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals(username)) {
                return true;
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading approved requests log: " + e.getMessage());
    }
    return false;
}

private static String getPasswordFromUsersFile(String username) {
    try (BufferedReader reader = new BufferedReader(new FileReader(usersFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");

            if (userDetails.length >= 2 && userDetails[0].equals(username)) {
                return userDetails[1];
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading user data: " + e.getMessage());
    }
    return null;
}

private static void removeFromApprovedRequests(String username) {
    String approvedLogFilePath = "approved_requests.txt";
    List<String> requests = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(approvedLogFilePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals(username)) {
                requests.add(line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading approved requests log: " + e.getMessage());
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(approvedLogFilePath))) {
        for (String request : requests) {
            writer.write(request);
            writer.newLine();
        }
    } catch (IOException e) {
        System.err.println("Error updating approved requests log: " + e.getMessage());
    }
}
    
    // A simple email validation method
    private static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

}