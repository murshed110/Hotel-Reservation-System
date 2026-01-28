import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private int capacity;
    private boolean hasBalcony;
    private boolean available;
    private String description;
    
    // Default constructor
    public Room() {
        this.roomNumber = "";
        this.roomType = RoomType.STANDARD;
        this.pricePerNight = 100.0;
        this.capacity = 2;
        this.hasBalcony = false;
        this.available = true;
        this.description = "";
    }
    
    // Constructor with String roomType (for compatibility)
    public Room(String roomNumber, String roomType, double pricePerNight, 
                int capacity, boolean hasBalcony, String description) {
        this.roomNumber = roomNumber;
        this.roomType = stringToRoomType(roomType);
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.hasBalcony = hasBalcony;
        this.available = true;
        this.description = description;
    }
    
    // Constructor with RoomType enum
    public Room(String roomNumber, RoomType roomType, double pricePerNight, 
                int capacity, boolean hasBalcony, String description) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.hasBalcony = hasBalcony;
        this.available = true;
        this.description = description;
    }
    
    // Method to convert String to RoomType
    private RoomType stringToRoomType(String roomTypeStr) {
        if (roomTypeStr == null) return RoomType.STANDARD;
        
        String upper = roomTypeStr.toUpperCase().replace(" ", "_");
        try {
            return RoomType.valueOf(upper);
        } catch (IllegalArgumentException e) {
            // Handle common variations
            switch (roomTypeStr.toLowerCase()) {
                case "standard": return RoomType.STANDARD;
                case "deluxe": return RoomType.DELUXE;
                case "suite": return RoomType.SUITE;
                case "executive": return RoomType.EXECUTIVE;
                case "presidential": return RoomType.PRESIDENTIAL;
                default: return RoomType.STANDARD;
            }
        }
    }
    
    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    
    // For compatibility with String
    public void setRoomType(String roomTypeStr) { 
        this.roomType = stringToRoomType(roomTypeStr); 
    }
    
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean isHasBalcony() { return hasBalcony; }
    public void setHasBalcony(boolean hasBalcony) { this.hasBalcony = hasBalcony; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return "Room " + roomNumber + " (" + roomType + ") - $" + pricePerNight + "/night";
    }
}