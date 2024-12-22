package operations;

import java.io.*;
import java.util.*;

import models.Property;
import models.Seller;

public class SellerImp {

    static Seller seller = new Seller();  // initiating seller class
    static Scanner scanner = new Scanner(System.in);
        
     public static void createProperty(String username)
    
    
        { 
            seller.setUsername(username); // we are setting username to get username from Seller class
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Property Type: Choose one of them: apartment, villa, or town: ");
            String type = scanner.nextLine().toLowerCase(); // to get lowercase for all inputs.
            
            while (!type.equals("apartment") && !type.equals("villa") && !type.equals("town")) {
                System.out.println("Invalid Property Type . Only Choose one of them: apartment, villa, or town: ");
                System.out.print("Enter Property Type: ");
                type = scanner.nextLine().toLowerCase();
            }
                    System.out.print("Enter Property Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter Property Price: ");
                    double price = Double.parseDouble(scanner.nextLine()); // if user gives integer it will parse to Double
                    System.out.print("Enter Property Location: ");
                    String location = scanner.nextLine();
                
                    String archivedStatus = "0";
                
                
                    int propertyId = generateUniqueId(); // Generate unique ID
                
                    Property property = new Property(propertyId, type, description, price, location, archivedStatus, seller.getUsername());
                
                    PropertyManager.saveProperty(property); // it saves property to file
           
          System.out.println("Property Added Successfully!");
          
        }
        
    private static int generateUniqueId() {
            int lastId = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] propertyData = line.split(",");
                    lastId = Integer.parseInt(propertyData[0]);  // Since 0' column is Id 
                }
            } catch (IOException e) {
                System.out.println("Error Reading Property IDs: " + e.getMessage());
            }
            return lastId + 1;
        
            
        }
        
    public static void editProperty(String username) {
            displayProperties(seller.getUsername());
        System.out.println("Choose Which Property You Want To Edit: Enter The Property ID");
    
        Scanner scanner = new Scanner(System.in);
            String givenPropertyId = scanner.nextLine();
    
            boolean propertyFound = false; // we first write contradict 
            List<String> allProperties = new ArrayList<>();
    
            try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] propertyData = line.split(",");
    
                    // Check if this is the property is true based on user's request
                    if (propertyData[0].equals(givenPropertyId) && propertyData[5].equals("0")&& propertyData[6].equals(seller.getUsername()) ) {  
                        propertyFound = true; // if all true propertyFound is true it will display
    
                        System.out.println("Enter New Details For The Property (Do Not Want Just Press #Enter# ):");
    
                        System.out.print("Edit Type (" + propertyData[1] + "): ");
                        String newTitle = scanner.nextLine();
                        System.out.print("Edit Description (" + propertyData[2] + "): ");
                        String newDescription = scanner.nextLine();
                        System.out.print("Edit Price (" + propertyData[3] + "): ");
                        String newPrice = scanner.nextLine();
                        System.out.print("Edit Location (" + propertyData[4] + "): ");
                        String newLocation = scanner.nextLine();
    
                        // Update only if new values are provided
                        propertyData[1] = newTitle.isEmpty() ? propertyData[1] : newTitle;   // if user press enter it will not touch it
                        propertyData[2] = newDescription.isEmpty() ? propertyData[2] : newDescription;
                        propertyData[3] = newPrice.isEmpty() ? propertyData[3] : newPrice;
                        propertyData[4] = newLocation.isEmpty() ? propertyData[4] : newLocation;
                    }
                    // Add the (possibly updated) property to the list
                    allProperties.add(String.join(",", propertyData));
                }
            } catch (IOException e) {
                System.out.println("Error Reading Properties: " + e.getMessage());
            }
    
            if (propertyFound) {
                PropertyManager.updatedProperty(allProperties);  // Save the entire updated list
                System.out.println("Property Edited Successfully!");
            } else {
                System.out.println("Property Not Found Or Does Not Belong To You.");
            }
           
        
    }
    
    
    
    
    
    public static void archiveProperty(String username) {
        displayProperties(seller.getUsername());
        System.out.println("Choose Which Property You Want To Archive: Enter The Property ID");
    
        Scanner scanner = new Scanner(System.in);
            String givenPropertyId = scanner.nextLine();
    
            List<String> updatedProperties = new ArrayList<>();
            boolean propertyFound = false;
    
            try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] propertyData = line.split(",");
    
                    // Validate data length
                    if (propertyData.length < 7) { // we have 7 data in properties we are checking it is all true for all of them
                        System.out.println("Invalid Data Format For Property: " + line);
                        updatedProperties.add(line); 
                        continue;
                    }
    
                    if (propertyData[0].equals(givenPropertyId) && propertyData[6].equals(username)) { // if user wants to archive this
                        propertyFound = true;
                        propertyData[5] = "1";   // we set archivedStatus to 1
                        String updatedLine = String.join(",", propertyData);
                        updatedProperties.add(updatedLine); 
    
                        
                    } else {
                        updatedProperties.add(line); 
                    }
                }
            } catch (IOException e) {
                System.out.println("Error Reading Properties: " + e.getMessage());
                scanner.close();
                return;
            }
              if (propertyFound) {
                PropertyManager.updatedProperty(updatedProperties);  // Save the entire updated list
                System.out.println("Property Archived Successfully!");
            } else {
                System.out.println("Property Not Found Or Does Not Belong To You.");
            }
            
        }

    
    
    
    private static void displayProperties(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
            String line;
            System.out.println("\nYour Properties:");
            boolean hasProperties = false;  
    
            while ((line = br.readLine()) != null) {
                String[] propertyData = line.split(",");
                if (propertyData[6].equals(username)&& propertyData[5].equals("0")) {
                    hasProperties = true;
                    System.out.println("Property ID: " + propertyData[0]);
                    System.out.println("Title: " + propertyData[1]);
                    System.out.println("Description: " + propertyData[2]);
                    System.out.println("Price: $" + propertyData[3]);
                    System.out.println("Location: " + propertyData[4]);
                    System.out.println("Owner: " + propertyData[6]);
                    System.out.println("-----------------------");
                }
            }
    
            if (!hasProperties) {
                System.out.println("No Properties Found.");
            }
    
        } catch (IOException e) {
            System.out.println("Error Reading Properties File: " + e.getMessage());
        }
    }

    public static void handleContracts(String username) {
        seller.setUsername(username); // Set the seller's username

        // Read the contracts file
        List<String[]> contracts = readContractsFromFile();

        if (contracts.isEmpty()) {
            System.out.println("No contracts found.");
            return;
        }

        // Display the contracts to the seller
        System.out.println("\n============== Contracts ===============");
        int contractCount = 1;
        for (String[] contract : contracts) {
            System.out.println(contractCount + ". Contract ID: " + contract[0]);
            System.out.println("   Type: " + contract[1]);
            System.out.println("   Description: " + contract[2]);
            System.out.println("   Price: " + contract[3]);
            System.out.println("   Location: " + contract[4]);
            System.out.println("   Owner: " + contract[5]);
            System.out.println("   Buyer: " + contract[6]);
            System.out.println("   Status: " + contract[8]);
            System.out.println("----------------------------------------");
            contractCount++;
        }

        // Ask the seller to select a contract
        System.out.print("Select a contract to respond to (Enter number) or type 0 to return: ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) {
            System.out.println("Returning to menu...");
            return;
        }

        // Make sure the choice is valid
        if (choice > 0 && choice <= contracts.size()) {
            signContract(contracts, choice - 1);
        } else {
            System.out.println("Invalid option. Returning to menu.");
        }
    }

    private static List<String[]> readContractsFromFile() {
        List<String[]> contracts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("contracts.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] contractData = line.split(",");
                contracts.add(contractData);
            }
        } catch (IOException e) {
            System.out.println("Error reading contracts file: " + e.getMessage());
        }
        return contracts;
    }

    private static void signContract(List<String[]> contracts, int index) {
        String[] contractDetails = contracts.get(index);
    
        System.out.println("\nSelected Contract:");
        System.out.println("Contract ID: " + contractDetails[0]);
        System.out.println("Type: " + contractDetails[1]);
        System.out.println("Description: " + contractDetails[2]);
        System.out.println("Price: " + contractDetails[3]);
        System.out.println("Location: " + contractDetails[4]);
        System.out.println("Owner: " + contractDetails[5]);
        System.out.println("Buyer: " + contractDetails[6]);
        System.out.println("Status: " + contractDetails[8]);
        System.out.println("----------------------------------------");
    
        System.out.print("\nDo you want to sign this contract? (yes/no): ");
        String decision = scanner.nextLine().toLowerCase();
    
        if (decision.equals("yes")) {
            contractDetails[8] = "accepted"; // Update status to signed
            System.out.println("Contract signed.");
        } else {
            System.out.println("Contract not signed.");
        }
    
        updateContract(contracts); // Save updated contracts to file
    }
    
    private static void updateContract(List<String[]> contracts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("contracts.csv"))) {
            for (String[] contract : contracts) {
                bw.write(String.join(",", contract));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating contracts file: " + e.getMessage());
        }
    }
}
    