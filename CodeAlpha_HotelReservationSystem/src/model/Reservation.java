package model;

public class Reservation {
    private final int id;
    private final String guestName;
    private final int roomNumber;
    private final int nights;
    private final double totalCost;
    private boolean paid;

    public Reservation(int id, String guestName, int roomNumber, int nights,
                       double totalCost, boolean paid) {
        if (guestName == null || guestName.isBlank() || guestName.contains(",")) {
            throw new IllegalArgumentException("Guest name cannot be empty or contain commas.");
        }
        if (nights <= 0) {
            throw new IllegalArgumentException("Nights must be at least 1.");
        }
        this.id = id;
        this.guestName = guestName.trim();
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.totalCost = totalCost;
        this.paid = paid;
    }

    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public int getRoomNumber() { return roomNumber; }
    public int getNights() { return nights; }
    public double getTotalCost() { return totalCost; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String toCsv() {
        return id + "," + guestName + "," + roomNumber + "," + nights + "," + totalCost + "," + paid;
    }

    public static Reservation fromCsv(String line) {
        String[] p = line.split(",");
        if (p.length != 6) {
            throw new IllegalArgumentException("Bad line: " + line);
        }
        return new Reservation(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]),
                Integer.parseInt(p[3]), Double.parseDouble(p[4]), Boolean.parseBoolean(p[5]));
    }

    @Override
    public String toString() {
        return "Booking #" + id + " | Guest: " + guestName + " | Room: " + roomNumber
                + " | Nights: " + nights + " | Total: $" + totalCost
                + " | " + (paid ? "PAID" : "UNPAID");
    }
}