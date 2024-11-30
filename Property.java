

public class Property {

    private int propertyId;
    private String title;
    private String description;
    private double price;
    private String location;
    private String ownerUsername;

    public Property(int propertyId, String title, String description, double price, String location, String ownerUsername) {
        this.propertyId = propertyId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.ownerUsername=ownerUsername;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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