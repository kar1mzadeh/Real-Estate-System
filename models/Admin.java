package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Admin extends User {
    final String usersFilePath = "users.csv";

    public Admin(String username, String role) {
        super(username, role);
    }

    @Override
    public void showMenu() {
        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. Retrieve Accounts");
        System.out.println("2. Logout");
    }

    public void retrieveUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(usersFilePath))) {
            String line;
            System.out.println("\n===== User Accounts =====");
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length >= 3) {
                    String username = userDetails[0];
                    String role = userDetails[2];
                    String email = "";
                    if (userDetails.length >= 4) {
                        email = userDetails[3];
                    }
                    System.out.println("Username: " + username + ", Role: " + role + ", Email: " + email);
                } else {
                    System.out.println("Invalid entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

}
