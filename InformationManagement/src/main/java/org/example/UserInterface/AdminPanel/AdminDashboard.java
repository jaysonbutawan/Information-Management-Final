package org.example.UserInterface.AdminPanel;
import org.example.DAO.PropertyDAO;
import org.example.Object.PropertyDetails;
import org.example.UserInterface.PropertiesFiles.AddProperty_Dialog.AddPropertyDialog;
import org.example.UserInterface.PropertiesFiles.UpdateProperty_Dialog.UpdatePropertyDialog;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;


public class
AdminDashboard extends JFrame {
    private JTable propertiesTable;
    private static DefaultTableModel propertiesModel;
    private DefaultTableModel rentersModel;
    private int totalProperties = 25;
    private final Frame parent = new Frame();
    private static JPanel statsPanel = new JPanel();

    public AdminDashboard() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header Panel
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Content Panel with TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Properties", createPropertiesPanel());
        tabbedPane.addTab("Renters", createRentersPanel());
        tabbedPane.addTab("Top Payers", createTopPayersPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Side Panel with action buttons
        mainPanel.add(createSidePanel(), BorderLayout.EAST);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Title
        JLabel titleLabel = new JLabel("Landlord Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(250, 30));
        JButton searchButton = new JButton("Search");
        styleButton(searchButton, new Color(46, 204, 113));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Stats Panel
        statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        statsPanel.setOpaque(false);

        updateStatsPanel(); //update ni diri

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(statsPanel, BorderLayout.CENTER);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        return headerPanel;
    }

    public static void updateStatsPanel() {
        statsPanel.removeAll(); // Clear existing stats

        statsPanel.add(createStatBox("Total Properties", String.valueOf(PropertyDAO.properties_total_count()), Color.decode("#3498db")));
        statsPanel.add(createStatBox("Available", String.valueOf(PropertyDAO.properties_total_count()), Color.decode("#2ecc71"))); // Replace if needed
        int occupiedProperties = 20; // Replace with actual query if needed
        statsPanel.add(createStatBox("Occupied", String.valueOf(occupiedProperties), Color.decode("#e74c3c")));

        statsPanel.revalidate();
        statsPanel.repaint();
    }


    private static JPanel createStatBox(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 15, 5, 15));
        panel.setBackground(color);
        panel.setOpaque(true);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valueLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPropertiesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create properties table
        String[] columns = {"ID", "Property Name", "Address", "Type", "Rent Amount", "Description", "Status"};
        propertiesModel = new DefaultTableModel(columns, 0);
        propertiesTable = new JTable(propertiesModel);
        propertiesTable.setRowHeight(40);
        refreshPropertiesTable();

        JScrollPane scrollPane = new JScrollPane(propertiesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    public static void refreshPropertiesTable() {
        propertiesModel.setRowCount(0); // Clear existing rows

        List<PropertyDetails> propertyList = PropertyDAO.fetchAll();
        for (PropertyDetails pd : propertyList) {
            Object[] row = {
                    pd.getId(),
                    pd.getName(),
                    pd.getAddress(),
                    pd.getTypeName(),
                    pd.getRent(),
                    pd.getDescription(),
                    pd.getStatus()
            };
            propertiesModel.addRow(row);
        }

        propertiesModel.fireTableDataChanged(); // Let JTable know data changed
    }


    private JPanel createRentersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create renters table
        String[] columns = {"ID", "Renter Name", "Property", "Lease Start", "Lease End", "Status", "Action"};
        rentersModel = new DefaultTableModel(columns, 0);

        // Sample data
        Object[] row1 = {"1", "John Doe", "Sunrise Apartments", "2025-01-01", "2025-12-31", "Pending", "Approve/Reject"};
        Object[] row2 = {"2", "Jane Smith", "Downtown Office", "2024-06-01", "2025-05-31", "Active", "View"};
        rentersModel.addRow(row1);
        rentersModel.addRow(row2);

        JTable rentersTable = new JTable(rentersModel);
        rentersTable.setRowHeight(40);
        rentersTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        rentersTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), "Approve/Reject"));

        JScrollPane scrollPane = new JScrollPane(rentersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTopPayersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Create top payers table
        String[] columns = {"Rank", "Renter Name", "Property", "Total Paid", "Consistency"};
        DefaultTableModel topPayersModel = new DefaultTableModel(columns, 0);

        // Sample data
        Object[] row1 = {"1", "John Doe", "Sunrise Apartments", "$14,400", "100%"};
        Object[] row2 = {"2", "Jane Smith", "Downtown Office", "$30,000", "100%"};
        topPayersModel.addRow(row1);
        topPayersModel.addRow(row2);

        JTable topPayersTable = new JTable(topPayersModel);
        topPayersTable.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(topPayersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(200, 0));

        // Add Property Button
        JButton addButton = createIconButton("Add Property", "path/to/add_icon.png");
        addButton.addActionListener(e -> new AddPropertyDialog(parent));

        // Update Property Button
        JButton updateButton = createIconButton("Update", "path/to/update_icon.png");
        updateButton.addActionListener(e -> {
            int selectedRow = propertiesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a property to update.");
                return;
            }
            int modelRow = propertiesTable.convertRowIndexToModel(selectedRow);
            org.example.Object.PropertyDetails details = null;
            if (modelRow >= 0) {
                details = PropertyDAO.fetchAll().get(modelRow);
            }
            if (details != null) {
                new UpdatePropertyDialog(parent, details);
            }
        });

        JButton removeButton = createIconButton("Remove", "path/to/remove_icon.png");
        removeButton.addActionListener(e -> removeSelectedProperty());

        JButton property_type = createIconButton("Property Type", "path/to/remove_icon.png");
        property_type.addActionListener(e -> PropertyTypeDialog.showPropertyTypeManagement(parent));

        panel.add(addButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(updateButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(removeButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(property_type);

        return panel;
    }

    private JButton createIconButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)));

        // Set icon if path is provided
        if (iconPath != null && !iconPath.isEmpty()) {
            button.setIcon(new ImageIcon(iconPath));
        }

        return button;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void removeSelectedProperty() {
        int selectedRow = propertiesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a property to remove.");
            return;
        }
        int modelRow = propertiesTable.convertRowIndexToModel(selectedRow);
        int propertyId = (int) propertiesModel.getValueAt(modelRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this property?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = PropertyDAO.deleteProperty(propertyId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Property deleted successfully!");
                refreshPropertiesTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete property.");
            }
        }
    }

    // Button Renderer and Editor for table actions
    private static class ButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                return (JButton) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final String label;
        private final JButton button;

        public ButtonEditor(JCheckBox checkBox, String label) {
            super(checkBox);
            this.label = label;
            button = new JButton(label);
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (label.equals("Approve/Reject")) {
                handleRenterAction(row);
            } else if (label.equals("Rent")) {
                handleRentAction(row);
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        private void handleRenterAction(int row) {
            Object[] options = {"Approve", "Reject", "Cancel"};
            int choice = JOptionPane.showOptionDialog(
                    AdminDashboard.this,
                    "Process renter request:",
                    "Renter Approval",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) { // Approve
                rentersModel.setValueAt("Approved", row, 5);
                JOptionPane.showMessageDialog(AdminDashboard.this, "Renter approved successfully!");
            } else if (choice == 1) { // Reject
                rentersModel.setValueAt("Rejected", row, 5);
                JOptionPane.showMessageDialog(AdminDashboard.this, "Renter rejected.");
            }
        }

        private void handleRentAction(int row) {
            String property = (String) propertiesModel.getValueAt(row, 1);
            JOptionPane.showMessageDialog(
                    AdminDashboard.this,
                    "Processing rental for: " + property,
                    "Rent Property",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}