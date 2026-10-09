import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static class Room {
        final int number;
        final String type;
        final double rate;
        boolean booked;

        Room(int number, String type, double rate) {
            this.number = number;
            this.type = type;
            this.rate = rate;
            this.booked = false;
        }
    }

    static class Reservation {
        final int bookingId;
        final String guestName;
        final Room room;
        final int nights;
        final double total;

        Reservation(int bookingId, String guestName, Room room, int nights) {
            this.bookingId = bookingId;
            this.guestName = guestName;
            this.room = room;
            this.nights = nights;
            this.total = room.rate * nights;
        }
    }

    static final Scanner sc = new Scanner(System.in);
    static final ArrayList<Room> rooms = new ArrayList<>();
    static final ArrayList<Reservation> reservations = new ArrayList<>();
    static int nextBookingId = 1001;

    public static void main(String[] args) {
        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(301, "Suite", 4000));

        while (true) {
            System.out.println("\\n=== HOTEL RESERVATION SYSTEM ===");
            System.out.println("1. View rooms");
            System.out.println("2. Search available rooms by type");
            System.out.println("3. Make reservation");
            System.out.println("4. Cancel reservation");
            System.out.println("5. View all reservations");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice = readInt();
            switch (choice) {
                case 1 -> showRooms();
                case 2 -> searchRooms();
                case 3 -> bookRoom();
                case 4 -> cancelBooking();
                case 5 -> showReservations();
                case 6 -> { System.out.println("Thank you."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    static void showRooms() {
        System.out.println("\\n--- ROOM LIST ---");
        for (Room r : rooms) {
            System.out.println("Room " + r.number + " | " + r.type + " | Rs. "
                    + money(r.rate) + "/night | " + (r.booked ? "BOOKED" : "AVAILABLE"));
        }
    }

    static void searchRooms() {
        System.out.print("Room type (Standard/Deluxe/Suite): ");
        String type = sc.nextLine().trim();
        boolean found = false;
        for (Room r : rooms) {
            if (r.type.equalsIgnoreCase(type) && !r.booked) {
                System.out.println("Room " + r.number + " | Rs. " + money(r.rate) + "/night");
                found = true;
            }
        }
        if (!found) System.out.println("No available rooms found for that type.");
    }

    static void bookRoom() {
        showRooms();
        System.out.print("Enter room number to book: ");
        int number = readInt();
        Room selected = findRoom(number);
        if (selected == null) {
            System.out.println("Room does not exist.");
            return;
        }
        if (selected.booked) {
            System.out.println("This room is already booked.");
            return;
        }
        System.out.print("Guest name: ");
        String guest = sc.nextLine().trim();
        if (guest.isEmpty()) {
            System.out.println("Guest name cannot be empty.");
            return;
        }
        System.out.print("Number of nights: ");
        int nights = readInt();
        if (nights <= 0) {
            System.out.println("Number of nights must be at least 1.");
            return;
        }

        Reservation reservation = new Reservation(nextBookingId++, guest, selected, nights);
        selected.booked = true;
        reservations.add(reservation);
        System.out.println("Reservation confirmed. Booking ID: " + reservation.bookingId);
        System.out.println("Total amount: Rs. " + money(reservation.total));
        System.out.println("Payment status: SIMULATED / NOT A REAL PAYMENT");
    }

    static void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");
        int id = readInt();
        for (int i = 0; i < reservations.size(); i++) {
            Reservation res = reservations.get(i);
            if (res.bookingId == id) {
                res.room.booked = false;
                reservations.remove(i);
                System.out.println("Booking cancelled. No real payment was processed.");
                return;
            }
        }
        System.out.println("Booking ID not found.");
    }

    static void showReservations() {
        System.out.println("\\n--- RESERVATIONS ---");
        if (reservations.isEmpty()) {
            System.out.println("No active reservations.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.println("Booking ID: " + r.bookingId + " | Guest: " + r.guestName
                    + " | Room: " + r.room.number + " (" + r.room.type + ")"
                    + " | Nights: " + r.nights + " | Total: Rs. " + money(r.total));
        }
    }

    static Room findRoom(int number) {
        for (Room r : rooms) if (r.number == number) return r;
        return null;
    }

    static String money(double amount) { return String.format("%.2f", amount); }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a whole number: ");
            sc.next();
        }
        int v = sc.nextInt(); sc.nextLine(); return v;
    }
}
