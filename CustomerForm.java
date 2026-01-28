import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerForm extends JFrame {
    private List<Customer> customers;
    private DefaultTableModel tableModel;
    private JTable customerTable;
    private JFrame parentFrame;
    
    // Form components
    private JTextField customerIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JComboBox<IdType> idTypeCombo;
    private JTextField idNumberField;
    
    public CustomerForm() {
        this(null);
    }
    
    public CustomerForm(JFrame parent) {
        this.parentFrame = parent;
        setTitle("Customer Management");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Add back button
        Main.addBackButton(this, parent);
        
        customers = DataStorage.loadData("customers.ser");
        if (customers == null) customers = new ArrayList<>();
        
        initComponents();
        loadTableData();
    }
    
    private void initComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left panel for form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        formPanel.setPreferredSize(new Dimension(400, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer ID:*"), gbc);
        customerIdField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(customerIdField, gbc);
        
        // First Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("First Name:*"), gbc);
        firstNameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Last Name:*"), gbc);
        lastNameField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:*"), gbc);
        emailField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Phone:*"), gbc);
        phoneField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Address:*"), gbc);
        addressArea = new JTextArea(3, 15);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(addressScroll, gbc);
        
        // ID Type (ENUM)
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel("ID Type:"), gbc);
        idTypeCombo = new JComboBox<>(IdType.values());
        gbc.gridx = 1;
        formPanel.add(idTypeCombo, gbc);
        
        // ID Number
        gbc.gridx = 0; gbc.gridy = 7;
        formPanel.add(new JLabel("ID Number:"), gbc);
        idNumberField = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(idNumberField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton addButton = createStyledButton("Add", new Color(70, 130, 180));
        JButton updateButton = createStyledButton("Update", new Color(60, 179, 113));
        JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60));
        JButton clearButton = createStyledButton("Clear", new Color(128, 128, 128));
        
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(buttonPanel, gbc);
        
        // Right panel for table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Customers List"));
        
        // Create table
        String[] columns = {"Customer ID", "Name", "Email", "Phone", "Address", "ID Type", "ID Number", "Registered"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        customerTable = new JTable(tableModel);
        TableHelper.setupTable(customerTable);
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                loadSelectedCustomer();
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(customerTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        
        searchButton.addActionListener(e -> searchCustomers(searchField.getText()));
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
        JLabel statusLabel = new JLabel("Total Customers: " + customers.size());
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addCustomer() {
        try {
            String customerId = customerIdField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();
            IdType idType = (IdType) idTypeCombo.getSelectedItem();
            String idNumber = idNumberField.getText().trim();
            
            if (customerId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
                email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields (*)!");
                return;
            }
            
            // Check if customer already exists
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(customerId)) {
                    JOptionPane.showMessageDialog(this, "Customer ID already exists!");
                    return;
                }
            }
            
            Customer customer = new Customer(customerId, firstName, lastName, email, phone, 
                                            address, idType, idNumber);
            
            // Save to file
            DataStorage.saveCustomer(customer);
            
            // Reload data
            customers = DataStorage.loadData("customers.ser");
            loadTableData();
            clearForm();
            
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update!");
            return;
        }
        
        try {
            String customerId = customerIdField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressArea.getText().trim();
            IdType idType = (IdType) idTypeCombo.getSelectedItem();
            String idNumber = idNumberField.getText().trim();
            
            // Find and update customer
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(customerId)) {
                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    customer.setEmail(email);
                    customer.setPhone(phone);
                    customer.setAddress(address);
                    customer.setIdType(idType);
                    customer.setIdNumber(idNumber);
                    
                    DataStorage.saveCustomer(customer);
                    break;
                }
            }
            
            customers = DataStorage.loadData("customers.ser");
            loadTableData();
            
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete!");
            return;
        }
        
        String customerId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete customer " + customerId + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DataStorage.deleteCustomer(customerId)) {
                customers = DataStorage.loadData("customers.ser");
                loadTableData();
                clearForm();
                JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting customer!");
            }
        }
    }
    
    private void loadSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            String customerId = (String) tableModel.getValueAt(selectedRow, 0);
            for (Customer customer : customers) {
                if (customer.getCustomerId().equals(customerId)) {
                    customerIdField.setText(customer.getCustomerId());
                    firstNameField.setText(customer.getFirstName());
                    lastNameField.setText(customer.getLastName());
                    emailField.setText(customer.getEmail());
                    phoneField.setText(customer.getPhone());
                    addressArea.setText(customer.getAddress());
                    idTypeCombo.setSelectedItem(customer.getIdType());
                    idNumberField.setText(customer.getIdNumber());
                    break;
                }
            }
        }
    }
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getIdType(),
                customer.getIdNumber(),
                sdf.format(customer.getRegistrationDate())
            });
        }
    }
    
    private void searchCustomers(String searchText) {
        tableModel.setRowCount(0);
        String searchLower = searchText.toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Customer customer : customers) {
            if (customer.getCustomerId().toLowerCase().contains(searchLower) ||
                customer.getFirstName().toLowerCase().contains(searchLower) ||
                customer.getLastName().toLowerCase().contains(searchLower) ||
                customer.getEmail().toLowerCase().contains(searchLower) ||
                customer.getPhone().contains(searchText)) {
                tableModel.addRow(new Object[]{
                    customer.getCustomerId(),
                    customer.getFirstName() + " " + customer.getLastName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress(),
                    customer.getIdType(),
                    customer.getIdNumber(),
                    sdf.format(customer.getRegistrationDate())
                });
            }
        }
    }
    
    private void clearForm() {
        customerIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        idTypeCombo.setSelectedIndex(0);
        idNumberField.setText("");
        customerTable.clearSelection();
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
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
}