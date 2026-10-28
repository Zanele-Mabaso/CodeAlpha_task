package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HotelManager {
    private static final Path FILE = Path.of("bookings.csv");
    private final List<Room> rooms = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private int nextId = 1;

    public HotelManager() {
        for (int i = 101; i <= 103; i++) rooms.add(new Room(i, RoomCategory.STANDARD));
        for (int i = 201; i <= 203; i++) rooms.add(new Room(i, RoomCategory.DELUXE));
        for (int i = 301; i <= 302; i++) rooms.add(new Room(i, RoomCategory.SUITE));
        load();
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    // category == null means "any category"
    public List<Room> searchAvailable(RoomCategory category) {
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable() && (category == null || r.getCategory() == category)) {
                result.add(r);
            }
        }
        return result;
    }

    public Reservation book(String guestName, int roomNumber, int nights) {
        Room room = findRoom(roomNumber);
        if (room == null) throw new IllegalArgumentException("No such room: " + roomNumber);
        if (!room.isAvailable()) throw new IllegalArgumentException("Room " + roomNumber + " is already booked.");

        double total = room.getCategory().getPricePerNight() * nights;
        Reservation res = new Reservation(nextId, guestName, roomNumber, nights, total, false);
        nextId++;
        room.setAvailable(false);
        reservations.add(res);
        save();
        return res;
    }

    public void cancel(int id) {
        Reservation res = findReservation(id);
        if (res == null) throw new IllegalArgumentException("No booking with id " + id);
        findRoom(res.getRoomNumber()).setAvailable(true);
        reservations.remove(res);
        save();
    }

    public void pay(int id) {
        Reservation res = findReservation(id);
        if (res == null) throw new IllegalArgumentException("No booking with id " + id);
        if (res.isPaid()) throw new IllegalArgumentException("Booking " + id + " is already paid.");
        res.setPaid(true);
        save();
    }

    public Reservation findReservation(int id) {
        for (Reservation r : reservations) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    private Room findRoom(int number) {
        for (Room r : rooms) {
            if (r.getNumber() == number) return r;
        }
        return null;
    }

    private void save() {
        List<String> lines = new ArrayList<>();
        for (Reservation r : reservations) lines.add(r.toCsv());
        try {
            Files.write(FILE, lines);
        } catch (IOException e) {
            System.out.println("Could not save bookings: " + e.getMessage());
        }
    }

    private void load() {
        if (!Files.exists(FILE)) return;
        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line.isBlank()) continue;
                try {
                    Reservation r = Reservation.fromCsv(line);
                    reservations.add(r);
                    Room room = findRoom(r.getRoomNumber());
                    if (room != null) room.setAvailable(false);
                    if (r.getId() >= nextId) nextId = r.getId() + 1;
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping bad line in file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not load bookings: " + e.getMessage());
        }
    }
}