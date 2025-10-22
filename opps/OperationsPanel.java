package opps;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class OperationsPanel extends JPanel {

    public JTable checkinTable, checkoutTable, bookingTable, roomAvailabilityTable;
    public JSpinner dateSpinner;

    public static class Booking {
        public String bookingId, customer;
        public int room;
        public LocalDate checkInDate, checkOutDate, bookingDate;

        public Booking(String id, String cust, int room, LocalDate checkIn, LocalDate checkOut, LocalDate bookingDate) {
            this.bookingId = id;
            this.customer = cust;
            this.room = room;
            this.checkInDate = checkIn;
            this.checkOutDate = checkOut;
            this.bookingDate = bookingDate;
        }
    }

    public static class RoomInfo {
        public int roomNumber;
        public String type;
        public boolean available;

        public RoomInfo(int num, String type, boolean avail) {
            this.roomNumber = num;
            this.type = type;
            this.available = avail;
        }
    }

    public List<Booking> bookings = new ArrayList<>();
    public List<RoomInfo> rooms = new ArrayList<>();
    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public OperationsPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- Top Filter Panel ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        filterPanel.add(new JLabel("Select Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setValue(new Date());
        filterPanel.add(dateSpinner);

        JButton filterBtn = new JButton("Filter");
        filterBtn.addActionListener(e -> refreshTables());
        filterPanel.add(filterBtn);
        add(filterPanel, BorderLayout.NORTH);

        // --- Tables ---
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        checkinTable = new JTable(new DefaultTableModel(new String[]{"Booking ID", "Customer", "Room", "Check-in Date"}, 0));
        centerPanel.add(createTitledPanel("Check-ins", new JScrollPane(checkinTable)));

        checkoutTable = new JTable(new DefaultTableModel(new String[]{"Booking ID", "Customer", "Room", "Check-out Date"}, 0));
        centerPanel.add(createTitledPanel("Check-outs", new JScrollPane(checkoutTable)));

        bookingTable = new JTable(new DefaultTableModel(new String[]{"Booking ID", "Customer", "Room", "Check-in", "Check-out"}, 0));
        centerPanel.add(createTitledPanel("Bookings", new JScrollPane(bookingTable)));

        roomAvailabilityTable = new JTable(new DefaultTableModel(new String[]{"Room No", "Type", "Available"}, 0));
        centerPanel.add(createTitledPanel("Room Availability", new JScrollPane(roomAvailabilityTable)));

        add(centerPanel, BorderLayout.CENTER);

        // --- Action Buttons ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton checkInBtn = new JButton("Check In");
        JButton checkOutBtn = new JButton("Check Out");
        JButton addBookingBtn = new JButton("Add Booking");
        JButton cancelBookingBtn = new JButton("Cancel Booking");

        actionPanel.add(checkInBtn);
        actionPanel.add(checkOutBtn);
        actionPanel.add(addBookingBtn);
        actionPanel.add(cancelBookingBtn);
        add(actionPanel, BorderLayout.SOUTH);

        // --- Attach dialogs ---
        checkInBtn.addActionListener(e -> showCheckInDialog());
        checkOutBtn.addActionListener(e -> showCheckOutDialog());
        addBookingBtn.addActionListener(e -> showAddBookingDialog());
        cancelBookingBtn.addActionListener(e -> showCancelBookingDialog());

        // --- Dummy data ---
        initDummyData();
        refreshTables();
    }

    public JPanel createTitledPanel(String title, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void initDummyData() {
        LocalDate today = LocalDate.now();
        rooms.add(new RoomInfo(101, "Single", true));
        rooms.add(new RoomInfo(102, "Single", true));
        rooms.add(new RoomInfo(201, "Double", true));
        rooms.add(new RoomInfo(202, "Double", true));
        rooms.add(new RoomInfo(301, "Suite", true));
        rooms.add(new RoomInfo(302, "Suite", true));

        BookingManager.addBooking(bookings, "Alice", 101, today, today.plusDays(2));
        BookingManager.addBooking(bookings, "Bob", 201, today.minusDays(1), today);
        BookingManager.addBooking(bookings, "Charlie", 301, today.plusDays(1), today.plusDays(3));
    }

    public void refreshTables() {
        Date selected = (Date) dateSpinner.getValue();
        LocalDate selectedDate = selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        ((DefaultTableModel) checkinTable.getModel()).setRowCount(0);
        ((DefaultTableModel) checkoutTable.getModel()).setRowCount(0);
        ((DefaultTableModel) bookingTable.getModel()).setRowCount(0);
        ((DefaultTableModel) roomAvailabilityTable.getModel()).setRowCount(0);

        for (Booking b : bookings) {
            if (b.checkInDate.equals(selectedDate))
                ((DefaultTableModel) checkinTable.getModel())
                        .addRow(new Object[]{b.bookingId, b.customer, b.room, b.checkInDate.format(dateFormat)});
            if (b.checkOutDate.equals(selectedDate))
                ((DefaultTableModel) checkoutTable.getModel())
                        .addRow(new Object[]{b.bookingId, b.customer, b.room, b.checkOutDate.format(dateFormat)});
            if (b.bookingDate.equals(selectedDate))
                ((DefaultTableModel) bookingTable.getModel())
                        .addRow(new Object[]{b.bookingId, b.customer, b.room,
                                b.checkInDate.format(dateFormat),
                                b.checkOutDate.format(dateFormat)});
        }

        for (RoomInfo r : rooms) {
            boolean occupied = false;
            for (Booking b : bookings) {
                if (b.room == r.roomNumber && (!selectedDate.isBefore(b.checkInDate) && !selectedDate.isAfter(b.checkOutDate))) {
                    occupied = true;
                    break;
                }
            }
            ((DefaultTableModel) roomAvailabilityTable.getModel())
                    .addRow(new Object[]{r.roomNumber, r.type, occupied ? "No" : "Yes"});
        }
    }

    // --- Utility: get unique room types ---
    private List<String> getRoomTypes() {
        return rooms.stream()
                .map(r -> r.type)
                .distinct()
                .collect(Collectors.toList());
    }

    // --- Utility: get available rooms by type ---
    private List<Integer> getAvailableRoomsByType(String type) {
        return rooms.stream()
                .filter(r -> r.type.equals(type) && r.available)
                .map(r -> r.roomNumber)
                .collect(Collectors.toList());
    }

    // --- Check-In dialog ---
    public void showCheckInDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Check-In", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JComboBox<String> roomTypeBox = new JComboBox<>(getRoomTypes().toArray(new String[0]));
        DefaultListModel<Integer> roomListModel = new DefaultListModel<>();
        JList<Integer> roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        roomTypeBox.addActionListener(ev -> {
            roomListModel.clear();
            String selectedType = (String) roomTypeBox.getSelectedItem();
            getAvailableRoomsByType(selectedType).forEach(roomListModel::addElement);
        });

        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "dd-MM-yyyy"));
        checkInSpinner.setValue(new Date());
        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "dd-MM-yyyy"));

        dialog.add(new JLabel("Customer Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Room Type:")); dialog.add(roomTypeBox);
        dialog.add(new JLabel("Select Room(s):")); dialog.add(new JScrollPane(roomList));
        dialog.add(new JLabel("Check-in Date:")); dialog.add(checkInSpinner);
        dialog.add(new JLabel("Check-out Date:")); dialog.add(checkOutSpinner);

        JButton submit = new JButton("Submit");
        submit.addActionListener(ev -> {
            try {
                String name = nameField.getText();
                List<Integer> selectedRooms = roomList.getSelectedValuesList();
                LocalDate checkIn = ((Date) checkInSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOut = ((Date) checkOutSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                for (int roomNo : selectedRooms)
                    BookingManager.checkIn(bookings, name, roomNo, checkIn, checkOut);

                JOptionPane.showMessageDialog(dialog, "Check-in done for " + name);
                dialog.dispose();
                refreshTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!");
            }
        });

        JButton cancel = new JButton("Cancel"); cancel.addActionListener(ev -> dialog.dispose());
        dialog.add(submit); dialog.add(cancel);
        dialog.setVisible(true);
    }

    // --- Add Booking dialog ---
    public void showAddBookingDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add Booking", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField();
        JComboBox<String> roomTypeBox = new JComboBox<>(getRoomTypes().toArray(new String[0]));
        DefaultListModel<Integer> roomListModel = new DefaultListModel<>();
        JList<Integer> roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        roomTypeBox.addActionListener(ev -> {
            roomListModel.clear();
            String selectedType = (String) roomTypeBox.getSelectedItem();
            getAvailableRoomsByType(selectedType).forEach(roomListModel::addElement);
        });

        JSpinner checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "dd-MM-yyyy"));
        checkInSpinner.setValue(new Date());
        JSpinner checkOutSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "dd-MM-yyyy"));

        dialog.add(new JLabel("Customer Name:")); dialog.add(nameField);
        dialog.add(new JLabel("Room Type:")); dialog.add(roomTypeBox);
        dialog.add(new JLabel("Select Room(s):")); dialog.add(new JScrollPane(roomList));
        dialog.add(new JLabel("Check-in Date:")); dialog.add(checkInSpinner);
        dialog.add(new JLabel("Check-out Date:")); dialog.add(checkOutSpinner);

        JButton submit = new JButton("Submit");
        submit.addActionListener(e -> {
            try {
                String name = nameField.getText();
                List<Integer> selectedRooms = roomList.getSelectedValuesList();
                LocalDate checkIn = ((Date) checkInSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOut = ((Date) checkOutSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                for (int roomNo : selectedRooms)
                    BookingManager.addBooking(bookings, name, roomNo, checkIn, checkOut);

                JOptionPane.showMessageDialog(dialog, "Booking added for " + name);
                dialog.dispose();
                refreshTables();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input!");
            }
        });

        JButton cancel = new JButton("Cancel"); cancel.addActionListener(ev -> dialog.dispose());
        dialog.add(submit); dialog.add(cancel);
        dialog.setVisible(true);
    }

    // --- Cancel Booking dialog ---
    public void showCancelBookingDialog() {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to cancel:");
        if (bookingId != null && !bookingId.isEmpty()) {
            boolean success = BookingManager.cancelBooking(bookings, bookingId);
            if (success)
                JOptionPane.showMessageDialog(this, "Booking canceled: " + bookingId);
            else
                JOptionPane.showMessageDialog(this, "Booking ID not found: " + bookingId);
            refreshTables();
        }
    }

    // --- Check-Out dialog ---
    public void showCheckOutDialog() {
        String bookingId = JOptionPane.showInputDialog(this, "Enter Booking ID to check-out:");
        if (bookingId != null && !bookingId.isEmpty()) {
            boolean success = BookingManager.checkOut(bookings, bookingId);
            if (success)
                JOptionPane.showMessageDialog(this, "Check-out done for booking: " + bookingId);
            else
                JOptionPane.showMessageDialog(this, "Booking ID not found: " + bookingId);
            refreshTables();
        }
    }
}
