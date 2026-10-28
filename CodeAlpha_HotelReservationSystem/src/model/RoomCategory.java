package model;

public enum RoomCategory {
    STANDARD(80.0), DELUXE(120.0), SUITE(200.0);

    private final double pricePerNight;

    RoomCategory(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}