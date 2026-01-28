import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class RoomForm extends JFrame {
    private List<Room> rooms;
    private DefaultTableModel tableModel;
    private JTable roomTable;
    private JFrame parentFrame;
    
    // Form components
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeCombo;
    private JTextField priceField;
    private JSpinner capacitySpinner;
    private JCheckBox balconyCheck;
    private JCheckBox availableCheck;
    private JTextArea descriptionArea;
    private JLabel statusLabel;
    
    public RoomForm() {
        this(null);
    }
    
    public RoomForm(JFrame parent) {
        this.parentFrame = parent;
        setTitle("Room Management");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add back button
        Main.addBackButton(this, parent);
        
        rooms = DataStorage.loadData("rooms.ser");
        if (rooms == null) rooms = new ArrayList<>();
        
        initComponents();
        loadTableData();
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Left panel for form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Room Details"));
        formPanel.setPreferredSize(new Dimension(450, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Room Number - Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Room Number:*"), gbc);
        roomNumberField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(roomNumberField, gbc);
        
        // Room Type - Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Room Type:*"), gbc);
        roomTypeCombo = new JComboBox<>(new String[]{
            "STANDARD", "DELUXE", "SUITE", "EXECUTIVE", "PRESIDENTIAL"
        });
        gbc.gridx = 1;
        formPanel.add(roomTypeCombo, gbc);
        
        // Price - Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Price per Night:*"), gbc);
        priceField = new JTextField(20);
        priceField.setText("100.00");
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);
        
        // Capacity - Row 3
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Capacity:*"), gbc);
        capacitySpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        gbc.gridx = 1;
        formPanel.add(capacitySpinner, gbc);
        
        // Balcony - Row 4
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Has Balcony:"), gbc);
        balconyCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(balconyCheck, gbc);
        
        // Available - Row 5
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Available:"), gbc);
        availableCheck = new JCheckBox();
        availableCheck.setSelected(true);
        gbc.gridx = 1;
        formPanel.add(availableCheck, gbc);
        
        // Description - Row 6
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Description:"), gbc);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        // Button panel - Row 7
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton addButton = createStyledButton("Add", new Color(46, 139, 87));
        JButton updateButton = createStyledButton("Update", new Color(70, 130, 180));
        JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60));
        JButton clearButton = createStyledButton("Clear", new Color(128, 128, 128));
        
        addButton.addActionListener(e -> addRoom());
        updateButton.addActionListener(e -> updateRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);
        
        // Right panel for table
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Rooms List"));
        
        // Create table
        String[] columns = {"Room No", "Type", "Price", "Capacity", "Balcony", "Available", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return Integer.class;
                if (columnIndex == 4 || columnIndex == 5) return Boolean.class;
                return String.class;
            }
        };
        
        roomTable = new JTable(tableModel);
        TableHelper.setupTable(roomTable);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Custom renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        roomTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        roomTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        roomTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        roomTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                loadSelectedRoom();
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(roomTable);
        tableScroll.setPreferredSize(new Dimension(600, 0));
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = createStyledButton("Search", new Color(70, 130, 180));
        JButton refreshButton = createStyledButton("Refresh", new Color(46, 139, 87));
        
        searchButton.addActionListener(e -> searchRooms(searchField.getText()));
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadTableData();
        });
        
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        
        // Add to main panel
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Status bar
        statusLabel = new JLabel("Total Rooms: " + rooms.size());
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel.setBackground(new Color(240, 240, 240));
        statusLabel.setOpaque(true);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Add sample data if empty
        if (rooms.isEmpty()) {
            addSampleRooms();
        }
    }
    
    private void addSampleRooms() {
        // Add some sample rooms for demonstration
        rooms.add(new Room("101", "STANDARD", 100.00, 2, true, "Standard room with basic amenities"));
        rooms.add(new Room("102", "STANDARD", 120.00, 2, false, "Standard room, city view"));
        rooms.add(new Room("201", "DELUXE", 180.00, 3, true, "Deluxe room with king size bed"));
        rooms.add(new Room("202", "DELUXE", 200.00, 4, true, "Deluxe family room"));
        rooms.add(new Room("301", "SUITE", 300.00, 4, true, "Executive suite with living area"));
        rooms.add(new Room("302", "SUITE", 350.00, 2, true, "Honeymoon suite with jacuzzi"));
        
        DataStorage.saveData(rooms, "rooms.ser");
        loadTableData();
    }
    
    private void addRoom() {
        try {
            String roomNumber = roomNumberField.getText().trim();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String priceText = priceField.getText().trim();
            int capacity = (int) capacitySpinner.getValue();
            boolean hasBalcony = balconyCheck.isSelected();
            boolean isAvailable = availableCheck.isSelected();
            String description = descriptionArea.getText().trim();
            
            // Validation
            if (roomNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter room number!");
                return;
            }
            
            if (priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter price!");
                return;
            }
            
            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be greater than 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid price!");
                return;
            }
            
            // Check if room already exists
            for (Room room : rooms) {
                if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room number already exists!");
                    return;
                }
            }
            
            
            Room room = new Room(roomNumber, roomType, price, capacity, hasBalcony, description);
            room.setAvailable(isAvailable);
            rooms.add(room);
            
            // Save to file
            DataStorage.saveRoom(room);
            
            // Update table and status
            loadTableData();
            statusLabel.setText("Total Rooms: " + rooms.size());
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Room added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to update!");
            return;
        }
        
        try {
            String roomNumber = roomNumberField.getText().trim();
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String priceText = priceField.getText().trim();
            int capacity = (int) capacitySpinner.getValue();
            boolean hasBalcony = balconyCheck.isSelected();
            boolean isAvailable = availableCheck.isSelected();
            String description = descriptionArea.getText().trim();
            
            // Validation
            if (priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter price!");
                return;
            }
            
            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be greater than 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid price!");
                return;
            }
            
            // Find and update room
            boolean found = false;
            for (Room room : rooms) {
                if (room.getRoomNumber().equals(roomNumber)) {
                    room.setRoomType(roomType);
                    room.setPricePerNight(price);
                    room.setCapacity(capacity);
                    room.setHasBalcony(hasBalcony);
                    room.setAvailable(isAvailable);
                    room.setDescription(description);
                    
                    // Save to file
                    DataStorage.saveRoom(room);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(this, "Room not found!");
                return;
            }
            
            // Update table
            loadTableData();
            
            JOptionPane.showMessageDialog(this, "Room updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete!");
            return;
        }
        
        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete room " + roomNumber + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DataStorage.deleteRoom(roomNumber)) {
                // Update local list
                rooms = DataStorage.loadData("rooms.ser");
                
                // Update table and status
                loadTableData();
                statusLabel.setText("Total Rooms: " + rooms.size());
                clearForm();
                
                JOptionPane.showMessageDialog(this, "Room deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting room!");
            }
        }
    }
    
    private void loadSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow >= 0) {
            String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
            for (Room room : rooms) {
                if (room.getRoomNumber().equals(roomNumber)) {
                    roomNumberField.setText(room.getRoomNumber());
                    roomTypeCombo.setSelectedItem(room.getRoomType().toString());
                    priceField.setText(String.format("%.2f", room.getPricePerNight()));
                    capacitySpinner.setValue(room.getCapacity());
                    balconyCheck.setSelected(room.isHasBalcony());
                    availableCheck.setSelected(room.isAvailable());
                    descriptionArea.setText(room.getDescription());
                    break;
                }
            }
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                room.getRoomNumber(),
                room.getRoomType(),
                String.format("$%.2f", room.getPricePerNight()),
                room.getCapacity(),
                room.isHasBalcony(),
                room.isAvailable(),
                room.getDescription()
            });
        }
    }
    
    private void searchRooms(String searchText) {
        tableModel.setRowCount(0);
        String searchLower = searchText.toLowerCase();
        
        for (Room room : rooms) {
            if (room.getRoomNumber().toLowerCase().contains(searchLower) ||
                room.getRoomType().toString().toLowerCase().contains(searchLower) ||
                room.getDescription().toLowerCase().contains(searchLower)) {
                tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getRoomType(),
                    String.format("$%.2f", room.getPricePerNight()),
                    room.getCapacity(),
                    room.isHasBalcony(),
                    room.isAvailable(),
                    room.getDescription()
                });
            }
        }
    }
    
    private void clearForm() {
        roomNumberField.setText("");
        roomTypeCombo.setSelectedIndex(0);
        priceField.setText("100.00");
        capacitySpinner.setValue(2);
        balconyCheck.setSelected(false);
        availableCheck.setSelected(true);
        descriptionArea.setText("");
        roomTable.clearSelection();
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
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
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            RoomForm frame = new RoomForm();
            frame.setVisible(true);
        });
    }
}