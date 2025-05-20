package org.example.UserInterface.AdminPanel;

import org.example.DAO.Property_TypeDAO;
import org.example.Database.DatabaseConnection;
import org.example.Object.Property_Type;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PropertyTypeDialog extends JDialog {

    private DefaultTableModel propertyTypeModel;
    private JTable propertyTypeTable;
    private Property_TypeDAO propertyTypeDAO = new Property_TypeDAO();

    public PropertyTypeDialog(Frame parent) {
        super(parent, "Property Type Management", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table model
        String[] columns = {"Property Type", "Description", "Date Added"};
        propertyTypeModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        // Create table
        propertyTypeTable = new JTable(propertyTypeModel);
        propertyTypeTable.setRowHeight(30);
        propertyTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        // Add mouse listener for double-click
        propertyTypeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showEditDeleteDialog();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(propertyTypeTable);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Property Type");
        styleButton(addButton, new Color(46, 204, 113)); // Green color

        addButton.addActionListener(e -> showAddPropertyTypeDialog());

        buttonPanel.add(addButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadPropertyTypesFromView();
        add(mainPanel);
        setVisible(true);
    }
    private void loadPropertyTypesFromView() {
        String query = "SELECT type_name, description, created_at FROM viewpropety_type";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            propertyTypeModel.setRowCount(0); // Clear existing rows


            while (rs.next()) {
                String typeName = rs.getString("type_name");
                String description = rs.getString("description");
                String createdAt = rs.getString("created_at");

                propertyTypeModel.addRow(new Object[]{typeName, description, createdAt});


            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load property types.");
        }
    }


    private void showAddPropertyTypeDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField typeField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);

        panel.add(new JLabel("Property Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Description:"));
        panel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add New Property Type",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            int id = 0;
            if (!typeField.getText().trim().isEmpty()) {
                Property_Type type = new Property_Type(id,typeField.getText().trim(),descArea.getText().trim());
                propertyTypeDAO.insertProperty_Type(type);
                loadPropertyTypesFromView();
                JOptionPane.showMessageDialog(this, "Property type added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Property type cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDeleteDialog() {
        int selectedRow = propertyTypeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a property type first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String type = (String) propertyTypeModel.getValueAt(selectedRow, 0);
        String desc = (String) propertyTypeModel.getValueAt(selectedRow, 1);

        Object[] options = {"Update", "Delete", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Manage Property Type:\n\n" +
                        "Type: " + type + "\n" +
                        "Description: " + desc,
                "Property Type Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) { // Update
            updatePropertyType(selectedRow, type, desc);
        } else if (choice == 1) { // Delete
            deletePropertyType(selectedRow, type);
        }
    }

    private void updatePropertyType(int row, String currentType, String currentDesc) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField typeField = new JTextField(currentType);
        JTextArea descArea = new JTextArea(currentDesc, 3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);

        panel.add(new JLabel("Property Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Description:"));
        panel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Property Type",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newType = typeField.getText().trim();
            String newDesc = descArea.getText().trim();

            if (!newType.isEmpty()) {
                try {
                    // ✅ Correct column index for ID
                    int id = Integer.parseInt(propertyTypeModel.getValueAt(row, 0).toString());

                    // Call the DAO to update the DB
                    Property_TypeDAO.updatePropertyType(id, newType, newDesc);

                    // Update UI table
                    propertyTypeModel.setValueAt(newType, row, 1); // type name
                    propertyTypeModel.setValueAt(newDesc, row, 2); // description
                    System.out.println("ID: " + propertyTypeModel.getValueAt(row, 0));
                    System.out.println("Type: " + propertyTypeModel.getValueAt(row, 1));
                    System.out.println("Desc: " + propertyTypeModel.getValueAt(row, 2));

                    JOptionPane.showMessageDialog(this, "Property type updated successfully!");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Property type cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void deletePropertyType(int row, String type) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete '" + type + "'?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // 🔴 Get the ID from column 0
            int id = (int) propertyTypeModel.getValueAt(row, 0);

            // ✅ Call DAO to delete from DB
            Property_TypeDAO.deletePropertyType(id);

            // ✅ Remove from table model (UI)
            propertyTypeModel.removeRow(row);

            JOptionPane.showMessageDialog(this, "Property type deleted successfully!");
        }
    }


    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }

    // Method to show the dialog from your main UI
    public static void showPropertyTypeManagement(Frame parent) {
        PropertyTypeDialog dialog = new PropertyTypeDialog(parent);
        dialog.setVisible(true);
    }
}