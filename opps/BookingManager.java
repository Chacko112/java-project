
package opps;

import java.time.LocalDate;
import java.util.List;

public class BookingManager {

    public static void addBooking(List<OperationsPanel.Booking> bookings, String customerName,
                                  int roomNo, LocalDate checkIn, LocalDate checkOut) {
        String bookingId = "B" + (bookings.size() + 1);
        OperationsPanel.Booking booking = new OperationsPanel.Booking(bookingId, customerName, roomNo, checkIn, checkOut, LocalDate.now());
        bookings.add(booking);
        System.out.println("Booking added: " + bookingId + " for " + customerName);
    }

    public static boolean cancelBooking(List<OperationsPanel.Booking> bookings, String bookingId) {
        for (OperationsPanel.Booking b : bookings) {
            if (b.bookingId.equals(bookingId)) {
                bookings.remove(b);
                System.out.println("Booking canceled: " + bookingId);
                return true;
            }
        }
        System.out.println("Booking ID not found: " + bookingId);
        return false;
    }

    public static void checkIn(List<OperationsPanel.Booking> bookings, String customerName,
                               int roomNo, LocalDate checkIn, LocalDate checkOut) {
        // For simplicity, check-in is just adding a booking
        addBooking(bookings, customerName, roomNo, checkIn, checkOut);
        System.out.println("Checked in: " + customerName + " Room: " + roomNo);
    }

    public static boolean checkOut(List<OperationsPanel.Booking> bookings, String bookingId) {
        // For simplicity, check-out is just canceling the booking
        boolean result = cancelBooking(bookings, bookingId);
        if (result)
            System.out.println("Checked out booking: " + bookingId);
        return result;
    }
}