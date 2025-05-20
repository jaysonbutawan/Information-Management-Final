package org.example.UserInterface.Signup;
import org.example.DAO.UsersDAO;
import org.example.UserInterface.LoginUI;
import org.mindrot.jbcrypt.BCrypt;
import org.example.Object.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupUI extends JFrame {

    private final Color primaryColor = new Color(52, 152, 219);
    private final Color whiteColor = Color.WHITE;
    private JFormattedTextField dateField;

    public SignupUI() {
        setTitle("Create Your Account");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(whiteColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Close button
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 24));
        closeButton.setForeground(Color.GRAY);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> System.exit(0));

        // Title label
        JLabel titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);

        // Not already Implemented
        JLabel birthdate = new JLabel("Birthday?", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        Color fontColor = new Color(111, 230, 252);
        birthdate.setForeground(fontColor);



        headerPanel.add(closeButton, BorderLayout.EAST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 1, 0, 15));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JTextField firstname = createStyledTextField("First Name");
        JTextField lastname = createStyledTextField("Last Name");
        JTextField emailField = createStyledTextField("Email Address");
        JPasswordField passwordField = createStyledPasswordField("Password");
        JPasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");



        formPanel.add(firstname);
        formPanel.add(lastname);
        formPanel.add(emailField);
        formPanel.add(passwordField);
        formPanel.add(confirmPasswordField);
        formPanel.add(createDatePickerField());

        JButton signupButton = new JButton("Create Account");
        Color greenColor = new Color(46, 204, 113);
        styleButton(signupButton, greenColor);
        signupButton.setFont(new Font("Arial", Font.BOLD, 20));
        signupButton.setForeground(Color.BLACK);

        styleButton(signupButton, greenColor);
        signupButton.addActionListener(e -> {
            String rawPassword = new String(passwordField.getPassword()).trim();
            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            Date birthday = (Date) dateField.getValue();
            Users users = new Users(firstname.getText().trim(), lastname.getText().trim(),emailField.getText().trim(),hashedPassword,birthday);
            UsersDAO.insertUser(users);
            dispose();
            new LoginUI().setVisible(true);
        });

        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        JLabel loginLabel = new JLabel("Already have an account? ");
        JButton loginButton = new JButton("Log In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setForeground(primaryColor);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener( event->{
            dispose();
            new LoginUI().setVisible(true);
        });
        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(signupButton, BorderLayout.CENTER);
        bottomPanel.add(loginPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    private JPanel createDatePickerField() {
        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.setOpaque(false);

        // Create the date field
         dateField = new JFormattedTextField(new SimpleDateFormat("MM/dd/yyyy"));
        dateField.setValue(new Date()); // Set current date as default
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        dateField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Create calendar button
        JButton calendarButton = new JButton("📅");
        calendarButton.setFont(new Font("Arial", Font.PLAIN, 14));
        calendarButton.setForeground(primaryColor);
        calendarButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        calendarButton.setContentAreaFilled(false);
        calendarButton.setFocusPainted(false);

        // Add action to show calendar dialog
        calendarButton.addActionListener(e -> {
            JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
            dateSpinner.setEditor(dateEditor);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    dateSpinner,
                    "Choose Birthday",
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
    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.GRAY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        textField.setOpaque(false);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(placeholder);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setForeground(Color.GRAY);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        passwordField.setOpaque(false);
        passwordField.setEchoChar((char) 0);

        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar('•');
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText(placeholder);
                }
            }
        });

        return passwordField;
    }

    private void styleButton(JButton button, Color greenColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(whiteColor);
        button.setBackground(primaryColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            SignupUI signupUI = new SignupUI();
//            signupUI.setVisible(true);
//        });
//    }
}
