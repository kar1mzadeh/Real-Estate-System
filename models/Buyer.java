package models;

import java.io.*;
import java.util.*;

public class Buyer extends User {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PROPERTIES_FILE = "properties.csv"; // property details are stored
    private static final String USERS_FILE = "users.csv"; // user details are stored
    private static List<String[]> propertyDetails = new ArrayList<>();


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
                System.out.println("Owner: " + propertyDetails[5]);

                System.out.println("--------------------------------------------");


                // After displaying the property details, ask the buyer if they want to contact the seller/agent
                contactSellerAgent();
                break;
            }
        }
    }

    public void contactSellerAgent() {
        // Ask the buyer if they are interested in negotiating a contract
        System.out.print("Do you want to contact the seller/agent to negotiate a contract? (yes/no): ");
        String contactDecision = scanner.nextLine().toLowerCase();
 
        if (contactDecision.equals("yes")) {
            try {
                // Retrieve the seller's username for the selected property
                if (propertyDetails.isEmpty()) {
                    System.out.println("No property details found. Please search for properties first.");
                    return;
                }
    
                String sellerUsername = propertyDetails.get(0)[5]; 
                String propertyId = propertyDetails.get(0)[0];
    
                // Look up the seller's email using their username from the users.csv
                BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));
                String line;
                String sellerEmail = null;
    
                while ((line = br.readLine()) != null) {
                    String[] userDetails = line.split(",");
                    if (userDetails[0].equals(sellerUsername)) { 
                        sellerEmail = userDetails[3]; 
                        break;
                    }
                }
                br.close();
    
                if (sellerEmail != null) {
                    System.out.println("\nSeller's Email: " + sellerEmail);
                    System.out.println("Successfully contacted the seller! A request to negotiate the contract has been sent.");
                    System.out.println("The seller will review your request and respond soon.");
                    
                    try (BufferedReader reader = new BufferedReader(new FileReader("requests.csv"));
                         BufferedWriter writer = new BufferedWriter(new FileWriter("requests.csv", true))) {
                        
                        int requestId = 1; // Default to 1 if no requests exist
                        
                        // Determine the last request ID from the file
                        String lastLine = null;
                        String l;
                        while ((l = reader.readLine()) != null) {
                            lastLine = l;
                        }
                        
                        if (lastLine != null) {
                            String[] lastRequestDetails = lastLine.split(",");
                            requestId = Integer.parseInt(lastRequestDetails[0]) + 1; // Increment the last ID
                        }
                        
                        // Write the new request to the file
                        writer.write(requestId + "," + getUsername() + "," + propertyId);
                        writer.newLine();
                    } catch (IOException e) {
                        System.err.println("Error handling requests file: " + e.getMessage());
                    }
                }
                
            } catch (IOException e) {
                System.err.println("Error reading users file: " + e.getMessage());
            }
        } else {
            System.out.println("You chose not to contact the seller/agent.");
        }
    }
    

    public void makePayment() {
        
    }
}