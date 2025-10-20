package opps;

import javax.swing.*;
import java.awt.*;

public class AdminMainFrame extends JFrame {

    private JTabbedPane tabbedPane;

    public AdminMainFrame(String managerName) {
        setTitle("Manager Dashboard");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // ----- Create OperationsPanel first -----
        OperationsPanel operationsPanel = new OperationsPanel();

        // ----- Tabs -----
        tabbedPane.addTab("Home", new HomePanel(managerName));
        tabbedPane.addTab("Operations", operationsPanel);
        tabbedPane.addTab("Rooms", new RoomsPanel(operationsPanel, null));
        //tabbedPane.addTab("Payments", new PaymentsPanel());
        //tabbedPane.addTab("Reports", new ReportsPanel());
        //tabbedPane.addTab("About", new AboutPanel());
        //tabbedPane.addTab("User", new UserPanel(managerName));

        add(tabbedPane);

        // ----- Header -----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manager Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        UserBox userBox = new UserBox(managerName); // small profile box

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userBox, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    
}
