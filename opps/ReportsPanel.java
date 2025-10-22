package opps;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout());

        JTabbedPane reportTabs = new JTabbedPane();

        // --- Weekly Report Tab ---
        JPanel weeklyPanel = new JPanel();
        weeklyPanel.setLayout(new BoxLayout(weeklyPanel, BoxLayout.Y_AXIS));
        weeklyPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel lWeeklyTitle = new JLabel("WEEKLY REPORT");
        lWeeklyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lWeeklyTitle.setFont(new Font("Arial", Font.BOLD, 16));
        weeklyPanel.add(lWeeklyTitle);
        weeklyPanel.add(Box.createVerticalStrut(20));

        weeklyPanel.add(centerLabel("CUSTOMER - 62.5%"));
        weeklyPanel.add(centerLabel("ELECTRICITY - 25%"));
        weeklyPanel.add(centerLabel("OTHER EXPENSE - 12.5%"));

        reportTabs.addTab("WEEKLY", weeklyPanel);

        // --- Monthly Report Tab ---
        JPanel monthlyPanel = new JPanel();
        monthlyPanel.setLayout(new BoxLayout(monthlyPanel, BoxLayout.Y_AXIS));
        monthlyPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel lMonthlyTitle = new JLabel("MONTHLY REPORT");
        lMonthlyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lMonthlyTitle.setFont(new Font("Arial", Font.BOLD, 16));
        monthlyPanel.add(lMonthlyTitle);
        monthlyPanel.add(Box.createVerticalStrut(20));

        monthlyPanel.add(centerLabel("CUSTOMER, ELECTRICITY, WATER BILL"));
        monthlyPanel.add(centerLabel("SALARY, MAINTENANCE, SECURITY"));

        reportTabs.addTab("MONTHLY", monthlyPanel);

        add(reportTabs, BorderLayout.CENTER);
    }

    // Helper to center labels
    private JLabel centerLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }
}
