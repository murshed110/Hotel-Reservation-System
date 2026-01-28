import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class TableHelper {
    
    public static void setupTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        if (table.getParent() != null) {
            table.getParent().setBackground(Color.WHITE);
        }
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        
        // Enable sorting
        table.setAutoCreateRowSorter(true);
    }
}