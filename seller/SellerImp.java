package seller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Property;

public class SellerImp {

    static Seller seller = new Seller();  // initiating seller class
        
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
                    System.out.println("Owner" + propertyData[6]);
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

    



}
