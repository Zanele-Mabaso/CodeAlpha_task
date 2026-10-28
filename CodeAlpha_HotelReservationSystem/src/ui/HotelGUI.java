package ui;

import model.HotelManager;
import model.Reservation;
import model.Room;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HotelGUI extends JFrame {
    private final HotelManager hotel = new HotelManager();

    private final DefaultTableModel roomModel = new DefaultTableModel(
            new String[]{"Room", "Category", "Price/night", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel bookingModel = new DefaultTableModel(
            new String[]{"ID", "Guest", "Room", "Nights", "Total", "Payment"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    private final JTable roomTable = new JTable(roomModel);
    private final JTable bookingTable = new JTable(bookingModel);
    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{"ALL", "STANDARD", "DELUXE", "SUITE"});
    private final JCheckBox availableOnly = new JCheckBox("Available only");
    private final JTextField nameField = new JTextField(12);
    private final JTextField nightsField = new JTextField("1", 4);

    public HotelGUI() {
        super("Hotel Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1));

        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(buildRoomPanel());
        add(buildBookingPanel());

        categoryBox.addActionListener(e -> refreshRooms());
        availableOnly.addActionListener(e -> refreshRooms());

        refreshRooms();
        refreshBookings();
    }

    private JPanel buildRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Rooms"));

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filters.add(new JLabel("Category:"));
        filters.add(categoryBox);
        filters.add(availableOnly);

        JPanel bookForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bookButton = new JButton("Book selected room");
        bookButton.addActionListener(e -> bookSelectedRoom());
        bookForm.add(new JLabel("Guest name:"));
        bookForm.add(nameField);
        bookForm.add(new JLabel("Nights:"));
        bookForm.add(nightsField);
        bookForm.add(bookButton);

        panel.add(filters, BorderLayout.NORTH);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);
        panel.add(bookForm, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Bookings"));

        JButton payButton = new JButton("Pay (simulated)");
        JButton cancelButton = new JButton("Cancel booking");
        JButton detailsButton = new JButton("View details");
        payButton.addActionListener(e -> paySelected());
        cancelButton.addActionListener(e -> cancelSelected());
        detailsButton.addActionListener(e -> showDetails());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(payButton);
        buttons.add(cancelButton);
        buttons.add(detailsButton);

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshRooms() {
        roomModel.setRowCount(0);
        String chosen = (String) categoryBox.getSelectedItem();
        for (Room r : hotel.getAllRooms()) {
            boolean categoryOk = "ALL".equals(chosen) || r.getCategory().name().equals(chosen);
            boolean availableOk = !availableOnly.isSelected() || r.isAvailable();
            if (categoryOk && availableOk) {
                roomModel.addRow(new Object[]{
                        r.getNumber(),
                        r.getCategory(),
                        "$" + r.getCategory().getPricePerNight(),
                        r.isAvailable() ? "Available" : "Booked"});
            }
        }
    }

    private void refreshBookings() {
        bookingModel.setRowCount(0);
        for (Reservation r : hotel.getAllReservations()) {
            bookingModel.addRow(new Object[]{
                    r.getId(), r.getGuestName(), r.getRoomNumber(), r.getNights(),
                    String.format("$%.2f", r.getTotalCost()),
                    r.isPaid() ? "PAID" : "UNPAID"});
        }
    }

    private void bookSelectedRoom() {
        int row = roomTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a room in the table first.");
            return;
        }
        int roomNumber = (int) roomModel.getValueAt(row, 0);
        try {
            int nights = Integer.parseInt(nightsField.getText().trim());
            Reservation res = hotel.book(nameField.getText(), roomNumber, nights);
            JOptionPane.showMessageDialog(this, "Booked!\n" + res);
            nameField.setText("");
            nightsField.setText("1");
        } catch (NumberFormatException e) {
            showError("Nights must be a whole number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
        refreshRooms();
        refreshBookings();
    }

    private Integer selectedBookingId() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) {
            showError("Please select a booking in the table first.");
            return null;
        }
        return (int) bookingModel.getValueAt(row, 0);
    }

    private void paySelected() {
        Integer id = selectedBookingId();
        if (id == null) return;
        try {
            hotel.pay(id);
            JOptionPane.showMessageDialog(this, "Payment successful (simulated).");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
        refreshBookings();
    }

    private void cancelSelected() {
        Integer id = selectedBookingId();
        if (id == null) return;
        int answer = JOptionPane.showConfirmDialog(this,
                "Cancel booking #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;
        try {
            hotel.cancel(id);
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
        refreshRooms();
        refreshBookings();
    }

    private void showDetails() {
        Integer id = selectedBookingId();
        if (id == null) return;
        Reservation r = hotel.findReservation(id);
        JOptionPane.showMessageDialog(this, r == null ? "No such booking." : r.toString());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}