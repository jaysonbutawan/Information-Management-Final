package org.example.UserInterface.PropertiesFiles.AddProperty_Dialog;
import javax.swing.*;
import java.awt.*;

public class InputValidator {

    public boolean validateNonEmpty(JTextField field, String errorMessage) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            showError(field, errorMessage);
            return false;
        }
        resetBorder(field);
        return true;
    }

    public boolean validatePositiveDouble(JTextField field, String errorMessage) {
        String text = field.getText().trim();
        try {
            double value = Double.parseDouble(text);
            if (value <= 0) {
                showError(field, errorMessage);
                return false;
            }
        } catch (NumberFormatException ex) {
            showError(field, "Invalid number. " + errorMessage);
            return false;
        }
        resetBorder(field);
        return true;
    }

    private void showError(JTextField field, String errorMessage) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        JOptionPane.showMessageDialog(field.getParent(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetBorder(JTextField field) {
        field.setBorder(UIManager.getBorder("TextField.border"));
    }
}
