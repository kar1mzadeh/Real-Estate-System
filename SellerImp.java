import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.User;
public class SellerImp 

{
// Archive Properties
// Sign Contract

public void createProperty(User user)
{
    Scanner scanner = new Scanner(System.in);
    
  System.out.print("Enter property title: ");
    String title = scanner.nextLine();
    System.out.print("Enter property description: ");
    String description = scanner.nextLine();
    System.out.print("Enter property price: ");
    double price = Double.parseDouble(scanner.nextLine());
    System.out.print("Enter property location: ");
    String location = scanner.nextLine();

    int propertyId = getNextPropertyId(); // Generate unique ID

    Property property = new Property(propertyId, title, description, price, location, user.getUsername());

    savePropertyToFile(property);
    System.out.println("Property added successfully!");
}
private static int getNextPropertyId() {
    int lastId = 0;
    try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] propertyData = line.split(",");
            lastId = Integer.parseInt(propertyData[0]);  // Assumes ID is in column 0
        }
    } catch (IOException e) {
        System.err.println("Error reading property IDs: " + e.getMessage());
    }
    return lastId + 1;
}
private static void savePropertyToFile(Property property) {
    try (FileWriter fw = new FileWriter("properties.csv", true);
         BufferedWriter bw = new BufferedWriter(fw)) {
        bw.write(property.toCSV());
        bw.newLine();
    } catch (IOException e) {
        System.err.println("Error saving property: " + e.getMessage());
    }
    
    
}


public void editProperty(String username) {
    displayUserProperties(username);
    System.out.println("Choose which property you want to edit: Enter the Property ID");

    Scanner scanner = new Scanner(System.in);
    String givenPropertyId = scanner.nextLine();

    boolean propertyFound = false;
    List<String> allProperties = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] propertyData = line.split(",");

            // Check if this is the property to update
            if (propertyData[0].equals(givenPropertyId) && propertyData[5].equals(username)) {
                propertyFound = true;

                System.out.println("Enter new details for the property (leave blank to keep current value):");

                System.out.print("New Title (" + propertyData[1] + "): ");
                String newTitle = scanner.nextLine();
                System.out.print("New Description (" + propertyData[2] + "): ");
                String newDescription = scanner.nextLine();
                System.out.print("New Price (" + propertyData[3] + "): ");
                String newPrice = scanner.nextLine();
                System.out.print("New Location (" + propertyData[4] + "): ");
                String newLocation = scanner.nextLine();

                // Update only if new values are provided
                propertyData[1] = newTitle.isEmpty() ? propertyData[1] : newTitle;
                propertyData[2] = newDescription.isEmpty() ? propertyData[2] : newDescription;
                propertyData[3] = newPrice.isEmpty() ? propertyData[3] : newPrice;
                propertyData[4] = newLocation.isEmpty() ? propertyData[4] : newLocation;
            }
            // Add the (possibly updated) property to the list
            allProperties.add(String.join(",", propertyData));
        }
    } catch (IOException e) {
        System.out.println("Error reading properties: " + e.getMessage());
    }

    if (propertyFound) {
        saveUpdatedPropertyToFile(allProperties);  // Save the entire updated list
        System.out.println("Property updated successfully!");
    } else {
        System.out.println("Property not found or does not belong to you.");
    }
}




private void saveUpdatedPropertyToFile(List<String> properties) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("properties.csv"))) {
        for (String property : properties) {
            bw.write(property);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving updated properties: " + e.getMessage());
    }
}

private static void displayUserProperties(String username) {
    try (BufferedReader br = new BufferedReader(new FileReader("properties.csv"))) {
        String line;
        System.out.println("\nYour Properties:");
      

        boolean hasProperties = false;  

        while ((line = br.readLine()) != null) {
            String[] propertyData = line.split(",");
            // Assuming the 6th field (index 5) in CSV stores the owner's username
            if (propertyData[5].equals(username)) {
                hasProperties = true;
                System.out.println("Property ID: " + propertyData[0]);
                System.out.println("Title: " + propertyData[1]);
                System.out.println("Description: " + propertyData[2]);
                System.out.println("Price: $" + propertyData[3]);
                System.out.println("Location: " + propertyData[4]);
                System.out.println("-----------------------");
            }
        }

        if (!hasProperties) {
            System.out.println("No properties found.");
        }

    } catch (IOException e) {
        System.out.println("Error reading properties file: " + e.getMessage());
    }
}



}


