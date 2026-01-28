import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String reservationId;
    private Customer customer;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private double totalAmount;
    private Date bookingDate;
    private String specialRequests;
    
    // Default constructor
    public Reservation() {
        this.reservationId = "";
        this.customer = null;
        this.room = null;
        this.checkInDate = new Date();
        this.checkOutDate = new Date();
        this.numberOfGuests = 1;
        this.status = ReservationStatus.PENDING;
        this.totalAmount = 0.0;
        this.bookingDate = new Date();
        this.specialRequests = "";
    }
    
    public Reservation(String reservationId, Customer customer, Room room, 
                       Date checkInDate, Date checkOutDate, int numberOfGuests,
                       String specialRequests) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.status = ReservationStatus.CONFIRMED;
        this.specialRequests = specialRequests;
        this.bookingDate = new Date();
        calculateTotalAmount();
    }
    
    private void calculateTotalAmount() {
        if (room == null || checkInDate == null || checkOutDate == null) return;
        
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        if (days == 0) days = 1;
        this.totalAmount = days * room.getPricePerNight();
    }
    
    // Getters and Setters
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { 
        this.customer = customer; 
        calculateTotalAmount();
    }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { 
        this.room = room; 
        calculateTotalAmount();
    }
    
    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { 
        this.checkInDate = checkInDate; 
        calculateTotalAmount();
    }
    
    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { 
        this.checkOutDate = checkOutDate; 
        calculateTotalAmount();
    }
    
    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
    
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }
    
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    
    public int getNumberOfDays() {
        if (checkInDate == null || checkOutDate == null) return 0;
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
    
    @Override
    public String toString() {
        return "Reservation #" + reservationId + " - " + 
               (customer != null ? customer.getFullName() : "No Customer") + 
               " in " + (room != null ? room.getRoomNumber() : "No Room");
    }
}