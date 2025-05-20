package org.example.UserInterface.PropertiesFiles.UpdateProperty_Dialog;

import org.example.Object.Property;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import org.example.Object.Property_Type;

public class PropertyDetailsPanel extends JPanel {
    private final JTextField propertyname;
    private final JTextField addressField;
    private final JTextField priceField;
    private final JTextField sizeField;
    private final JComboBox<Property_Type> typeComboBox;
    private final JTextField description;


    public PropertyDetailsPanel() {
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(new TitledBorder("Property Details"));
        setBackground(Color.WHITE);

        Property property = new Property();
        propertyname = new JTextField(property.getProperty_name());
        addressField = new JTextField(property.getAddress());
        priceField = new JTextField(String.valueOf(property.getRent_price()));
        sizeField = new JTextField(String.valueOf(property.getSize()));

        // Fetch property types from DB
        java.util.List<Property_Type> typeList = new java.util.ArrayList<>();
        try {
            String sql = "SELECT type_id, type_name, description FROM property_type";
            java.sql.Connection conn = org.example.Database.DatabaseConnection.getConnection();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("type_id");
                String name = rs.getString("type_name");
                String desc = rs.getString("description");
                typeList.add(new Property_Type(id, name, desc));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        typeComboBox = new JComboBox<>(typeList.toArray(new Property_Type[0]));

        description = new JTextField(property.getProperty_name());

        add(new JLabel("Property Name"));
        add(propertyname);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Size:"));
        add(sizeField);
        add(new JLabel("Type:"));
        add(typeComboBox);
        add(new JLabel("Description:"));
        add(description);
    }

    public String getPropertyname() {
        return propertyname.getText().trim();
    }

    public String getAddress() {
        return addressField.getText().trim();
    }

    public float getPrice() {
        return Float.parseFloat(priceField.getText().trim());
    }

    public float getPropertySize() {
        return Float.parseFloat(sizeField.getText().trim());
    }

    public String getType() {
        Property_Type selected = (Property_Type) typeComboBox.getSelectedItem();
        return selected != null ? selected.getType_name() : null;
    }
    public int getTypeId() {
        Property_Type selected = (Property_Type) typeComboBox.getSelectedItem();
        return selected != null ? selected.getType_id() : -1;
    }

    public String getDetails() {
        return description.getText().trim();
    }
    public boolean validateInputs() {
        boolean valid = ValidationUtils.validateNonEmpty(propertyname, "Please fill the property name.");
        if (!ValidationUtils.validateNonEmpty(addressField, "Address cannot be empty.")) valid = false;
        if (!ValidationUtils.validatePositiveNumber(priceField, "Price must be a positive number.")) valid = false;
        if (!ValidationUtils.validatePositiveNumber(sizeField, "Size must be a positive number.")) valid = false;

        return valid;
    }
    public void setPropertyDetails(org.example.Object.PropertyDetails details) {
        propertyname.setText(details.getName());
        addressField.setText(details.getAddress());
        priceField.setText(String.valueOf(details.getRent()));
        sizeField.setText(String.valueOf(details.getSize()));
        // Set selected type by matching type name
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < typeComboBox.getItemCount(); i++) {
                Property_Type pt = typeComboBox.getItemAt(i);
                if (pt.getType_name().equals(details.getTypeName())) {
                    typeComboBox.setSelectedIndex(i);
                    break;
                }
            }
            typeComboBox.revalidate();
            typeComboBox.repaint();
        });
        description.setText(details.getDescription());
    }
}
