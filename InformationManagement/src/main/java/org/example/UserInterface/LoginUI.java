package org.example.UserInterface;
    import org.example.DAO.UsersDAO;
    import org.example.UserInterface.Signup.SignupUI;
    import org.example.UserInterface.UserUI.TenantPortal_Panel;

    import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

    public class LoginUI extends JFrame {
        private final JTextField usernameField;
        private final JPasswordField passwordField;

        // Custom color (RGB: 52, 152, 219)
        private final Color primaryColor = new Color(52, 152, 219);
        private final Color whiteColor = Color.WHITE;
        private final Color greenColor = new Color(46, 204, 113);

        public LoginUI() {
            setTitle("Login");
            setSize(400, 500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setUndecorated(true);
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

            // Main panel with rounded corners
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
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

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
            JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
            titleLabel.setForeground(primaryColor);

            headerPanel.add(closeButton, BorderLayout.EAST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // Form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(3, 1, 0, 15));
            formPanel.setOpaque(false);
            formPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

            // Form fields
            usernameField = createStyledTextField("Username");
            passwordField = createStyledPasswordField("Password");

            formPanel.add(usernameField);
            formPanel.add(passwordField);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setOpaque(false);


            JButton adminLoginButton = new JButton("Login as Admin");
            styleButton(adminLoginButton, primaryColor);
            adminLoginButton.setPreferredSize(new Dimension(200, 30));
            adminLoginButton.setForeground(Color.BLACK);
            adminLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
            adminLoginButton.setMargin(new Insets(5, 10, 5, 10));
            adminLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();

                }
            });

            JButton tenantLoginButton = new JButton("Login as Tenant");
            styleButton(tenantLoginButton, greenColor);
            tenantLoginButton.setPreferredSize(new Dimension(200, 30));
            tenantLoginButton.setForeground(Color.BLACK);
            tenantLoginButton.setFont(new Font("Arial", Font.PLAIN, 12));
            tenantLoginButton.setMargin(new Insets(5, 10, 5, 10));
            tenantLoginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword()).trim();

                    boolean isValid = UsersDAO.authenticate(username,password);
                    if (isValid) {
                        JFrame frame = new JFrame("Tenant Portal");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setSize(800, 600);
                        frame.add(new TenantPortal_Panel());
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);
                        new TenantPortal_Panel().setVisible(true); // replace with your next UI
                        System.out.println("LOgin already");
                        dispose(); // close login form
                    } else {
                        Component Button = new JButton();
                        JOptionPane.showMessageDialog(Button, "Invalid credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                   }
            });

            JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            adminPanel.setOpaque(false);
            adminLoginButton.setPreferredSize(new Dimension(150, 30));
            adminPanel.add(adminLoginButton);

            JPanel tenantPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            tenantPanel.setOpaque(false);
            tenantLoginButton.setPreferredSize(new Dimension(150, 30));
            tenantPanel.add(tenantLoginButton);

            buttonPanel.add(adminPanel);
            buttonPanel.add(tenantPanel);

            // Signup link
            JPanel signupPanel = new JPanel();
            signupPanel.setOpaque(false);
            JLabel signupLabel = new JLabel("Don't have an account? ");
            JButton signupButton = new JButton("Sign Up");
            signupButton.setFont(new Font("Arial", Font.BOLD, 12));
            signupButton.setForeground(primaryColor);
            signupButton.setBorder(BorderFactory.createEmptyBorder());
            signupButton.setContentAreaFilled(false);
            signupButton.setFocusPainted(false);
            signupButton.addActionListener(e -> {
                dispose();
                new SignupUI().setVisible(true);
            });
            signupPanel.add(signupLabel);
            signupPanel.add(signupButton);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.setOpaque(false);

            bottomPanel.add(buttonPanel, BorderLayout.CENTER);
            bottomPanel.add(signupPanel, BorderLayout.SOUTH);

            mainPanel.add(headerPanel, BorderLayout.NORTH);
            mainPanel.add(formPanel, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);

            add(mainPanel);
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

        private void styleButton(JButton button, Color bgColor) {
            button.setFont(new Font("Arial", Font.BOLD, 16));
            button.setForeground(whiteColor);
            button.setBackground(bgColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
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

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LoginUI loginUI = new LoginUI();
                loginUI.setVisible(true);
            });
        }
    }
