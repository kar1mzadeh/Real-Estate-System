package seller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import models.Property;

public class PropertyManager {
        public static void saveProperty(Property property) {  
        try (FileWriter fw = new FileWriter("properties.csv", true);      // to properties csv file
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(property.toCSV());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error Saving Property: " + e.getMessage());
        }
        
        
    }

    
    public static void updatedProperty(List<String> properties) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter("properties.csv"))) {    // it reads from lists after saves to file
        for (String property : properties) {
            bw.write(property);
            bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error Saving Updated Properties: " + e.getMessage());
    }
}
}
