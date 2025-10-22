package opps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

// Room class to store room information
class Room {
    public int roomNumber;
    public String roomType;
    public String status;
    public double price;
    public String guestName;
    public String guestPhone;

    public Room(int roomNumber, String roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.status = "Available";
        this.guestName = "";
        this.guestPhone = "";
    }
}

// Room Panel Component
class RoomPanelComponent extends JPanel {
    public Room room;
    public JLabel roomLabel, statusLabel;
    public JButton actionButton, maintenanceButton;

    public RoomPanelComponent(Room room, ActionListener bookListener, ActionListener maintenanceListener) {
        this.room = room;
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(150, 140));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        roomLabel = new JLabel("Room " + room.roomNumber);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel typeLabel = new JLabel(room.roomType);
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        JLabel priceLabel = new JLabel("₹" + room.price + "/night");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        statusLabel = new JLabel(room.status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        infoPanel.add(roomLabel);
        infoPanel.add(typeLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(statusLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2,1,2,2));
        actionButton = new JButton();
        actionButton.addActionListener(bookListener);
        maintenanceButton = new JButton("Maintenance");
        maintenanceButton.setFont(new Font("Arial", Font.PLAIN, 10));
        maintenanceButton.addActionListener(maintenanceListener);
        buttonPanel.add(actionButton);
        buttonPanel.add(maintenanceButton);

        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        updateDisplay();
    }

    public void updateDisplay() {
        switch(room.status) {
            case "Available":
                setBackground(new Color(144,238,144));
                statusLabel.setForeground(new Color(0,128,0));
                actionButton.setText("Book Room");
                actionButton.setEnabled(true);
                maintenanceButton.setEnabled(true);
                break;
            case "Occupied":
                setBackground(new Color(255,182,193));
                statusLabel.setForeground(new Color(178,34,34));
                actionButton.setText("Check Out");
                actionButton.setEnabled(true);
                maintenanceButton.setEnabled(false);
                break;
            case "Maintenance":
                setBackground(new Color(255,255,153));
                statusLabel.setForeground(new Color(184,134,11));
                actionButton.setText("Unavailable");
                actionButton.setEnabled(false);
                maintenanceButton.setText("End Maintenance");
                maintenanceButton.setEnabled(true);
                break;
        }
        statusLabel.setText(room.status);
    }
}

// Rooms Panel
public class RoomsPanel extends JPanel {
    public ArrayList<Room> rooms;
    public ArrayList<RoomPanelComponent> roomPanels;
    public JPanel roomsDisplayPanel;
    public JLabel statsLabel;

    public ArrayList<OperationsPanel.Booking> bookings = new ArrayList<>();
    public OperationsPanel operationsPanelReference; // Reference to use the same booking dialog

    public RoomsPanel(OperationsPanel opsPanel, ArrayList<Room> roomList) {
        this.operationsPanelReference = opsPanel;
        setLayout(new BorderLayout(10,10));
        if (roomList != null) rooms = roomList;
        else initializeRooms();

        add(createHeaderPanel(), BorderLayout.NORTH);

        roomsDisplayPanel = new JPanel(new GridLayout(0,4,10,10));
        roomsDisplayPanel.setBackground(Color.WHITE);
        displayRooms();
        JScrollPane scrollPane = new JScrollPane(roomsDisplayPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scrollPane, BorderLayout.CENTER);

        JPanel footer = createFooterPanel();
        add(footer, BorderLayout.SOUTH);
    }

	private void initializeRooms() {
        rooms = new ArrayList<>();
        roomPanels = new ArrayList<>();
        for (int i=101;i<=106;i++) rooms.add(new Room(i,"Single",1500));
        for (int i=201;i<=208;i++) rooms.add(new Room(i,"Double",2500));
        for (int i=301;i<=304;i++) rooms.add(new Room(i,"Suite",5000));
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41,128,185));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JLabel titleLabel = new JLabel("Hotel Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateStats());

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(refreshButton, BorderLayout.EAST);
        return panel;
    }

    private void displayRooms() {
        roomsDisplayPanel.removeAll();
        roomPanels = new ArrayList<>();

        for (Room room : rooms) {
            RoomPanelComponent panel = new RoomPanelComponent(room,
                new RoomActionListener(room),
                new MaintenanceActionListener(room));
            roomPanels.add(panel);
            roomsDisplayPanel.add(panel);
        }

        roomsDisplayPanel.revalidate();
        roomsDisplayPanel.repaint();

        if (statsLabel != null) updateStats();
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236,240,241));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        statsLabel = new JLabel();
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        updateStats();
        panel.add(statsLabel);
        return panel;
    }

    private void updateStats() {
        int available=0, occupied=0, maintenance=0;
        double totalRevenue=0;
        for (Room r: rooms){
            switch(r.status){
                case "Available": available++; break;
                case "Occupied": occupied++; totalRevenue+=r.price; break;
                case "Maintenance": maintenance++; break;
            }
        }
        int total = rooms.size();
        double occupancy = (occupied*100.0)/total;
        statsLabel.setText(String.format(
            "Total Rooms: %d | Available: %d | Occupied: %d | Maintenance: %d | Occupancy: %.1f%% | Daily Revenue: ₹%.2f",
            total, available, occupied, maintenance, occupancy, totalRevenue));
    }

    // Action Listeners
    class RoomActionListener implements ActionListener {
        private Room room;
        public RoomActionListener(Room room){this.room=room;}
        public void actionPerformed(ActionEvent e){
            if (room.status.equals("Available")) bookRoom(room);
            else if (room.status.equals("Occupied")) checkoutRoom(room);
        }
    }

    class MaintenanceActionListener implements ActionListener {
        private Room room;
        public MaintenanceActionListener(Room room){this.room=room;}
        public void actionPerformed(ActionEvent e){
            if (room.status.equals("Maintenance")) endMaintenance(room);
            else if (room.status.equals("Available")) startMaintenance(room);
        }
    }

    // --- Booking and Checkout using OperationsPanel dialog and BookingManager ---
    private void bookRoom(Room room){
        if (operationsPanelReference != null) {
            operationsPanelReference.showAddBookingDialog();

            // Update room status if a booking exists for this room
            for (OperationsPanel.Booking b: operationsPanelReference.bookings){
                if (b.room == room.roomNumber && room.status.equals("Available")){
                    room.status="Occupied";
                    room.guestName=b.customer;
                    room.guestPhone="";
                    break;
                }
            }
            updateRoomDisplay(room);
            updateStats();
        }
    }

    private void checkoutRoom(Room room){
        String bookingId = null;
        for (OperationsPanel.Booking b: operationsPanelReference.bookings){
            if (b.room == room.roomNumber && room.status.equals("Occupied")){
                bookingId = b.bookingId;
                break;
            }
        }
        if (bookingId != null){
            int result = JOptionPane.showConfirmDialog(this,
                "Checkout room " + room.roomNumber + "?",
                "Confirm Checkout", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                BookingManager.checkOut(operationsPanelReference.bookings, bookingId);
                room.status="Available";
                room.guestName="";
                room.guestPhone="";
                updateRoomDisplay(room);
                updateStats();
            }
        }
    }

    private void startMaintenance(Room room){
        room.status="Maintenance";
        updateRoomDisplay(room);
        updateStats();
    }

    private void endMaintenance(Room room){
        room.status="Available";
        updateRoomDisplay(room);
        updateStats();
    }

    private void updateRoomDisplay(Room room){
        for (RoomPanelComponent panel: roomPanels){
            if (panel.room==room){
                panel.updateDisplay(); break;
            }
        }
    }
}
