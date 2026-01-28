import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DatePicker {
    private JPanel panel;
    private JComboBox<Integer> dateCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private Date selectedDate;
    
    public DatePicker() {
        initComponents();
    }
    
    private void initComponents() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Date combo (1-31)
        dateCombo = new JComboBox<>();
        dateCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 1; i <= 31; i++) {
            dateCombo.addItem(i);
        }
        dateCombo.setSelectedItem(currentDay);
        dateCombo.setPreferredSize(new Dimension(60, 30));
        dateCombo.setBackground(Color.WHITE);
        
        // Month combo with names
        monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        monthCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        monthCombo.setSelectedIndex(currentMonth);
        monthCombo.setPreferredSize(new Dimension(100, 30));
        monthCombo.setBackground(Color.WHITE);
        
        // Year combo (2020-2030)
        yearCombo = new JComboBox<>();
        yearCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 2020; i <= 2030; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(currentYear);
        yearCombo.setPreferredSize(new Dimension(80, 30));
        yearCombo.setBackground(Color.WHITE);
        
        // Add components with labels
        JLabel dateLabel = new JLabel("Day:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateLabel.setForeground(Color.DARK_GRAY);
        panel.add(dateLabel);
        panel.add(dateCombo);
        
        panel.add(Box.createHorizontalStrut(5));
        
        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Arial", Font.BOLD, 12));
        monthLabel.setForeground(Color.DARK_GRAY);
        panel.add(monthLabel);
        panel.add(monthCombo);
        
        panel.add(Box.createHorizontalStrut(5));
        
        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Arial", Font.BOLD, 12));
        yearLabel.setForeground(Color.DARK_GRAY);
        panel.add(yearLabel);
        panel.add(yearCombo);
        
        // Add a today button
        JButton todayBtn = new JButton("Today");
        todayBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        todayBtn.setMargin(new Insets(2, 8, 2, 8));
        todayBtn.setToolTipText("Set to Today's Date");
        todayBtn.addActionListener(e -> setToToday());
        panel.add(Box.createHorizontalStrut(10));
        panel.add(todayBtn);
        
        // Update selected date when combos change
        dateCombo.addActionListener(e -> updateDate());
        monthCombo.addActionListener(e -> {
            updateDate();
            adjustDaysForMonth();
        });
        yearCombo.addActionListener(e -> {
            updateDate();
            adjustDaysForMonth();
        });
        
        // Initial adjustments
        adjustDaysForMonth();
        updateDate();
    }
    
    private void adjustDaysForMonth() {
        try {
            int month = monthCombo.getSelectedIndex() + 1;
            int year = (Integer) yearCombo.getSelectedItem();
            
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1);
            int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            // Store current selection
            Integer currentDay = (Integer) dateCombo.getSelectedItem();
            
            // Remove extra items
            while (dateCombo.getItemCount() > maxDays) {
                dateCombo.removeItemAt(dateCombo.getItemCount() - 1);
            }
            
            // Add missing items
            while (dateCombo.getItemCount() < maxDays) {
                dateCombo.addItem(dateCombo.getItemCount() + 1);
            }
            
            // Restore selection if valid
            if (currentDay != null && currentDay <= maxDays) {
                dateCombo.setSelectedItem(currentDay);
            } else if (currentDay != null && currentDay > maxDays) {
                dateCombo.setSelectedItem(maxDays);
            }
        } catch (Exception e) {
            // Ignore errors during initialization
        }
    }
    
    private void updateDate() {
        try {
            int day = (Integer) dateCombo.getSelectedItem();
            int month = monthCombo.getSelectedIndex();
            int year = (Integer) yearCombo.getSelectedItem();
            
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            
            // Validate date (e.g., February 30)
            if (calendar.get(Calendar.DAY_OF_MONTH) != day) {
                // Adjust to last day of month
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateCombo.setSelectedItem(calendar.get(Calendar.DAY_OF_MONTH));
            }
            
            selectedDate = calendar.getTime();
        } catch (Exception e) {
            // If any error occurs, set to today's date
            setToToday();
        }
    }
    
    private void setToToday() {
        Calendar calendar = Calendar.getInstance();
        dateCombo.setSelectedItem(calendar.get(Calendar.DAY_OF_MONTH));
        monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        yearCombo.setSelectedItem(calendar.get(Calendar.YEAR));
        selectedDate = calendar.getTime();
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public Date getSelectedDate() {
        return selectedDate;
    }
    
    public void setDate(Date date) {
        if (date == null) return;
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        dateCombo.setSelectedItem(calendar.get(Calendar.DAY_OF_MONTH));
        monthCombo.setSelectedIndex(calendar.get(Calendar.MONTH));
        yearCombo.setSelectedItem(calendar.get(Calendar.YEAR));
        
        selectedDate = date;
    }
    
    public String getFormattedDate() {
        if (selectedDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        return sdf.format(selectedDate);
    }
    
    public String getDatabaseFormattedDate() {
        if (selectedDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(selectedDate);
    }
}