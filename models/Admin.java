package models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Admin extends User {
    final String usersFilePath = "users.csv";
    final static String requestsFilePath = "password_view_requests.txt";
    Scanner scanner = new Scanner(System.in);

    public Admin(String username, String role) {
        super(username, role);
    }

    @Override
    public void showMenu() {
        
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Retrieve Accounts");
            System.out.println("2. View Password View Requests");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            String option = scanner.next();

            switch (option) {
                case "1":
                    retrieveUsers();
                    break;
                case "2":
                    viewPasswordViewRequests();
                    break;
                case "3":
                    System.out.println("Logging out...");
                    isLoggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
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

    public void viewPasswordViewRequests() {
        try (BufferedReader br = new BufferedReader(new FileReader(requestsFilePath))) {
            List<String> requests = new ArrayList<>();
            String line;
            System.out.println("\n===== Password View Requests =====");
            int index = 1;

            while ((line = br.readLine()) != null) {
                requests.add(line);
                System.out.println(index + ". " + line);
                index++;
            }

            if (requests.isEmpty()) {
                System.out.println("No requests found.");
                return;
            }

            System.out.println("\nOptions:");
            System.out.println("1. Approve a request");
            System.out.println("2. Go back");
            System.out.print("Choose an option: ");
            String option = scanner.next();
            
            System.out.println("Selected option: " +  option);

            if ("1".equals(option)) {
                System.out.print("Enter the request number to approve: ");
                int requestNumber;
                try {
                    requestNumber = Integer.parseInt(scanner.next());
                    if (requestNumber > 0 && requestNumber <= requests.size()) {
                        approveRequest(requestNumber, requests);
                    } else {
                        System.out.println("Invalid request number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        } catch (IOException e) {
            System.err.println("No user has requested for password view!");
        }
    }

    private void approveRequest(int requestNumber, List<String> requests) {
        String approvedRequest = requests.get(requestNumber - 1);
        requests.remove(requestNumber - 1);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(requestsFilePath))) {
            for (String request : requests) {
                writer.write(request);
                writer.newLine();
            }
            System.out.println("Approved request: " + approvedRequest);
        } catch (IOException e) {
            System.err.println("Error updating requests file: " + e.getMessage());
        }
        logApprovedRequest(approvedRequest);
    }

    private void logApprovedRequest(String approvedRequest) {
        String approvedLogFilePath = "approved_requests.txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(approvedLogFilePath, true))) {
            writer.write(approvedRequest);
            writer.newLine();
            System.out.println("Approved request logged for user visibility.");
        } catch (IOException e) {
            System.err.println("Error writing to approved requests log: " + e.getMessage());
        }
    }
}
