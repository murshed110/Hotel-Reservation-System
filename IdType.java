public enum IdType {
    PASSPORT("Passport"),
    DRIVERS_LICENSE("Driver's License"),
    NATIONAL_ID("National ID"),
    VOTERS_ID("Voter's ID"),
    OTHER("Other");
    
    private final String displayName;
    
    IdType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}