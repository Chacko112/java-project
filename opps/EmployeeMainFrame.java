package opps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class EmployeeMainFrame extends JFrame {

	private JTabbedPane tabbedPane;
	
    public EmployeeMainFrame(String empName) {
        setTitle("Employee Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
       

        JLabel label = new JLabel("Welcome to the Employee Dashboard", SwingConstants.CENTER);
        add(label);
        
        tabbedPane = new JTabbedPane();

        // ----- Create OperationsPanel first -----
        OperationsPanel operationsPanel = new OperationsPanel();

        // TABSSS
        tabbedPane.addTab("Home", new HomePanel(empName));
        tabbedPane.addTab("Operations", operationsPanel);
        tabbedPane.addTab("Rooms", new RoomsPanel(operationsPanel, null));
        tabbedPane.addTab("Payments", new PaymentsPanel());
       // tabbedPane.addTab("Reports", new ReportsPanel());
       // tabbedPane.addTab("Users", new UserPanel());
		
		add(tabbedPane);
		
	     // ----- Header -----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manager Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
       

        UserBox userbox = new UserBox(empName); // small profile box

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userbox, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

        
    
}