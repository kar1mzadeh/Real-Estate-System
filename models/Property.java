
package models;

public class Property {

    private int propertyId;
    private String type;
    private String description;
    private double price;
    private String location;
    private String ownerUsername;
    private String archivedStatus;

    public Property(int propertyId, String type, String description, double price, String location, String ownerUsername, String archivedStatus) {
        this.propertyId = propertyId;
        this.type = type;
        this.description = description;
        this.price = price;
        this.location = location;
        this.ownerUsername=ownerUsername;
        this.archivedStatus=archivedStatus;
    }

    public Property()
    {
        
    }


    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
   




    public String getArchivedStatus() {
        return archivedStatus;
    }




    public void setArchivedStatus(String archivedStatus) {
        this.archivedStatus = archivedStatus;
    }

    public String toCSV() {
        return propertyId + "," + type + "," + description + "," + price + "," + location + "," +  ownerUsername + "," + archivedStatus ;
    }
    
}