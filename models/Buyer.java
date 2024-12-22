package models;

import java.io.*;
import java.util.*;

public class Buyer extends User {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PROPERTIES_FILE = "properties.csv"; // property details are stored
    private static final String USERS_FILE = "users.csv"; // user details are stored
    private static final String CONTRACTS_FILE = "contracts.csv"; // contracts details are stored
    private static List<String[]> contracts = new ArrayList<>(); 
    private static List<String[]> propertyDetails = new ArrayList<>();


    private String selectedPropertyId = ""; 
    private boolean contractAccepted = false; 
    
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
                    contactSellerAgent(); 
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
                propertyDetails.clear(); // clear any previous property details
                System.out.println("\n============ Search Properties ============");

                System.out.print("Enter suburb name or property ID: ");
                String searchQuery = scanner.nextLine().toLowerCase();
            
                try (BufferedReader br = new BufferedReader(new FileReader(PROPERTIES_FILE))) {
                    String line;
                    boolean found = false;
            
                    while ((line = br.readLine()) != null) {
                        String[] property = line.split(",");
                        String propertyId = property[0].trim();  
                        String suburb = property[4].trim().toLowerCase(); 

                    // compare if the property ID or suburb contains the search query
                        if (propertyId.toLowerCase().contains(searchQuery) || suburb.contains(searchQuery)) {
                            propertyDetails.add(property); 
                        if (property[5].equals("0")) { // check if the property is not archived
                            System.out.println("Property ID: " + property[0] + ", Suburb: " + property[4] +
                                ", Price: " + property[3] + ", Owner: " + property[6]);
                            found = true;
                        }
                    }
                }
            
                    if (!found) {
                        System.out.println("No properties found matching your search.");
                    } else {
                        System.out.print("\nWhich property are you interested in? Enter Property ID: ");
                        String selectedId = scanner.nextLine();
                        displayPropertyDetails(selectedId, propertyDetails);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading properties file: " + e.getMessage());
                }
            }

    // display the detailed information of a selected property
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


                // ask the buyer if they want to contact the seller/agent
                contactSellerAgent();
                return;
            }
        }
    }

    //contract negotiation
    public void contactSellerAgent() {
        // ask the buyer if they are interested in negotiating a contract
        System.out.print("Do you want to contact the seller/agent to negotiate a contract? (yes/no): ");
        String contactDecision = scanner.nextLine().toLowerCase();

        if (!contactDecision.equals("yes")) {
            System.out.println("Returning to the main menu.");
            return; // exit this method and continue in the menu loop
        }
        
        String sellerEmail = getSellerEmail();
        if (sellerEmail == null) {
            System.out.println("Seller's email not found. Please try again.");

        }
        
        System.out.println("Successfully contacted the seller/agent! Negotiation request sent.");
        System.out.println("The seller/agent will review your request and respond soon.");
    
        // Log the negotiation request
        logNegotiationRequest();
    }
    
    // to get the seller's email based on the selected property
    private String getSellerEmail() {
        String sellerUsername = propertyDetails.get(0)[6];
        String sellerEmail = null;
    
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(sellerUsername)) {
                    sellerEmail = userDetails[3]; 
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users file: " + e.getMessage());
        }
    
        return sellerEmail;
    }
    
    // to log the negotiation request to the requests.csv file
    private void logNegotiationRequest() {
        try (BufferedReader reader = new BufferedReader(new FileReader("requests.csv"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("requests.csv", true))) {
            
            int requestId = 1; 
            
            String lastLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            
            if (lastLine != null) {
                String[] lastRequestDetails = lastLine.split(",");
                requestId = Integer.parseInt(lastRequestDetails[0]) + 1; 
            }
            
            // write the new request to the file
            String propertyId = propertyDetails.get(0)[0]; 
            writer.write(requestId + "," + getUsername() + "," + propertyId );
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error handling requests file: " + e.getMessage());
        }
    }

    // check if the selected property has an accepted contract
    public void checkContractStatus(String selectedId) {
        contracts.clear(); 

        try (BufferedReader br = new BufferedReader(new FileReader(CONTRACTS_FILE))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] contract = line.split(",");
                String contractId = contract[0];
                String status = contract[8]; 
                
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
        contracts.clear(); 
        System.out.println("\n============ Make Payment ============");
    
        try (BufferedReader br = new BufferedReader(new FileReader(CONTRACTS_FILE))) {
            String line;
            boolean foundAcceptedContract = false;
    
            // load contracts from the file
            while ((line = br.readLine()) != null) {
                String[] contract = line.split(",");
                contracts.add(contract);
    
                // check if the contract status is "accepted"
                if (contract[8].trim().equalsIgnoreCase("accepted")) {
                    foundAcceptedContract = true;
    
                    // display the contract details
                    System.out.println("Contract ID: " + contract[0]);
                    System.out.println("Type: " + contract[1]);
                    System.out.println("Description: " + contract[2]);
                    System.out.println("Price: " + contract[3]);
                    System.out.println("Location: " + contract[4]);
                    System.out.println("Seller: " + contract[5]);
                    System.out.println("Buyer: " + contract[6]);
                    System.out.println("Status: " + contract[8]);
                    System.out.println("--------------------------------------");
    
                    // prompt to proceed with payment
                    System.out.print("Would you like to proceed with payment? (yes/no): ");
                    String decision = scanner.nextLine().toLowerCase();
    
                    if (decision.equals("yes")) {
                        double propertyPrice = Double.parseDouble(contract[3]);
    
                        // ask for installment months
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
    
                        processPayment(propertyPrice, installmentMonths); // pass installment details to the payment method
                        updateContractStatusToBought(contract[8]);
                        return;
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
    
        // initial deposit (10% of the property price)
        double initialDeposit = propertyPrice * 0.10;
        double remainingAmount = propertyPrice - initialDeposit;
    
        System.out.println("Initial Deposit (10%): $" + initialDeposit);
        System.out.println("Remaining Amount: $" + remainingAmount);
    
        // calculate monthly installment
        double monthlyInstallment = remainingAmount / installmentMonths;
        System.out.println("Monthly Installment: $" + monthlyInstallment);
    
        // confirm payment
        System.out.print("Confirm payment? (yes/no): ");
        String confirm = scanner.nextLine().toLowerCase();
    
        if (confirm.equals("yes")) {
            System.out.println("Payment confirmed. Thank you for securing the property!");
            // update the contract file
        } else {
            System.out.println("Payment process cancelled.");
        }
    }
    private void updateContractStatusToBought(String contractId) {
        // temporary list to hold updated contract information
        List<String[]> updatedContracts = new ArrayList<>();
    
        // loop through existing contracts and update the status of the selected contract
        for (String[] contract : contracts) {
            if (contract[0].equals(contractId)) {
                // Update the status to "bought"
                contract[8] = "bought";
            }
            updatedContracts.add(contract);
        }
    
        // write the updated contracts back to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONTRACTS_FILE))) {
            for (String[] contract : updatedContracts) {
                bw.write(String.join(",", contract));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating contracts file: " + e.getMessage());
        }
    }
}