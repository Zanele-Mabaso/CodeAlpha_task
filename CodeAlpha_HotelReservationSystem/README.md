# Hotel Reservation System

A Java Swing desktop app for browsing, booking and managing hotel rooms. Bookings are saved to a file, so they are still there after the app is closed and reopened.

I built this as Task 2 of the CodeAlpha Java Programming Internship.



## What it does

- Lists every room and lets you filter by category (Standard, Deluxe, Suite) and availability
- Books a room for a guest and works out the total cost from the nightly rate
- Cancels a booking and frees the room again
- Simulates payment, with no real money involved, and marks the booking as paid
- Shows the full details of any booking
- Saves bookings to `bookings.csv` and reloads them when the app starts
- Rejects bad input with a clear message: double-booking, an empty guest name, or an invalid number of nights

## Built with

Java, Swing for the interface, `java.nio.file` for file handling, and Git/GitHub for version control.

## How to run it

You need JDK 11 or later installed.

```bash
git clone https://github.com/Zanele-Mabaso/CodeAlpha_HotelReservationSystem.git
cd CodeAlpha_HotelReservationSystem
javac -d bin src/Main.java src/model/*.java src/ui/*.java
java -cp bin Main
```

The app creates `bookings.csv` the first time you make a booking.

## Project structure

```
src/
├── Main.java               Starts the application
├── model/
│   ├── RoomCategory.java   Room categories and their nightly price
│   ├── Room.java           A single room and whether it is available
│   ├── Reservation.java    A single booking, with validation and CSV conversion
│   └── HotelManager.java   Booking rules, search, saving and loading
└── ui/
    └── HotelGUI.java       The Swing window
```

## Design choices

**Model and interface are kept apart.** The GUI only shows data and responds to clicks. Rules such as "a booked room can't be booked again" live in `HotelManager`, so the window never has to decide what is allowed.

**Validation happens in one place.** `Reservation` checks the guest name and number of nights in its constructor, so invalid data is rejected no matter where a booking comes from.

**Room categories are an enum.** Each category carries its own price, so adding a new category means adding one line to `RoomCategory`.

**Availability comes from the bookings.** When the app starts, any room with a saved booking is marked as booked. This keeps a single source of truth instead of storing availability separately.

## Testing

I tested the app manually with these scenarios:

| Scenario | Result |
|---|---|
| Book an available room | Booking is created and the room shows as booked |
| Book a room that is already booked | Error message, nothing changes |
| Enter text or zero as the number of nights | Error message |
| Leave the guest name empty | Error message |
| Pay for a booking, then cancel it | Booking shows as paid, then the room becomes available again |
| Close and reopen the app | Bookings are still there |

## What I learned

This project taught me why it helps to keep the interface and the rules separate. When the GUI only displays things and asks `HotelManager` to do the work, a change to the rules doesn't mean rewriting the window. I also learned how a program can save its data to a file and rebuild its state from that file on startup. On the tooling side, I set up the repository from the command line, fixed a branch name mismatch, and learned to keep runtime files like `bookings.csv` out of version control. The hardest part was understanding how the pieces connect: a button click goes to a method in the GUI, which calls the manager, which updates the data and the file, after which the tables refresh.


## Ideas for next steps

- Check-in and check-out dates instead of a number of nights
- A database such as SQLite or SQL Server  instead of a CSV file
- Unit tests with JUnit
- Guest accounts and booking history

## Author

**Zanele Mabaso**
