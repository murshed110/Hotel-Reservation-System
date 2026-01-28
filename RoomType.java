public enum RoomType {
    STANDARD("Standard Room"),
    DELUXE("Deluxe Room"),
    SUITE("Suite"),
    EXECUTIVE("Executive Room"),
    PRESIDENTIAL("Presidential Suite");
    
    private final String displayName;
    
    RoomType(String displayName) {
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