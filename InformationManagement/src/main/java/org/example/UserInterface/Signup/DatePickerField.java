package org.example.UserInterface.Signup;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatePickerField {
    private JFormattedTextField dateField;

    public JPanel createDatePickerField() {
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);

        dateField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
        dateField.setValue(new Date()); // Set current date as default
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Create calendar button
        JButton calendarButton = new JButton("📅");
        calendarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        calendarButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        calendarButton.setContentAreaFilled(false);
        calendarButton.setFocusPainted(false);

        // Add action to show calendar dialog
        calendarButton.addActionListener(e -> {
            JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
            dateSpinner.setEditor(dateEditor);

            int result = JOptionPane.showConfirmDialog(
                    null,
                    dateSpinner,
                    "Choose Date",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                Date selectedDate = (Date) dateSpinner.getValue();
                dateField.setValue(selectedDate);
            }
        });

        datePanel.add(dateField, BorderLayout.CENTER);
        datePanel.add(calendarButton, BorderLayout.EAST);

        return datePanel;
    }

    public String getSelectedDateString() {
        return dateField.getText(); // Returns the selected date as a String
    }

    public Date getSelectedDate() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            return format.parse(dateField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null if parsing fails
        }
    }
}
