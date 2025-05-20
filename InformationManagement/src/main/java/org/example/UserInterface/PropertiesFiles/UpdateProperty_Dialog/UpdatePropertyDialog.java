package org.example.UserInterface.PropertiesFiles.UpdateProperty_Dialog;

import org.example.Object.Property;
import java.awt.Frame;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UpdatePropertyDialog extends JDialog {
    private final DefaultTableModel propertyTableModel = new DefaultTableModel();
    private final Property property;
    public UpdatePropertyDialog(Frame parent, org.example.Object.PropertyDetails details) {
        super(parent, "Update Property", true);
        setUndecorated(true);
        setSize(550, 500);
        setLocationRelativeTo(parent);

        // Initialize property with details
        this.property = new Property();
        property.setId(details.getId());
        property.setProperty_name(details.getName());
        property.setAddress(details.getAddress());
        property.setSize(details.getSize());
        property.setRent_price((float) details.getRent());
        property.setType_id(-1); // Will be set on update
        property.setDescription(details.getDescription());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Update Property", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> dispose());
        headerPanel.add(closeButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        PropertyDetailsPanel detailsPanel = new PropertyDetailsPanel();
        detailsPanel.setPropertyDetails(details);
        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);

        JButton updateButton = ButtonFactory.createButton("Update Property", new Color(52, 152, 219), Color.WHITE);
        JButton cancelButton = ButtonFactory.createButton("Cancel", new Color(231, 76, 60), Color.WHITE);
        Dimension buttonSize = new Dimension(150, 40);
        updateButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        updateButton.addActionListener(e -> handleUpdate(detailsPanel));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void handleUpdate(PropertyDetailsPanel detailsPanel) {
        if (!detailsPanel.validateInputs()) return;

        property.setProperty_name(detailsPanel.getPropertyname());
        property.setAddress(detailsPanel.getAddress());
        property.setRent_price(detailsPanel.getPrice());
        property.setSize(detailsPanel.getPropertySize());
        property.setDescription(detailsPanel.getDetails());
        int typeId = detailsPanel.getTypeId();
        property.setType_id(typeId);
        property.setId(this.property.getId());

        boolean success = org.example.DAO.PropertyDAO.updateProperty(property);
        if (success) {
            JOptionPane.showMessageDialog(this, "Property updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update property.");
        }
    }
}
