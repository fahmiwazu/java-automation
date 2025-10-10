package model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Mock Database Repository
public class BookingSlotRepository {
    private final List<BookingSlot> database;

    public BookingSlotRepository() {
        this.database = new ArrayList<>();
        initializeData();
    }

    // Initialize with your table data
    private void initializeData() {
        database.add(new BookingSlot(11, 15,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(7, 0),
                LocalTime.of(9, 0),
                800000));

        database.add(new BookingSlot(12, 15,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                1000000));

        database.add(new BookingSlot(13, 15,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(11, 0),
                LocalTime.of(13, 0),
                1200000));
    }

    // Mock Query: SELECT * FROM booking_slots
    public List<BookingSlot> findAll() {
        return new ArrayList<>(database);
    }

    // Mock Query: SELECT * FROM booking_slots WHERE id = ?
    public Optional<BookingSlot> findById(int id) {
        return database.stream()
                .filter(slot -> slot.getId() == id)
                .findFirst();
    }

    // Mock Query: SELECT * FROM booking_slots WHERE venue_id = ?
    public List<BookingSlot> findByVenueId(int venueId) {
        return database.stream()
                .filter(slot -> slot.getVenueId() == venueId)
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_slots WHERE date = ?
    public List<BookingSlot> findByDate(LocalDate date) {
        return database.stream()
                .filter(slot -> slot.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_slots WHERE price > ?
    public List<BookingSlot> findByPriceGreaterThan(int price) {
        return database.stream()
                .filter(slot -> slot.getPrice() > price)
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_slots WHERE venue_id = ? AND date = ?
    public List<BookingSlot> findByVenueIdAndDate(int venueId, LocalDate date) {
        return database.stream()
                .filter(slot -> slot.getVenueId() == venueId)
                .filter(slot -> slot.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Mock Query: INSERT INTO booking_slots
    public void save(BookingSlot slot) {
        database.add(slot);
    }

    // Mock Query: DELETE FROM booking_slots WHERE id = ?
    public boolean deleteById(int id) {
        return database.removeIf(slot -> slot.getId() == id);
    }

    // Mock Query: UPDATE booking_slots SET price = ? WHERE id = ?
    public boolean updatePrice(int id, int newPrice) {
        Optional<BookingSlot> slot = findById(id);
        if (slot.isPresent()) {
            slot.get().setPrice(newPrice);
            return true;
        }
        return false;
    }
}

