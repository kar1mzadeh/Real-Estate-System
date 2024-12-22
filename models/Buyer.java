package models;

import java.io.*;
import java.util.*;

public class Buyer extends User {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PROPERTIES_FILE = "properties.csv"; // property details are stored
    private static final String USERS_FILE = "users.csv"; // user details are stored
    private static final String CONTRACTS_FILE = "contracts.csv"; // contracts details are stored
    private static List<String[]> contracts = new ArrayList<>(); // to store contract details
    private static List<String[]> propertyDetails = new ArrayList<>();


    private String selectedPropertyId = ""; // Store the selected property ID for the buyer
    private boolean contractAccepted = false; // Flag to check if the contract has been accepted
    
    public Buyer(String username) {
        super(username, "buyer");
    }

    @Override
    public void showMenu() {
        while (true) {
            System.out.println("\n=============== Buyer Menu ================");
            System.out.println("1. Search Properties");
            System.out.println("2. Contact Seller/Agent");
            System.out.println("3. Make Payment");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    searchProperties();
                    break;
                case "2":
                    contactSellerAgent(); // called after property selection
                    break;
                case "3":
                    makePayment();
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Search properties by suburb or property ID
            public void searchProperties() {
                propertyDetails.clear(); // Clear any previous property details
                System.out.println("\n============ Search Properties ============");

                System.out.print("Enter suburb name or property ID: ");
                String searchQuery = scanner.nextLine().toLowerCase();
            
                try (BufferedReader br = new BufferedReader(new FileReader(PROPERTIES_FILE))) {
                    String line;
                    boolean found = false;
            
                    // Search properties based on suburb or property ID
                    while ((line = br.readLine()) != null) {
                        String[] property = line.split(",");
                        String propertyId = property[0].toLowerCase();
                        String suburb = property[4].toLowerCase(); // Assuming suburb is at index 4
            
                        if (propertyId.contains(searchQuery) || suburb.contains(searchQuery)) {
                            propertyDetails.add(property); // Add matching property to the list
                            if(property[5].equals("0")){       // it checks if it is archived or not
                                System.out.println("Property ID: " + property[0] + ", Suburb: " + property[4] +
                                ", Price: " + property[3] + ", Owner: " + property[6]);
                                found = true;
                            }

                        }
                    }
            
                    if (!found) {
                        System.out.println("No properties found matching your search.");
                    } else {
                        // Ask the user for the property they are interested in
                        System.out.print("\nWhich property are you interested in? Enter Property ID: ");
                        String selectedId = scanner.nextLine();
                        // Display detailed information for the selected property
                        displayPropertyDetails(selectedId, propertyDetails);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading properties file: " + e.getMessage());
                }
            }
            

            

    // Display the detailed information of a selected property
    public void displayPropertyDetails(String selectedId, List<String[]> searchResults) {
        for (String[] propertyDetails : searchResults) {
            if (propertyDetails[0].equals(selectedId)) {
                System.out.println("\n============ Property Details =============");

                System.out.println("Property ID: " + propertyDetails[0]);
                System.out.println("Type: " + propertyDetails[1]); // Apartment, Villa, Townhouse
                System.out.println("Description: " + propertyDetails[2]);
                System.out.println("Price: $" + propertyDetails[3]);
                System.out.println("Location: " + propertyDetails[4]);
                System.out.println("Owner: " + propertyDetails[6]);
                System.out.println("--------------------------------------------");


                // After displaying the property details, ask the buyer if they want to contact the seller/agent
                contactSellerAgent();
                // break;
                return;
            }
        }
    }

    public void contactSellerAgent() {
        // Ask the buyer if they are interested in negotiating a contract
        System.out.print("Do you want to contact the seller/agent to negotiate a contract? (yes/no): ");
        String contactDecision = scanner.nextLine().toLowerCase();

        if (!contactDecision.equals("yes")) {
            System.out.println("Returning to the main menu.");
            return; // Exit this method and continue in the menu loop
        }
        
        // Retrieve the seller's email for the selected property
        String sellerEmail = getSellerEmail();
        if (sellerEmail == null) {
            System.out.println("Seller's email not found. Please try again.");

        }
        
        
        // Successfully contact the seller
        System.out.println("Successfully contacted the seller/agent! Negotiation request sent.");
        System.out.println("The seller/agent will review your request and respond soon.");
    
        // Log the negotiation request
        logNegotiationRequest();
    }
    
    // Method to get the seller's email based on the selected property
    private String getSellerEmail() {
        String sellerUsername = propertyDetails.get(0)[6]; // Assuming the seller's username is in the 7th column
        String sellerEmail = null;
    
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(sellerUsername)) {
                    sellerEmail = userDetails[3]; // Assuming the email is at index 3
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
    
        return sellerEmail;
    }
    
    // Method to log the negotiation request to the requests.csv file
    private void logNegotiationRequest() {
        try (BufferedReader reader = new BufferedReader(new FileReader("requests.csv"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("requests.csv", true))) {
            
            int requestId = 1; // Default to 1 if no requests exist
            
            // Determine the last request ID from the file
            String lastLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            
            if (lastLine != null) {
                String[] lastRequestDetails = lastLine.split(",");
                requestId = Integer.parseInt(lastRequestDetails[0]) + 1; // Increment the last ID
            }
            
            // Write the new request to the file
            String propertyId = propertyDetails.get(0)[0]; // Get property ID
            writer.write(requestId + "," + getUsername() + "," + propertyId );
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error handling requests file: " + e.getMessage());
        }
    }
    
    

    // Check if the selected property has an accepted contract
    public void checkContractStatus(String selectedId) {
        contracts.clear(); // Clear previous contract data

        try (BufferedReader br = new BufferedReader(new FileReader(CONTRACTS_FILE))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] contract = line.split(",");
                String contractId = contract[0]; // Assuming property ID is at index 0
                String status = contract[8]; // Assuming contract status is at index 8
                
                if (contractId.equals(selectedId) && status.equals("accepted")) {
                    contractAccepted = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading contracts file: " + e.getMessage());
        }
    }

    public void makePayment() {
        contracts.clear(); // Clear any previous contract details
        System.out.println("\n============ Make Payment ============");
    
        try (BufferedReader br = new BufferedReader(new FileReader(CONTRACTS_FILE))) {
            String line;
            boolean foundAcceptedContract = false;
    
            // Load contracts from the file
            while ((line = br.readLine()) != null) {
                String[] contract = line.split(",");
                contracts.add(contract);
    
                // Check if the contract status is "accepted" and matches the selected property ID
                if (contract[0].trim().equals(selectedPropertyId) && contract[8].trim().equalsIgnoreCase("accepted")) {
                    foundAcceptedContract = true;
    
                    // Display the contract details
                    System.out.println("Property ID: " + contract[0]);
                    System.out.println("Type: " + contract[1]);
                    System.out.println("Description: " + contract[2]);
                    System.out.println("Price: " + contract[4]);
                    System.out.println("Status: " + contract[8]);
                    System.out.println("--------------------------------------");
    
                    // Prompt to proceed with payment
                    System.out.print("Would you like to proceed with payment? (yes/no): ");
                    String decision = scanner.nextLine().toLowerCase();
    
                    if (decision.equals("yes")) {
                        double propertyPrice = Double.parseDouble(contract[4]);
    
                        // Ask for installment months
                        System.out.print("Enter the number of installment months: ");
                        int installmentMonths;
                        while (true) {
                            try {
                                installmentMonths = Integer.parseInt(scanner.nextLine());
                                if (installmentMonths <= 0) {
                                    throw new NumberFormatException("Installment months must be positive.");
                                }
                                break;
                            } catch (NumberFormatException e) {
                                System.out.print("Invalid input. Please enter a valid number of months: ");
                            }
                        }
    
                        processPayment(propertyPrice, installmentMonths); // Pass installment details to the payment method
                    } else {
                        System.out.println("Payment cancelled.");
                    }
                }
            }
    
            if (!foundAcceptedContract) {
                System.out.println("No accepted contract found for the selected property.");
            }
        } catch (IOException e) {
            System.out.println("Error reading contracts file: " + e.getMessage());
        }
    }
    
    private void processPayment(double propertyPrice, int installmentMonths) {
        System.out.println("\n============ Payment Process ============");
    
        // Calculate initial deposit (10% of the property price)
        double initialDeposit = propertyPrice * 0.10;
        double remainingAmount = propertyPrice - initialDeposit;
    
        System.out.println("Initial Deposit (10%): $" + initialDeposit);
        System.out.println("Remaining Amount: $" + remainingAmount);
    
        // Calculate monthly installment
        double monthlyInstallment = remainingAmount / installmentMonths;
        System.out.println("Monthly Installment: $" + monthlyInstallment);
    
        // Confirm payment
        System.out.print("Confirm payment? (yes/no): ");
        String confirm = scanner.nextLine().toLowerCase();
    
        if (confirm.equals("yes")) {
            System.out.println("Payment confirmed. Thank you for securing the property!");
            // Update the contract file or database as needed
        } else {
            System.out.println("Payment process cancelled.");
        }
    }
}