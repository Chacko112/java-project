package opps;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserPanel extends JPanel {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton addButton, removeButton;

    public UserPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Username"}, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Employee"));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        addButton = new JButton("Add Employee");
        removeButton = new JButton("Remove Selected");
        formPanel.add(addButton);
        formPanel.add(removeButton);

        add(formPanel, BorderLayout.SOUTH);
    }
}
