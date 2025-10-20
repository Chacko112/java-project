package opps;

import javax.swing.*;

public class EmployeeMainFrame extends JFrame {

    public EmployeeMainFrame() {
        setTitle("Employee Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome to the Employee Dashboard", SwingConstants.CENTER);
        add(label);
    }
}
