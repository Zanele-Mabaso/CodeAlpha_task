package model;

public class Room {
    private final int number;
    private final RoomCategory category;
    private boolean available = true;

    public Room(int number, RoomCategory category) {
        this.number = number;
        this.category = category;
    }

    public int getNumber() { return number; }
    public RoomCategory getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Room " + number + " [" + category + "] $" + category.getPricePerNight()
                + "/night - " + (available ? "Available" : "Booked");
    }
}