package org.example.UserInterface.PropertiesFiles.AddProperty_Dialog;

import org.example.DAO.PropertyDAO;
import org.example.Database.DatabaseConnection;
import org.example.Object.Property;
import org.example.Object.Property_Type;
import org.example.UserInterface.AdminPanel.AdminDashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.DAO.PropertyDAO.fetchAll;
import static org.example.DAO.PropertyDAO.properties_total_count;

public class AddPropertyDialog extends JDialog {
    private final JTextField propertyNameField = new JTextField();
    private final JTextField addressField = new JTextField();
    private final JTextField priceField = new JTextField();
    private final JTextField sizeField = new JTextField();
    private final JComboBox<Property_Type> typeComboBox = new JComboBox<>();
    private final JTextField description = new JTextField();
    private final DefaultTableModel propertyTableModel = new DefaultTableModel();

    // Color scheme
    private final Color primaryColor = new Color(52, 152, 219); // Blue
    private final Color accentColor = new Color(46, 204, 113); // Green
    private final Color darkText = new Color(52, 73, 94);
    private final Color lightBg = new Color(245, 245, 245);

    public AddPropertyDialog(Frame parent) {
        super(parent, "Add Property", true);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 650, 20, 20)); // Adjusted dimensions
        setSize(500, 650); // More appropriate size
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(lightBg);

        // Header panel with close button
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel); // Added scroll pane
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }


    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        System.out.println("heyyy");
        JLabel titleLabel = new JLabel("Add New Property", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(primaryColor);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setForeground(Color.GRAY);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> dispose());

        headerPanel.add(closeButton, BorderLayout.EAST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(primaryColor, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10); // top, left, bottom, right padding

        // Add fields with proper constraints
        formPanel.add(createModernField("Property Name", propertyNameField), gbc);
        gbc.gridy++;
        formPanel.add(createModernField("Address", addressField), gbc);
        gbc.gridy++;
        formPanel.add(createModernField("Price ($)", priceField), gbc);
        gbc.gridy++;
        formPanel.add(createModernField("Size (sq ft)", sizeField), gbc);
        gbc.gridy++;
        formPanel.add(createModernComboBox("Property Type"), gbc);
        gbc.gridy++;
        formPanel.add(createModernField("Description", description), gbc);

        // Add flexible space at bottom to push fields up
        gbc.gridy++;
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        return formPanel;
    }


    private JPanel createModernField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        JLabel title = new JLabel(label);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(darkText);

        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 15, 10, 15)));
        field.setBackground(new Color(250, 250, 250));
        field.setPreferredSize(new Dimension(0, 35));
        field.setMinimumSize(new Dimension(100, 35));
        panel.add(title, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createModernComboBox(String label) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        JLabel title = new JLabel(label);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(darkText);

        this.typeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        this.typeComboBox.setBackground(new Color(250, 250, 250));
        this.typeComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 15, 5, 15)));
        this.typeComboBox.setRenderer(new ModernComboBoxRenderer());
        String query = "SELECT id, type_name, description FROM property_type";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String typeName = resultSet.getString("type_name");
                String desc = resultSet.getString("description");

                Property_Type pt = new Property_Type(id, typeName, desc);
                this.typeComboBox.addItem(pt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
//            comboBox.addItem();
        }
        panel.add(title, BorderLayout.NORTH);
        panel.add(this.typeComboBox, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(231, 76, 60)); // Red
        cancelButton.addActionListener(e -> dispose());

        JButton addButton = new JButton("Add Property");
        styleButton(addButton, accentColor); // Green
        addButton.addActionListener(createAddButtonListener());

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1),
                new EmptyBorder(10, 25, 10, 25)));
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

    private static class ModernComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            setBorder(new EmptyBorder(5, 15, 5, 15));
            if (isSelected) {
                setBackground(new Color(52, 152, 219));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(new Color(52, 73, 94));
            }
            return this;
        }
    }


    private ActionListener createAddButtonListener() {
        return e -> {
            if (!validateInput()) return;

            String property_name = propertyNameField.getText().trim();
            String address = addressField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            float size = Float.parseFloat(sizeField.getText().trim());
            Property_Type selectedType = (Property_Type) typeComboBox.getSelectedItem();

            if (selectedType == null) {
                JOptionPane.showMessageDialog(null, "Please select a property type.");
                return;
            }

            int type = selectedType.getType_id();
            String details = description.getText().trim();

            // Check if property already exists
            if (PropertyDAO.propertyExists(property_name)) {
                JOptionPane.showMessageDialog(null, "Property already exists!");
                return;
            }

            // Insert property
            Property property = new Property(property_name, address, size, price, type, details);
            boolean success = PropertyDAO.insertProperty(property);

            if (success) {
                JOptionPane.showMessageDialog(null, "Property added successfully!");
                AdminDashboard.updateStatsPanel();
                AdminDashboard.refreshPropertiesTable();
                // Clear fields
                propertyNameField.setText("");
                addressField.setText("");
                priceField.setText("");
                sizeField.setText("");
                description.setText("");
                typeComboBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add property.");
            }
        };
    }


    private boolean validateInput() {
        InputValidator validator = new InputValidator();
        boolean valid = true;
        if(!validator.validateNonEmpty(propertyNameField, "Please fill the property name ")) valid = false;
        if (!validator.validateNonEmpty(addressField, "Address cannot be empty.")) valid = false;
        if (!validator.validatePositiveDouble(priceField, "Price must be greater than 0.")) valid = false;
        if (!validator.validatePositiveDouble(sizeField, "Size must be greater than 0.")) valid = false;

//        String type = (String) typeComboBox.getSelectedItem();
        return valid;
    }
}
