package org.example.UserInterface.PropertiesFiles.UpdateProperty_Dialog;

import javax.swing.*;
import java.awt.*;

public class ValidationUtils {

    public static boolean validateNonEmpty(JTextField field, String errorMessage) {
        if (field.getText().trim().isEmpty()) {
            highlightError(field, errorMessage);
            return false;
        }
        resetBorder(field);
        return true;
    }

    public static boolean validatePositiveNumber(JTextField field, String errorMessage) {
        try {
            float value = Float.parseFloat(field.getText().trim());
            if (value <= 0) {
                highlightError(field, errorMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            highlightError(field, errorMessage);
            return false;
        }
        resetBorder(field);
        return true;
    }

    public static boolean validatePositiveInteger(JTextField field, String errorMessage) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value <= 0) {
                highlightError(field, errorMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            highlightError(field, errorMessage);
            return false;
        }
        resetBorder(field);
        return true;
    }

    private static void highlightError(JTextField field, String errorMessage) {
        field.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void resetBorder(JTextField field) {
        field.setBorder(UIManager.getBorder("TextField.border"));
    }
}
