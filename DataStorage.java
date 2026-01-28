import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    
    // Save data to serialized file
    public static <T> void saveData(List<T> data, String filename) {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }
            
            FileOutputStream fileOut = new FileOutputStream("data/" + filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
            System.out.println("Data saved to " + filename + " (" + data.size() + " records)");
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }
    
    // Load data from serialized file
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadData(String filename) {
        File file = new File("data/" + filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<T> data = (List<T>) in.readObject();
            in.close();
            fileIn.close();
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
            
            // If loading fails, try to recover by creating new file
            if (file.exists()) {
                System.out.println("Creating new " + filename + " due to corruption");
                file.delete();
            }
            return new ArrayList<>();
        }
    }
    
    // Save individual room
    public static void saveRoom(Room room) {
        List<Room> rooms = loadData("rooms.ser");
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(room.getRoomNumber())) {
                rooms.set(i, room);
                saveData(rooms, "rooms.ser");
                return;
            }
        }
        rooms.add(room);
        saveData(rooms, "rooms.ser");
    }
    
    // Save individual customer
    public static void saveCustomer(Customer customer) {
        List<Customer> customers = loadData("customers.ser");
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
                customers.set(i, customer);
                saveData(customers, "customers.ser");
                return;
            }
        }
        customers.add(customer);
        saveData(customers, "customers.ser");
    }
    
    // Save individual reservation
    public static void saveReservation(Reservation reservation) {
        List<Reservation> reservations = loadData("reservations.ser");
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservation.getReservationId())) {
                reservations.set(i, reservation);
                saveData(reservations, "reservations.ser");
                return;
            }
        }
        reservations.add(reservation);
        saveData(reservations, "reservations.ser");
    }
    
    // Delete methods
    public static boolean deleteRoom(String roomNumber) {
        List<Room> rooms = loadData("rooms.ser");
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber().equals(roomNumber)) {
                rooms.remove(i);
                saveData(rooms, "rooms.ser");
                return true;
            }
        }
        return false;
    }
    
    public static boolean deleteCustomer(String customerId) {
        List<Customer> customers = loadData("customers.ser");
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equals(customerId)) {
                customers.remove(i);
                saveData(customers, "customers.ser");
                return true;
            }
        }
        return false;
    }
    
    public static boolean deleteReservation(String reservationId) {
        List<Reservation> reservations = loadData("reservations.ser");
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservationId)) {
                Room room = reservations.get(i).getRoom();
                if (room != null) {
                    room.setAvailable(true);
                    saveRoom(room);
                }
                reservations.remove(i);
                saveData(reservations, "reservations.ser");
                return true;
            }
        }
        return false;
    }
}