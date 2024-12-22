package models;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Agent extends User {
    private static final Scanner scanner = new Scanner(System.in);

    public Agent(String username) {
        super(username, "agent");
    }

    @Override
    public void showMenu() {
        while (true) {
            System.out.println("\n============== Agent Menu =================");

            System.out.println("1. View Requests");
            // System.out.println("2. Create Contract");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            String option = scanner.next();

            switch (option) {
                case "1":
                    manageContracts();
                    break;
                // case "2":
                //     createContract(); // Optional direct entry to contract creation
                //     break;
                case "2":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void manageContracts() {
        System.out.println("\n============== View Requests ===============");

        try (BufferedReader br = new BufferedReader(new FileReader("requests.csv"))) {
            String line;
            // boolean hasRequests = false;
            List<String[]> requests = new ArrayList<>();
            int requestCount = 1;

            while ((line = br.readLine()) != null) {
                String[] requestDetails = line.split(",");
                requests.add(requestDetails);
                // System.out.println( + ". Request ID: " + requestDetails[0]);
                System.out.println(requestCount + "  Buyer: " + requestDetails[1]);
                System.out.println("  Property ID: " + requestDetails[2]);
                System.out.println("-------------------------------------------");

                // hasRequests = true;
                requestCount++;
            }

            // if (!hasRequests) {
            //     System.out.println("No requests found.");
            //     return;
            // }
            if (requests.isEmpty()) {
                System.out.println("No requests found.");
                return;
            }

            System.out.print("Select a request to respond to (Enter number) or type 0 to return to the menu: ");
            int choice = Integer.parseInt(scanner.next());

            if (choice > 0 && choice <= requests.size()) {
                respondToRequest(requests, choice - 1);
            } else {
                System.out.println("Returning to menu.");
            }
        } catch (IOException e) {
            System.err.println("Error reading requests file: " + e.getMessage());
        }
    }

    private static void respondToRequest(List<String[]> requests, int index) {
        String[] requestDetails = requests.get(index);

        System.out.println("\nSelected Request:");
        System.out.println("Request ID: " + requestDetails[0]);
        System.out.println("Buyer: " + requestDetails[1]);
        System.out.println("Property ID: " + requestDetails[2]);

        System.out.print("\nDo you want to accept this request? (yes/no): ");
        String decision = scanner.next().toLowerCase();

        if (decision.equals("yes")) {
            System.out.println("Request accepted. Proceeding to create a contract...");
            createContract(requestDetails);
        
            // Remove the accepted request from the list
            requests.remove(index);

            // Update the requests.csv file
            updateRequestsFile(requests);
        } else {
            System.out.println("Request declined. Returning to menu.");
        }
    }

    private static void updateRequestsFile(List<String[]> requests) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("requests.csv"))) {
            for (String[] request : requests) {
                bw.write(String.join(",", request));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating requests file: " + e.getMessage());
        }
    }
    

    public static void createContract(String[] requestDetails) {
        System.out.println("\n================ Contract =================");

        String buyerUsername = requestDetails[1];
        String propertyId = requestDetails[2];
        
        String[] propertyDetails = getPropertyDetailsById(propertyId);
        if (propertyDetails == null) {
            System.out.println("Error: Property details not found for Property ID: " + propertyId);
            return;
        }
    
        // Display property details
        System.out.println("Property ID: " + propertyDetails[0]);
        System.out.println("Type: " + propertyDetails[1]); // Apartment, Villa, Townhouse
        System.out.println("Description: " + propertyDetails[2]);
        System.out.println("Price: $" + propertyDetails[3]);
        System.out.println("Location: " + propertyDetails[4]);
        System.out.println("Owner: " + propertyDetails[5]);
        System.out.println("Buyer: " + buyerUsername);

        // Get the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = LocalDateTime.now().format(formatter);

        // Display and collect contract details
        System.out.println("Date and Time: " + currentDateTime);

        System.out.print("Sign (enter \"sign\"): ");
        String contractDetails = scanner.next();

        // Save contract to a file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("contracts.csv", true))) {
            String contractId = UUID.randomUUID().toString();
            bw.write(contractId + "," + buyerUsername + "," + propertyId + "," + contractDetails + "\n");
            System.out.println("Contract created successfully!");
        } catch (IOException e) {
            System.err.println("Error saving contract: " + e.getMessage());
        }
    }

    private static String[] getPropertyDetailsById(String propertyId) {
        try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] propertyDetails = line.split(",");
                if (propertyDetails[0].equals(propertyId)) { // Match Property ID
                    return propertyDetails;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
        }
        return null; // Return null if not found
    }
    
}
