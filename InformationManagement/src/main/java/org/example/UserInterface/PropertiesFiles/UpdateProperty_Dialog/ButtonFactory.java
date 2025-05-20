package org.example.UserInterface.PropertiesFiles.UpdateProperty_Dialog;

import javax.swing.*;
import java.awt.*;

public class ButtonFactory {

    public static JButton createButton(String text, Color backgroundColor, Color foregroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(foregroundColor);
        button.setFocusPainted(false);
        return button;
    }
}
