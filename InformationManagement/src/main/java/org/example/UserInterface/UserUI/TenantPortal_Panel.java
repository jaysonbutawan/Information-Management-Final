package org.example.UserInterface.UserUI;

import org.example.DAO.PropertyDAO;
import org.example.Object.PropertyDetails;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TenantPortal_Panel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private JButton viewLeasesButton;
    private JButton actionButton;
    private static JTable propertyTable;
    private static boolean viewingLeases = false;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color BACKGROUND_COLOR = new Color(245, 248, 250);
    private final Color TABLE_HEADER_COLOR = new Color(240, 240, 240);
    private final Color TABLE_ROW_EVEN = new Color(255, 255, 255);
    private final Color TABLE_ROW_ODD = new Color(248, 248, 248);

    public TenantPortal_Panel() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(BACKGROUND_COLOR);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 2, 0, PRIMARY_COLOR.darker()),
                new EmptyBorder(15, 25, 15, 25)
        ));

        // Profile section
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        profilePanel.setOpaque(false);

        // Profile icon
        JLabel profileIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(PRIMARY_COLOR.darker());
                g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
                g2.dispose();

                super.paintComponent(g);
            }
        };
        profileIcon.setPreferredSize(new Dimension(40, 40));
        profileIcon.setHorizontalAlignment(SwingConstants.CENTER);
        profileIcon.setFont(new Font("Arial", Font.BOLD, 16));
        profileIcon.setText("TP");

        // Title
        JLabel headerLabel = new JLabel("TENANT PORTAL");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(new EmptyBorder(0, 15, 0, 0));

        profilePanel.add(profileIcon);
        profilePanel.add(headerLabel);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        viewLeasesButton = new JButton(viewingLeases ? "VIEW PROPERTIES" : "VIEW LEASES");
        styleButton(viewLeasesButton, SECONDARY_COLOR);
        viewLeasesButton.addActionListener(this::toggleLeaseView);

        buttonPanel.add(viewLeasesButton);

        headerPanel.add(profilePanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create table with custom styling
        propertyTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                }
                return c;
            }
        };

        // Custom table header
        JTableHeader header = propertyTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.DARK_GRAY);
        header.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)
        ));

        propertyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        propertyTable.setRowHeight(30);
        propertyTable.setShowGrid(false);
        propertyTable.setIntercellSpacing(new Dimension(0, 0));
        propertyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(propertyTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 10, 0),
                new MatteBorder(1, 1, 1, 1, new Color(220, 220, 220))
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);

        updateTableData();

        // Action button
        actionButton = new JButton(viewingLeases ? "TERMINATE/PAY RENT" : "REQUEST LEASE");
        styleButton(actionButton, PRIMARY_COLOR);
        actionButton.addActionListener(this::handleActionButton);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 0, 5, 0)
        ));

        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(actionButton);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private static void updateTableData() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (viewingLeases) {
            model.setColumnIdentifiers(new String[]{"Property", "Address", "Lease Start", "Lease End", "Rent Amount", "Description"});
            // In a real implementation, you would fetch lease data here
            // Example:
            // List<LeaseDetails> leases = LeaseDAO.getLeasesForTenant(currentTenantId);
            // for (LeaseDetails lease : leases) { ... }
        } else {
            model.setColumnIdentifiers(new String[]{"ID", "Property", "Address", "Type", "Monthly Rent", "Description", "Status"});

            List<PropertyDetails> propertyList = PropertyDAO.fetchAll();
            for (PropertyDetails pd : propertyList) {
                if ("Available".equalsIgnoreCase(pd.getStatus())) {
                    model.addRow(new Object[]{
                            pd.getId(),
                            pd.getName(),
                            pd.getAddress(),
                            pd.getTypeName(),
                            String.format("$%,.2f", pd.getRent()),
                            pd.getDescription(),
                            pd.getStatus()
                    });
                }
            }
        }

        propertyTable.setModel(model);

        // Custom column alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        if (!viewingLeases) {
            propertyTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
            propertyTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Rent

            // Set column widths
            propertyTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            propertyTable.getColumnModel().getColumn(1).setPreferredWidth(180);
            propertyTable.getColumnModel().getColumn(2).setPreferredWidth(250);
            propertyTable.getColumnModel().getColumn(3).setPreferredWidth(120);
            propertyTable.getColumnModel().getColumn(4).setPreferredWidth(120);
            propertyTable.getColumnModel().getColumn(5).setPreferredWidth(300);
            propertyTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        }
    }

    public static void refreshPropertiesTenant() {
        updateTableData();
    }

    private void toggleLeaseView(ActionEvent e) {
        viewingLeases = !viewingLeases;
        if (viewingLeases) {
            viewLeasesButton.setText("VIEW PROPERTIES");
            actionButton.setText("TERMINATE/PAY RENT");
        } else {
            viewLeasesButton.setText("VIEW LEASES");
            actionButton.setText("REQUEST LEASE");
        }
        updateTableData();
    }

    private void handleActionButton(ActionEvent e) {
        if (viewingLeases) {
            // Handle lease actions (terminate/pay rent)
            Object[] options = {"Pay Rent", "Terminate Lease", "Cancel"};
            int choice = JOptionPane.showOptionDialog(this,
                    "<html><div style='width:200px;'><b>Choose an action:</b></div></html>",
                    "Lease Management",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) { // Pay Rent
                String amount = (String) JOptionPane.showInputDialog(this,
                        "<html><div style='width:200px;'>Enter payment amount:</div></html>",
                        "Rent Payment",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "");

                if (amount != null && !amount.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "<html><div style='width:200px;'>Payment of <b>$" + amount + "</b> submitted successfully!</div></html>",
                            "Payment Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (choice == 1) { // Terminate
                int confirm = JOptionPane.showConfirmDialog(this,
                        "<html><div style='width:200px;'>Are you sure you want to terminate this lease?</div></html>",
                        "Confirm Termination",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this,
                            "<html><div style='width:200px;'>Lease termination request submitted.</div></html>",
                            "Termination Request",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } else {
            // Handle property lease request
            int selectedRow = propertyTable.getSelectedRow();
            if (selectedRow >= 0) {
                String propertyId = propertyTable.getValueAt(selectedRow, 0).toString();
                String propertyName = propertyTable.getValueAt(selectedRow, 1).toString();

                int confirm = JOptionPane.showConfirmDialog(this,
                        "<html><div style='width:250px;'>Request lease for property:<br><b>" + propertyName + "</b> (ID: " + propertyId + ")?</div></html>",
                        "Lease Request",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    int propertyIds = Integer.parseInt(propertyTable.getValueAt(selectedRow, 0).toString());

                    // Automatically set start and due dates
                    LocalDate startLocalDate = LocalDate.now();
                    LocalDate dueLocalDate = startLocalDate.plusMonths(1);

                    Date startDate = Date.valueOf(startLocalDate);
                    Date dueDate = Date.valueOf(dueLocalDate);

//                    boolean success = LeaseDAO.requestLease(currentTenantId, propertyIds, startDate, dueDate);

//                    if (success) {
//                        JOptionPane.showMessageDialog(this,
//                                "<html><div style='width:250px;'>Lease request submitted for:<br><b>" + propertyName + "</b><br>Start: " + startDate + "<br>Due: " + dueDate + "</div></html>",
//                                "Request Confirmation",
//                                JOptionPane.INFORMATION_MESSAGE);
//                        refreshPropertiesTenant(); // Refresh table if needed
//                    } else {
//                        JOptionPane.showMessageDialog(this,
//                                "Failed to submit lease request. Try again.",
//                                "Error",
//                                JOptionPane.ERROR_MESSAGE);
//                    }
                }

            }
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker(), 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(bgColor.darker().darker(), 1, true),
                        new EmptyBorder(8, 20, 8, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(bgColor.darker(), 1, true),
                        new EmptyBorder(8, 20, 8, 20)
                ));
            }
        });
    }
}