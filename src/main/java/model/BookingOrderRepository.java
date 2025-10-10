package model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Mock Database Repository for BookingOrder
public class BookingOrderRepository {
    private final List<BookingOrder> database;

    public BookingOrderRepository() {
        this.database = new ArrayList<>();
        initializeData();
    }

    // Initialize with your table data
    private void initializeData() {
        database.add(new BookingOrder(1001, "BK/000001", 15, 12,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                1200000));
    }

    // Mock Query: SELECT * FROM booking_orders
    public List<BookingOrder> findAll() {
        return new ArrayList<>(database);
    }

    // Mock Query: SELECT * FROM booking_orders WHERE id = ?
    public Optional<BookingOrder> findById(int id) {
        return database.stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    // Mock Query: SELECT * FROM booking_orders WHERE booking_id = ?
    public Optional<BookingOrder> findByBookingId(String bookingId) {
        return database.stream()
                .filter(order -> order.getBookingId().equals(bookingId))
                .findFirst();
    }

    // Mock Query: SELECT * FROM booking_orders WHERE user_id = ?
    public List<BookingOrder> findByUserId(int userId) {
        return database.stream()
                .filter(order -> order.getUserId() == userId)
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE venue_id = ?
    public List<BookingOrder> findByVenueId(int venueId) {
        return database.stream()
                .filter(order -> order.getVenueId() == venueId)
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE date = ?
    public List<BookingOrder> findByDate(LocalDate date) {
        return database.stream()
                .filter(order -> order.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE venue_id = ? AND date = ?
    public List<BookingOrder> findByVenueIdAndDate(int venueId, LocalDate date) {
        return database.stream()
                .filter(order -> order.getVenueId() == venueId)
                .filter(order -> order.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE user_id = ? AND date = ?
    public List<BookingOrder> findByUserIdAndDate(int userId, LocalDate date) {
        return database.stream()
                .filter(order -> order.getUserId() == userId)
                .filter(order -> order.getDate().equals(date))
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE price BETWEEN ? AND ?
    public List<BookingOrder> findByPriceBetween(int minPrice, int maxPrice) {
        return database.stream()
                .filter(order -> order.getPrice() >= minPrice && order.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Mock Query: SELECT * FROM booking_orders WHERE date BETWEEN ? AND ?
    public List<BookingOrder> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return database.stream()
                .filter(order -> !order.getDate().isBefore(startDate) && !order.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Mock Query: INSERT INTO booking_orders
    public void save(BookingOrder order) {
        database.add(order);
    }

    // Mock Query: DELETE FROM booking_orders WHERE id = ?
    public boolean deleteById(int id) {
        return database.removeIf(order -> order.getId() == id);
    }

    // Mock Query: DELETE FROM booking_orders WHERE booking_id = ?
    public boolean deleteByBookingId(String bookingId) {
        return database.removeIf(order -> order.getBookingId().equals(bookingId));
    }

    // Mock Query: UPDATE booking_orders SET price = ? WHERE id = ?
    public boolean updatePrice(int id, int newPrice) {
        Optional<BookingOrder> order = findById(id);
        if (order.isPresent()) {
            order.get().setPrice(newPrice);
            return true;
        }
        return false;
    }

    // Get total count
    public int count() {
        return database.size();
    }

    // Check if booking exists
    public boolean existsByBookingId(String bookingId) {
        return database.stream()
                .anyMatch(order -> order.getBookingId().equals(bookingId));
    }
}
