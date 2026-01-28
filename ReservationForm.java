import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ReservationForm extends JFrame {
    private List<Reservation> reservations;
    private List<Customer> customers;
    private List<Room> rooms;
    private DefaultTableModel tableModel;
    private JTable reservationTable;
    private JFrame parentFrame;
    
    // Form components
    private JTextField reservationIdField;
    private JComboBox<Customer> customerCombo;
    private JComboBox<Room> roomCombo;
    private JComboBox<Integer> dayInCombo, yearInCombo;
    private JComboBox<String> monthInCombo;
    private JComboBox<Integer> dayOutCombo, yearOutCombo;
    private JComboBox<String> monthOutCombo;
    private JSpinner guestsSpinner;
    private JComboBox<ReservationStatus> statusCombo;
    private JTextArea requestsArea;
    private JLabel totalAmountLabel;
    private JLabel daysLabel;
    
    public ReservationForm() {
        this(null);
    }
    
    public ReservationForm(JFrame parent) {
        this.parentFrame = parent;
        setTitle("Hotel Reservation System - Manage Reservations");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add back button
        addBackButton();
        
        // Load data
        loadData();
        
        initComponents();
        loadTableData();
    }
    
    private void addBackButton() {
        if (parentFrame == null) return;
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(70, 130, 180));
        
        JButton backButton = new JButton("← Back to Main");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            this.dispose();
            parentFrame.setVisible(true);
        });
        
        menuBar.add(backButton);
        setJMenuBar(menuBar);
    }
    
    private void loadData() {
        reservations = DataStorage.loadData("reservations.ser");
        customers = DataStorage.loadData("customers.ser");
        rooms = DataStorage.loadData("rooms.ser");
        
        if (reservations == null) reservations = new ArrayList<>();
        if (customers == null) customers = new ArrayList<>();
        if (rooms == null) rooms = new ArrayList<>();
    }
    
    private void initComponents() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // LEFT PANEL - Form
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setPreferredSize(new Dimension(450, 0));
        
        // Form container
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            "New Reservation",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // 1. Reservation ID
        addFormLabel("Reservation ID:*", 0, 0, gbc, formPanel);
        reservationIdField = createTextField();
        gbc.gridx = 1;
        formPanel.add(reservationIdField, gbc);
        
        // 2. Customer
        addFormLabel("Customer:*", 0, 1, gbc, formPanel);
        customerCombo = new JComboBox<>();
        loadCustomerCombo();
        customerCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customerCombo.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        formPanel.add(customerCombo, gbc);
        
        // 3. Room
        addFormLabel("Room:*", 0, 2, gbc, formPanel);
        roomCombo = new JComboBox<>();
        loadRoomCombo();
        roomCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomCombo.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        formPanel.add(roomCombo, gbc);
        
        // 4. Check-in Date
        addFormLabel("Check-in Date:*", 0, 3, gbc, formPanel);
        JPanel checkInPanel = createDatePanel(true);
        gbc.gridx = 1;
        formPanel.add(checkInPanel, gbc);
        
        // 5. Check-out Date
        addFormLabel("Check-out Date:*", 0, 4, gbc, formPanel);
        JPanel checkOutPanel = createDatePanel(false);
        gbc.gridx = 1;
        formPanel.add(checkOutPanel, gbc);
        
        // 6. Number of Guests
        addFormLabel("Number of Guests:*", 0, 5, gbc, formPanel);
        guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        guestsSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ((JSpinner.DefaultEditor) guestsSpinner.getEditor()).getTextField().setColumns(3);
        gbc.gridx = 1;
        formPanel.add(guestsSpinner, gbc);
        
        // 7. Status
        addFormLabel("Status:", 0, 6, gbc, formPanel);
        statusCombo = new JComboBox<>(ReservationStatus.values());
        statusCombo.setSelectedItem(ReservationStatus.CONFIRMED);
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusCombo.setPreferredSize(new Dimension(250, 28));
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        // 8. Total Amount
        addFormLabel("Total Amount:", 0, 7, gbc, formPanel);
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        totalPanel.setBackground(Color.WHITE);
        
        daysLabel = new JLabel("Days: 0");
        daysLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        daysLabel.setForeground(new Color(70, 130, 180));
        
        totalAmountLabel = new JLabel("$0.00");
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalAmountLabel.setForeground(new Color(0, 128, 0));
        
        totalPanel.add(daysLabel);
        totalPanel.add(Box.createHorizontalStrut(15));
        totalPanel.add(totalAmountLabel);
        gbc.gridx = 1;
        formPanel.add(totalPanel, gbc);
        
        // 9. Special Requests
        addFormLabel("Special Requests:", 0, 8, gbc, formPanel);
        requestsArea = new JTextArea(3, 20);
        requestsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        requestsArea.setLineWrap(true);
        requestsArea.setWrapStyleWord(true);
        JScrollPane requestsScroll = new JScrollPane(requestsArea);
        requestsScroll.setPreferredSize(new Dimension(250, 80));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(requestsScroll, gbc);
        
        // Calculate button
        gbc.gridx = 1; gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton calculateButton = createStyledButton("Calculate Total", new Color(70, 130, 180), 12);
        calculateButton.addActionListener(e -> calculateTotalAmount());
        calculateButton.setPreferredSize(new Dimension(150, 30));
        formPanel.add(calculateButton, gbc);
        
        formContainer.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(formContainer, BorderLayout.CENTER);
        
        // Form buttons panel
        JPanel formButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        formButtonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        formButtonsPanel.setBackground(Color.WHITE);
        
        JButton addButton = createStyledButton("Add Reservation", new Color(46, 139, 87), 12);
        JButton updateButton = createStyledButton("Update", new Color(70, 130, 180), 12);
        JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60), 12);
        JButton clearButton = createStyledButton("Clear Form", new Color(128, 128, 128), 12);
        
        addButton.addActionListener(e -> addReservation());
        updateButton.addActionListener(e -> updateReservation());
        deleteButton.addActionListener(e -> deleteReservation());
        clearButton.addActionListener(e -> clearForm());
        
        formButtonsPanel.add(addButton);
        formButtonsPanel.add(updateButton);
        formButtonsPanel.add(deleteButton);
        formButtonsPanel.add(clearButton);
        
        leftPanel.add(formButtonsPanel, BorderLayout.SOUTH);
        
        // RIGHT PANEL - Table
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        
        // Table container
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
            "Existing Reservations",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        // Create table
        String[] columns = {"Reservation ID", "Customer", "Room", "Check-in", "Check-out", 
                           "Guests", "Status", "Amount", "Booking Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservationTable = new JTable(tableModel);
        reservationTable.setRowHeight(30);
        reservationTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        reservationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        reservationTable.getTableHeader().setBackground(new Color(70, 130, 180));
        reservationTable.getTableHeader().setForeground(Color.WHITE);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationTable.setGridColor(new Color(220, 220, 220));
        reservationTable.setShowGrid(true);
        
        // Center align some columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            if (i == 0 || i == 2 || i == 3 || i == 4 || i == 5) {
                reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        // Right align amount column
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        reservationTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        
        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && reservationTable.getSelectedRow() != -1) {
                loadSelectedReservation();
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(reservationTable);
        tableContainer.add(tableScroll, BorderLayout.CENTER);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton searchButton = createStyledButton("Search", new Color(70, 130, 180), 12);
        JButton refreshButton = createStyledButton("Refresh", new Color(46, 139, 87), 12);
        
        searchButton.addActionListener(e -> searchReservations(searchField.getText()));
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadTableData();
            loadCustomerCombo();
            loadRoomCombo();
        });
        
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        tableContainer.add(searchPanel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statsPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statsPanel.setBackground(new Color(240, 248, 255));
        
        JLabel statsLabel = new JLabel(getStatisticsText());
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statsLabel.setForeground(new Color(70, 130, 180));
        statsPanel.add(statsLabel);
        
        tableContainer.add(statsPanel, BorderLayout.SOUTH);
        
        rightPanel.add(tableContainer, BorderLayout.CENTER);
        
        // Add both panels to main
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void addFormLabel(String text, int x, int y, GridBagConstraints gbc, JPanel panel) {
        gbc.gridx = x; gbc.gridy = y;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(60, 60, 60));
        panel.add(label, gbc);
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private JPanel createDatePanel(boolean isCheckIn) {
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        datePanel.setBackground(Color.WHITE);
        
        // Day combo
        JComboBox<Integer> dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayCombo.addItem(i);
        }
        dayCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dayCombo.setPreferredSize(new Dimension(55, 28));
        
        // Month combo
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        monthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monthCombo.setPreferredSize(new Dimension(100, 28));
        
        // Year combo
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 1; i <= currentYear + 2; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        yearCombo.setPreferredSize(new Dimension(70, 28));
        
        // Set dates
        Calendar cal = Calendar.getInstance();
        if (!isCheckIn) {
            cal.add(Calendar.DATE, 1);
        }
        dayCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        monthCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        yearCombo.setSelectedItem(cal.get(Calendar.YEAR));
        
        datePanel.add(dayCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearCombo);
        
        // Store references
        if (isCheckIn) {
            dayInCombo = dayCombo;
            monthInCombo = monthCombo;
            yearInCombo = yearCombo;
        } else {
            dayOutCombo = dayCombo;
            monthOutCombo = monthCombo;
            yearOutCombo = yearCombo;
        }
        
        return datePanel;
    }
    
    private JButton createStyledButton(String text, Color color, int fontSize) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private String getStatisticsText() {
        int totalReservations = reservations.size();
        int availableRooms = getAvailableRoomsCount();
        int todayCheckIns = getTodaysCheckIns();
        
        return String.format("📊 Total Reservations: %d | Available Rooms: %d | Today's Check-ins: %d",
            totalReservations, availableRooms, todayCheckIns);
    }
    
    private void calculateTotalAmount() {
        try {
            Room selectedRoom = (Room) roomCombo.getSelectedItem();
            if (selectedRoom == null) {
                daysLabel.setText("Days: 0");
                totalAmountLabel.setText("$0.00");
                return;
            }
            
            Date checkInDate = getDateFromComboBoxes(dayInCombo, monthInCombo, yearInCombo);
            Date checkOutDate = getDateFromComboBoxes(dayOutCombo, monthOutCombo, yearOutCombo);
            
            if (checkInDate == null || checkOutDate == null) {
                daysLabel.setText("Days: 0");
                totalAmountLabel.setText("$0.00");
                return;
            }
            
            if (checkOutDate.before(checkInDate)) {
                daysLabel.setText("Days: Invalid");
                totalAmountLabel.setText("$0.00");
                return;
            }
            
            long diff = checkOutDate.getTime() - checkInDate.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if (days == 0) days = 1;
            
            double total = days * selectedRoom.getPricePerNight();
            daysLabel.setText("Days: " + days);
            totalAmountLabel.setText(String.format("$%.2f", total));
            
        } catch (Exception e) {
            daysLabel.setText("Days: 0");
            totalAmountLabel.setText("$0.00");
        }
    }
    
    private Date getDateFromComboBoxes(JComboBox<Integer> day, JComboBox<String> month, JComboBox<Integer> year) {
        try {
            int d = (Integer) day.getSelectedItem();
            int m = month.getSelectedIndex();
            int y = (Integer) year.getSelectedItem();
            
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d);
            return cal.getTime();
        } catch (Exception e) {
            return null;
        }
    }
    
    private int getAvailableRoomsCount() {
        int count = 0;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                count++;
            }
        }
        return count;
    }
    
    private int getTodaysCheckIns() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        int count = 0;
        
        for (Reservation res : reservations) {
            if (sdf.format(res.getCheckInDate()).equals(today)) {
                count++;
            }
        }
        return count;
    }
    
    private void loadCustomerCombo() {
        customerCombo.removeAllItems();
        for (Customer customer : customers) {
            customerCombo.addItem(customer);
        }
        customerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer c = (Customer) value;
                    setText(c.getFullName() + " (" + c.getCustomerId() + ")");
                }
                return this;
            }
        });
    }
    
    private void loadRoomCombo() {
        roomCombo.removeAllItems();
        for (Room room : rooms) {
            roomCombo.addItem(room);
        }
        roomCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Room) {
                    Room r = (Room) value;
                    setText("Room " + r.getRoomNumber() + " - " + r.getRoomType() + 
                           " ($" + r.getPricePerNight() + ")");
                }
                return this;
            }
        });
    }
    
    private void addReservation() {
        try {
            String reservationId = reservationIdField.getText().trim();
            Customer customer = (Customer) customerCombo.getSelectedItem();
            Room room = (Room) roomCombo.getSelectedItem();
            Date checkInDate = getDateFromComboBoxes(dayInCombo, monthInCombo, yearInCombo);
            Date checkOutDate = getDateFromComboBoxes(dayOutCombo, monthOutCombo, yearOutCombo);
            int numberOfGuests = (int) guestsSpinner.getValue();
            ReservationStatus status = (ReservationStatus) statusCombo.getSelectedItem();
            String specialRequests = requestsArea.getText().trim();
            
            // Validation
            if (reservationId.isEmpty()) {
                showError("Please enter Reservation ID!", reservationIdField);
                return;
            }
            
            if (customer == null) {
                showError("Please select a customer!", customerCombo);
                return;
            }
            
            if (room == null) {
                showError("Please select a room!", roomCombo);
                return;
            }
            
            if (checkInDate == null || checkOutDate == null) {
                showError("Please select both check-in and check-out dates!", null);
                return;
            }
            
            if (checkOutDate.before(checkInDate)) {
                showError("Check-out date must be after check-in date!", null);
                return;
            }
            
            // Check if reservation ID already exists
            for (Reservation reservation : reservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    showError("Reservation ID already exists!", reservationIdField);
                    return;
                }
            }
            
            // Create reservation
            Reservation reservation = new Reservation(reservationId, customer, room, 
                                                     checkInDate, checkOutDate, 
                                                     numberOfGuests, specialRequests);
            reservation.setStatus(status);
            
            // Update room availability
            if (status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN) {
                room.setAvailable(false);
                DataStorage.saveRoom(room);
            }
            
            // Save reservation
            DataStorage.saveReservation(reservation);
            
            // Reload data
            reservations = DataStorage.loadData("reservations.ser");
            rooms = DataStorage.loadData("rooms.ser");
            loadTableData();
            loadRoomCombo();
            clearForm();
            
            showSuccess("Reservation added successfully!");
        } catch (Exception e) {
            showError("Error: " + e.getMessage(), null);
        }
    }
    
    private void updateReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a reservation to update!", null);
            return;
        }
        
        try {
            String reservationId = reservationIdField.getText().trim();
            Customer customer = (Customer) customerCombo.getSelectedItem();
            Room room = (Room) roomCombo.getSelectedItem();
            Date checkInDate = getDateFromComboBoxes(dayInCombo, monthInCombo, yearInCombo);
            Date checkOutDate = getDateFromComboBoxes(dayOutCombo, monthOutCombo, yearOutCombo);
            int numberOfGuests = (int) guestsSpinner.getValue();
            ReservationStatus status = (ReservationStatus) statusCombo.getSelectedItem();
            String specialRequests = requestsArea.getText().trim();
            
            // Find and update reservation
            for (Reservation reservation : reservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    Room oldRoom = reservation.getRoom();
                    
                    reservation.setCustomer(customer);
                    reservation.setRoom(room);
                    reservation.setCheckInDate(checkInDate);
                    reservation.setCheckOutDate(checkOutDate);
                    reservation.setNumberOfGuests(numberOfGuests);
                    reservation.setStatus(status);
                    reservation.setSpecialRequests(specialRequests);
                    
                    // Update room availability
                    if (!oldRoom.getRoomNumber().equals(room.getRoomNumber())) {
                        oldRoom.setAvailable(true);
                        DataStorage.saveRoom(oldRoom);
                        
                        if (status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN) {
                            room.setAvailable(false);
                        }
                    } else {
                        if (status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN) {
                            room.setAvailable(false);
                        } else if (status == ReservationStatus.CHECKED_OUT || 
                                   status == ReservationStatus.CANCELLED) {
                            room.setAvailable(true);
                        }
                    }
                    
                    DataStorage.saveRoom(room);
                    DataStorage.saveReservation(reservation);
                    break;
                }
            }
            
            // Reload data
            reservations = DataStorage.loadData("reservations.ser");
            rooms = DataStorage.loadData("rooms.ser");
            loadTableData();
            loadRoomCombo();
            
            showSuccess("Reservation updated successfully!");
        } catch (Exception e) {
            showError("Error: " + e.getMessage(), null);
        }
    }
    
    private void deleteReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a reservation to delete!", null);
            return;
        }
        
        String reservationId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete reservation " + reservationId + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DataStorage.deleteReservation(reservationId)) {
                reservations = DataStorage.loadData("reservations.ser");
                rooms = DataStorage.loadData("rooms.ser");
                loadTableData();
                loadRoomCombo();
                clearForm();
                showSuccess("Reservation deleted successfully!");
            } else {
                showError("Error deleting reservation!", null);
            }
        }
    }
    
    private void loadSelectedReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow >= 0) {
            String reservationId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Reservation reservation : reservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    reservationIdField.setText(reservation.getReservationId());
                    
                    // Select customer
                    for (int i = 0; i < customerCombo.getItemCount(); i++) {
                        if (((Customer)customerCombo.getItemAt(i)).getCustomerId()
                                .equals(reservation.getCustomer().getCustomerId())) {
                            customerCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    // Select room
                    for (int i = 0; i < roomCombo.getItemCount(); i++) {
                        if (((Room)roomCombo.getItemAt(i)).getRoomNumber()
                                .equals(reservation.getRoom().getRoomNumber())) {
                            roomCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    // Set dates
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(reservation.getCheckInDate());
                    dayInCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
                    monthInCombo.setSelectedIndex(cal.get(Calendar.MONTH));
                    yearInCombo.setSelectedItem(cal.get(Calendar.YEAR));
                    
                    cal.setTime(reservation.getCheckOutDate());
                    dayOutCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
                    monthOutCombo.setSelectedIndex(cal.get(Calendar.MONTH));
                    yearOutCombo.setSelectedItem(cal.get(Calendar.YEAR));
                    
                    guestsSpinner.setValue(reservation.getNumberOfGuests());
                    statusCombo.setSelectedItem(reservation.getStatus());
                    requestsArea.setText(reservation.getSpecialRequests());
                    
                    calculateTotalAmount();
                    break;
                }
            }
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Reservation reservation : reservations) {
            tableModel.addRow(new Object[]{
                reservation.getReservationId(),
                reservation.getCustomer().getFullName(),
                reservation.getRoom().getRoomNumber(),
                sdf.format(reservation.getCheckInDate()),
                sdf.format(reservation.getCheckOutDate()),
                reservation.getNumberOfGuests(),
                reservation.getStatus(),
                String.format("$%.2f", reservation.getTotalAmount()),
                sdf.format(reservation.getBookingDate())
            });
        }
    }
    
    private void searchReservations(String searchText) {
        tableModel.setRowCount(0);
        String searchLower = searchText.toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().toLowerCase().contains(searchLower) ||
                reservation.getCustomer().getFullName().toLowerCase().contains(searchLower) ||
                reservation.getRoom().getRoomNumber().toLowerCase().contains(searchLower) ||
                reservation.getStatus().toString().toLowerCase().contains(searchLower)) {
                tableModel.addRow(new Object[]{
                    reservation.getReservationId(),
                    reservation.getCustomer().getFullName(),
                    reservation.getRoom().getRoomNumber(),
                    sdf.format(reservation.getCheckInDate()),
                    sdf.format(reservation.getCheckOutDate()),
                    reservation.getNumberOfGuests(),
                    reservation.getStatus(),
                    String.format("$%.2f", reservation.getTotalAmount()),
                    sdf.format(reservation.getBookingDate())
                });
            }
        }
    }
    
    private void clearForm() {
        reservationIdField.setText("");
        customerCombo.setSelectedIndex(-1);
        roomCombo.setSelectedIndex(-1);
        
        Calendar cal = Calendar.getInstance();
        dayInCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        monthInCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        yearInCombo.setSelectedItem(cal.get(Calendar.YEAR));
        
        cal.add(Calendar.DATE, 1);
        dayOutCombo.setSelectedItem(cal.get(Calendar.DAY_OF_MONTH));
        monthOutCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        yearOutCombo.setSelectedItem(cal.get(Calendar.YEAR));
        
        guestsSpinner.setValue(1);
        statusCombo.setSelectedItem(ReservationStatus.CONFIRMED);
        requestsArea.setText("");
        daysLabel.setText("Days: 0");
        totalAmountLabel.setText("$0.00");
        reservationTable.clearSelection();
    }
    
    private void showError(String message, JComponent focusComponent) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        if (focusComponent != null) {
            focusComponent.requestFocus();
        }
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ReservationForm frame = new ReservationForm();
            frame.setVisible(true);
        });
    }
}