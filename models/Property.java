package models;

public class Property {

    private int propertyId;
    private String title;
    private String description;
    private double price;
    private String location;
    private String ownerUsername;
    private String type;

    public Property(int propertyId, String type, String location, double price, String description, String ownerUsername) {
        this.propertyId = propertyId;
        this.type = type;
        this.description = description;
        this.price = price;
        this.location = location;
        this.ownerUsername=ownerUsername;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
    public String toCSV() {
        return propertyId + "," + title + "," + description + "," + price + "," + location + "," + ownerUsername;
    }
    
}