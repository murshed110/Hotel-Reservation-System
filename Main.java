import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Main {
    private static JFrame mainFrame;

    public static void main(String[] args) {
        
        java.io.File dataDir = new java.io.File("data");
        if (!dataDir.exists()) dataDir.mkdir();

        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("🏨 Hotel Reservation System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1100, 700);
        mainFrame.setLocationRelativeTo(null);

        // MENU BAR ---
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.BLACK);
        fileMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JMenuItem viewAllDataItem = new JMenuItem("📊 View All Data");
        JMenuItem backupItem = new JMenuItem("💾 Backup Data");
        JMenuItem exitItem = new JMenuItem("🚪 Exit");

        viewAllDataItem.addActionListener(e -> showAllData());
        backupItem.addActionListener(e -> backupData());
        exitItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(mainFrame,
                "Are you sure you want to exit?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        fileMenu.add(viewAllDataItem);
        fileMenu.add(backupItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.BLACK);
        helpMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JMenuItem aboutItem = new JMenuItem("ℹ️ About");
        JMenuItem helpItem = new JMenuItem("❓ Help");

        aboutItem.addActionListener(e -> showAboutDialog());
        helpItem.addActionListener(e -> showHelpDialog());

        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Time label on menu bar (right)
        JLabel timeLabel = new JLabel();
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(timeLabel);

        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a | EEE, MMM dd, yyyy");
            timeLabel.setText(sdf.format(new Date()));
        });
        timer.start();

        mainFrame.setJMenuBar(menuBar);

        // --- MAIN PANEL ---
        Color ralBase = new Color(128, 100, 63); // RAL 1036 approximation
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color light = ralBase.brighter();
                GradientPaint gp = new GradientPaint(0, 0, light, 0, getHeight(), ralBase);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel logoLabel = new JLabel("🏨", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));

        JLabel titleLabel = new JLabel("HOTEL RESERVATION SYSTEM", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(25, 25, 112));

        JLabel subtitleLabel = new JLabel("Professional Hotel Management Solution", SwingConstants.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(70, 130, 180));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        // --- LEFT SIDEBAR ---
        JPanel leftSidebar = new JPanel();
        leftSidebar.setLayout(new BoxLayout(leftSidebar, BoxLayout.Y_AXIS));
        leftSidebar.setOpaque(false);
        leftSidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        leftSidebar.setPreferredSize(new Dimension(260, 0));

        JLabel sidebarTitle = new JLabel("Quick Actions");
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
        leftSidebar.add(sidebarTitle);

        JButton manageRoomsBtn = createSidebarButton("🛏️ Manage Rooms", new Color(70, 130, 180));
        manageRoomsBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new RoomForm(mainFrame).setVisible(true);
        });

        JButton manageCustomersBtn = createSidebarButton("👥 Manage Customers", new Color(60, 179, 113));
        manageCustomersBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new CustomerForm(mainFrame).setVisible(true);
        });

        JButton manageReservationsBtn = createSidebarButton("📅 Manage Reservations", new Color(218, 165, 32));
        manageReservationsBtn.addActionListener(e -> {
            mainFrame.setVisible(false);
            new ReservationForm(mainFrame).setVisible(true);
        });

        JButton viewAllDataBtn = createSidebarButton("📊 View All Data", new Color(155, 89, 182));
        viewAllDataBtn.addActionListener(e -> showAllData());

        leftSidebar.add(Box.createVerticalStrut(8));
        leftSidebar.add(manageRoomsBtn);
        leftSidebar.add(Box.createVerticalStrut(10));
        leftSidebar.add(manageCustomersBtn);
        leftSidebar.add(Box.createVerticalStrut(10));
        leftSidebar.add(manageReservationsBtn);
        leftSidebar.add(Box.createVerticalStrut(10));
        leftSidebar.add(viewAllDataBtn);

        // --- STATS PANEL ---
        JPanel statsPanel = createStatsDashboard();

        // --- RECENT ACTIVITY PANEL ---
        JPanel recentActivityPanel = createRecentActivityPanel();

        // --- FOOTER PANEL ---
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel footerLabel = new JLabel(
            "© 2024 Hotel Reservation System v5.0 | Professional Edition | All Rights Reserved",
            SwingConstants.CENTER
        );
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // --- ASSEMBLE MAIN PANEL ---
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftSidebar, BorderLayout.WEST);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(recentActivityPanel, BorderLayout.SOUTH);
        mainPanel.add(footerPanel, BorderLayout.PAGE_END);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static JButton createSidebarButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent evt) { button.setBackground(color); }
        });
        return button;
    }

    // --- BACK BUTTON FOR FORMS ---
    public static void addBackButton(JFrame form, JFrame parent) {
        if (parent == null) return;

        JMenuBar formMenuBar = new JMenuBar();
        formMenuBar.setBackground(new Color(25, 25, 112));

        JButton backButton = new JButton("← Back to Main");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(70, 130, 180));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            form.dispose();
            parent.setVisible(true);
        });

        JLabel titleLabel = new JLabel(form.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        formMenuBar.add(backButton);
        formMenuBar.add(Box.createHorizontalStrut(20));
        formMenuBar.add(titleLabel);
        formMenuBar.add(Box.createHorizontalGlue());

        form.setJMenuBar(formMenuBar);
    }

    // --- STATS DASHBOARD ---
    private static JPanel createStatsDashboard() {
        JPanel panel = new JPanel(new java.awt.GridLayout(2, 2, 20, 20));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        List<Room> rooms = DataStorage.loadData("rooms.ser");
        List<Customer> customers = DataStorage.loadData("customers.ser");
        List<Reservation> reservations = DataStorage.loadData("reservations.ser");

        int roomCount = rooms != null ? rooms.size() : 0;
        int customerCount = customers != null ? customers.size() : 0;
        int reservationCount = reservations != null ? reservations.size() : 0;
        int availableRooms = 0;
        int todayReservations = 0;

        if (rooms != null) for (Room r : rooms) if (r.isAvailable()) availableRooms++;
        if (reservations != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            for (Reservation res : reservations)
                if (sdf.format(res.getCheckInDate()).equals(today)) todayReservations++;
        }

        panel.add(createStatCard("🛏️", "Total Rooms", String.valueOf(roomCount),
                "Available: " + availableRooms, new Color(70, 130, 180)));

        panel.add(createStatCard("👥", "Total Customers", String.valueOf(customerCount),
                "Registered Users", new Color(60, 179, 113)));

        panel.add(createStatCard("📅", "Total Reservations", String.valueOf(reservationCount),
                "Today: " + todayReservations, new Color(218, 165, 32)));

        panel.add(createStatCard("💰", "Revenue", calculateTotalRevenue(reservations),
                "Total Income", new Color(155, 89, 182)));

        return panel;
    }

    private static String calculateTotalRevenue(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) return "$0.00";
        double total = 0;
        for (Reservation res : reservations)
            if (res.getStatus() != ReservationStatus.CANCELLED) total += res.getTotalAmount();
        return String.format("$%.2f", total);
    }

    private static JPanel createStatCard(String icon, String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.BLACK);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setOpaque(false);
        valuePanel.add(valueLabel, BorderLayout.CENTER);
        valuePanel.add(subtitleLabel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(valuePanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { card.setBackground(new Color(250, 250, 255)); }
            public void mouseExited(MouseEvent evt) { card.setBackground(Color.WHITE); }
        });

        return card;
    }

    // --- RECENT ACTIVITY PANEL ---
    private static JPanel createRecentActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Recent Activity"),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        List<Reservation> reservations = DataStorage.loadData("reservations.ser");

        String[] columns = {"Reservation ID", "Customer", "Room", "Check-in", "Check-out", "Status", "Amount"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        if (reservations != null && !reservations.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int count = Math.min(reservations.size(), 5);
            List<Reservation> sorted = new java.util.ArrayList<>(reservations);
            sorted.sort((r1, r2) -> r2.getBookingDate().compareTo(r1.getBookingDate()));

            for (int i = 0; i < count; i++) {
                Reservation r = sorted.get(i);
                model.addRow(new Object[]{
                        r.getReservationId(),
                        r.getCustomer().getFullName(),
                        r.getRoom().getRoomNumber(),
                        sdf.format(r.getCheckInDate()),
                        sdf.format(r.getCheckOutDate()),
                        r.getStatus(),
                        String.format("$%.2f", r.getTotalAmount())
                });
            }
        } else {
            model.addRow(new Object[]{"No recent activity", "", "", "", "", "", ""});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 150));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- ABOUT / HELP / BACKUP ---
    private static void showAboutDialog() {
        String aboutText = """
            <html>
            <div style='font-family: Segoe UI;'>
            <h2 style='color: #191970;'>🏨 Hotel Reservation System</h2>
            <hr>
            <p><b>Features:</b></p>
            <ul>
                <li>✓ Three Entity Classes with Serializable Interface</li>
                <li>✓ Entity Relationship Diagram Implementation</li>
                <li>✓ Three Separate Sequential Access Files</li>
                <li>✓ Enum Classes for Option Fields</li>
                <li>✓ Date Class with Professional Date Picker</li>
                <li>✓ Complete CRUD Operations</li>
                <li>✓ Data Persistence with Serialization</li>
                <li>✓ Professional GUI with Modern Design</li>
                <li>✓ Real-time Statistics Dashboard</li>
                <li>✓ Search and Filter Functionality</li>
            </ul>
            <hr>
            <p><b>Developed for:</b> Object-Oriented Programming Course</p>
            <p><b>Version:</b> 5.0 (Professional Edition)</p>
            <p><b>© 2024 All Rights Reserved</b></p>
            </div>
            </html>
            """;
        JOptionPane.showMessageDialog(mainFrame, aboutText, "About System",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showHelpDialog() {
        String helpText = """
            <html>
            <div style='font-family: Segoe UI; max-width: 500px;'>
            <h2 style='color: #191970;'>📖 User Guide</h2>
            <hr>
            <h3>Getting Started:</h3>
            <ol>
                <li>Use the <b>Quick Access</b> buttons or <b>Manage</b> menu</li>
                <li>Add Rooms, Customers, and Reservations</li>
                <li>All data is automatically saved</li>
            </ol>

            <h3>Features:</h3>
            <ul>
                <li><b>Manage Rooms:</b> Add/Edit/Delete Rooms</li>
                <li><b>Manage Customers:</b> Add/Edit/Delete Customers</li>
                <li><b>Manage Reservations:</b> Book/Edit/Cancel</li>
                <li><b>Statistics:</b> Dashboard shows total counts and revenue</li>
                <li><b>Recent Activity:</b> Shows last 5 bookings</li>
            </ul>
            <hr>
            <p>For detailed documentation, contact the developer.</p>
            </div>
            </html>
            """;
        JOptionPane.showMessageDialog(mainFrame, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void backupData() {
        JOptionPane.showMessageDialog(mainFrame, "Backup functionality is not yet implemented.", "Backup",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // --- FIXED VIEW ALL DATA ---
    private static void showAllData() {
        JFrame dataFrame = new JFrame("📊 All Data");
        dataFrame.setSize(800, 500);
        dataFrame.setLocationRelativeTo(mainFrame);

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- ROOMS TAB ---
        List<Room> rooms = DataStorage.loadData("rooms.ser");
        String[] roomColumns = {"Room Number", "Type", "Price", "Available"};
        DefaultTableModel roomModel = new DefaultTableModel(roomColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        if (rooms != null) {
            for (Room r : rooms) {
                roomModel.addRow(new Object[]{
                        r.getRoomNumber(),
                        r.getRoomType().toString(),
                        String.format("$%.2f", r.getPricePerNight()),
                        r.isAvailable() ? "Yes" : "No"
                });
            }
        }
        JTable roomTable = new JTable(roomModel);
        roomTable.setRowHeight(25);
        tabbedPane.addTab("Rooms", new JScrollPane(roomTable));

        // --- CUSTOMERS TAB ---
        List<Customer> customers = DataStorage.loadData("customers.ser");
        String[] custColumns = {"Customer ID", "Full Name", "Email", "Phone"};
        DefaultTableModel custModel = new DefaultTableModel(custColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        if (customers != null) {
            for (Customer c : customers) {
                custModel.addRow(new Object[]{
                        c.getCustomerId(),
                        c.getFullName(),
                        c.getEmail(),
                        c.getPhone()
                });
            }
        }
        JTable custTable = new JTable(custModel);
        custTable.setRowHeight(25);
        tabbedPane.addTab("Customers", new JScrollPane(custTable));

        // --- RESERVATIONS TAB ---
        List<Reservation> reservations = DataStorage.loadData("reservations.ser");
        String[] resColumns = {"Reservation ID", "Customer", "Room", "Check-in", "Check-out", "Status", "Amount"};
        DefaultTableModel resModel = new DefaultTableModel(resColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (reservations != null) {
            for (Reservation r : reservations) {
                resModel.addRow(new Object[]{
                        r.getReservationId(),
                        r.getCustomer().getFullName(),
                        r.getRoom().getRoomNumber(),
                        sdf.format(r.getCheckInDate()),
                        sdf.format(r.getCheckOutDate()),
                        r.getStatus(),
                        String.format("$%.2f", r.getTotalAmount())
                });
            }
        }
        JTable resTable = new JTable(resModel);
        resTable.setRowHeight(25);
        tabbedPane.addTab("Reservations", new JScrollPane(resTable));

        dataFrame.add(tabbedPane);
        dataFrame.setVisible(true);
    }
}
