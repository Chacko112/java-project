package opps;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PaymentsPanel extends JPanel {

    JTabbedPane payTabs;
    JTable payTable, pendingTable;
    JTextField tBookingId, tAdditional;

    // Payment data lists, can be accessed by main frame
    ArrayList<PaymentRecord> payList = new ArrayList<>();
    ArrayList<PaymentRecord> pendingList = new ArrayList<>();
    int paymentCounter = 100;

    public PaymentsPanel() {
        setLayout(new BorderLayout());

        payTabs = new JTabbedPane();

        // --- BILL GENERATION TAB ---
        JPanel billPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lBookingId = new JLabel("Booking ID:");
        gbc.gridx = 0; gbc.gridy = 0;
        billPanel.add(lBookingId, gbc);
        tBookingId = new JTextField("BK1001",15);
        gbc.gridx = 1; gbc.gridy = 0;
        billPanel.add(tBookingId, gbc);

        JLabel lAdditional = new JLabel("Additional Payment:");
        gbc.gridx = 0; gbc.gridy = 1;
        billPanel.add(lAdditional, gbc);
        tAdditional = new JTextField("0",15);
        gbc.gridx = 1; gbc.gridy = 1;
        billPanel.add(tAdditional, gbc);

        JButton bGenerateBill = new JButton("Generate Bill");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth=2;
        gbc.anchor = GridBagConstraints.CENTER;
        billPanel.add(bGenerateBill, gbc);

        bGenerateBill.addActionListener(e -> openBillFrame());
        payTabs.add("BILL GENERATION", billPanel);

        // --- PAYMENT HISTORY TAB ---
        JPanel historyPanel = new JPanel(new BorderLayout());
        String[] payCols = {"PAYMENT NO", "CUSTOMER NAME", "AMOUNT", "MODE", "DATE"};
        payTable = new JTable(new DefaultTableModel(payCols,0));
        historyPanel.add(new JScrollPane(payTable), BorderLayout.CENTER);
        payTabs.add("PAYMENT HISTORY", historyPanel);

        // --- PENDING PAYMENTS TAB ---
        JPanel pendingPanel = new JPanel(new BorderLayout());
        String[] pendingCols = {"CUSTOMER NAME", "AMOUNT", "MODE", "DATE"};
        pendingTable = new JTable(new DefaultTableModel(pendingCols,0));
        pendingPanel.add(new JScrollPane(pendingTable), BorderLayout.CENTER);
        payTabs.add("PENDING PAYMENTS", pendingPanel);

        add(payTabs, BorderLayout.CENTER);
    }

    // --- Bill frame ---
    private void openBillFrame() {
        // Dummy booking data (replace with real booking later)
        String bookingId = tBookingId.getText().trim();
        String customerName = "John Doe";
        int rooms = 2;
        double roomCharge = 1200;
        String checkIn = "22 Oct 2025 14:00";
        String checkOut = "24 Oct 2025 11:00";
        double additional = 0;
        try { additional = Double.parseDouble(tAdditional.getText().trim()); } 
        catch(Exception ex){ JOptionPane.showMessageDialog(this,"Enter valid additional amount!"); return; }
        double totalAmount = roomCharge + additional;

        JFrame billFrame = new JFrame("Bill - "+bookingId);
        billFrame.setSize(400,400);
        billFrame.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        mainPanel.add(centerLabel("Customer Name: "+customerName));
        mainPanel.add(centerLabel("Booking ID: "+bookingId));
        mainPanel.add(centerLabel("Number of Rooms: "+rooms));
        mainPanel.add(centerLabel("Room Charge: "+roomCharge));
        mainPanel.add(centerLabel("Additional Payment: "+additional));
        mainPanel.add(centerLabel("Total Amount: "+totalAmount));
        mainPanel.add(centerLabel("Check-In: "+checkIn));
        mainPanel.add(centerLabel("Check-Out: "+checkOut));
        mainPanel.add(Box.createVerticalStrut(20));

        JButton bMakePayment = new JButton("Make Payment");
        bMakePayment.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(bMakePayment);

        bMakePayment.addActionListener(ev -> {
            billFrame.dispose();
            openPaymentMethodFrame(customerName, totalAmount);
        });

        billFrame.add(mainPanel);
        billFrame.setVisible(true);
    }

    // --- Payment method selection frame ---
    private void openPaymentMethodFrame(String customerName, double amount){
        JFrame methodFrame = new JFrame("Select Payment Method");
        methodFrame.setSize(300,200);
        methodFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(centerLabel("Choose Payment Method:"));
        panel.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        JButton bCash = new JButton("CASH");
        JButton bCard = new JButton("CARD");
        JButton bUpi = new JButton("UPI/ONLINE");
        btnPanel.add(bCash); btnPanel.add(bCard); btnPanel.add(bUpi);
        panel.add(btnPanel);

        bCash.addActionListener(e -> { methodFrame.dispose(); openIndividualPaymentFrame(customerName, amount,"CASH"); });
        bCard.addActionListener(e -> { methodFrame.dispose(); openIndividualPaymentFrame(customerName, amount,"CARD"); });
        bUpi.addActionListener(e -> { methodFrame.dispose(); openIndividualPaymentFrame(customerName, amount,"UPI"); });

        methodFrame.add(panel);
        methodFrame.setVisible(true);
    }

    // --- Individual payment frame ---
    private void openIndividualPaymentFrame(String customerName, double amount, String mode){
        JFrame payFrame = new JFrame(mode+" Payment");
        payFrame.setSize(300,200);
        payFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        panel.add(centerLabel("Make Payment ("+mode+")"));
        panel.add(Box.createVerticalStrut(20));

        JButton bMarkPaid = new JButton("Mark as Paid");
        bMarkPaid.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(bMarkPaid);

        bMarkPaid.addActionListener(e -> {
            PaymentRecord p = new PaymentRecord(paymentCounter++, customerName, amount, mode, new Date());
            payList.add(p);
            pendingList.removeIf(x->x.customerName.equals(customerName));
            updatePayTable();
            updatePendingTable();
            JOptionPane.showMessageDialog(payFrame,"Payment Successful!");
            payFrame.dispose();
        });

        payFrame.add(panel);
        payFrame.setVisible(true);
    }

    // --- Helper for centered JLabel ---
    private JLabel centerLabel(String text){
        JLabel lbl = new JLabel(text);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    // --- Update tables ---
    private void updatePayTable(){
        DefaultTableModel model = (DefaultTableModel) payTable.getModel();
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        for(PaymentRecord p: payList){
            model.addRow(new Object[]{p.paymentNo,p.customerName,p.amount,p.mode,sdf.format(p.date)});
        }
    }

    private void updatePendingTable(){
        DefaultTableModel model = (DefaultTableModel) pendingTable.getModel();
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
        for(PaymentRecord p: pendingList){
            model.addRow(new Object[]{p.customerName,p.amount,p.mode,sdf.format(p.date)});
        }
    }

    // --- PaymentRecord class ---
    public class PaymentRecord{
        int paymentNo;
        String customerName;
        double amount;
        String mode;
        Date date;
        PaymentRecord(int no,String name,double amt,String mode,Date date){
            this.paymentNo=no;
            this.customerName=name;
            this.amount=amt;
            this.mode=mode;
            this.date=date;
        }
    }
}
