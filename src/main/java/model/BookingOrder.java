package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

// BookingOrder POJO Class
@Setter
@Getter
public class BookingOrder {
    // Setters
    // Getters
    private int id;
    private String bookingId;
    private int venueId;
    private int userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int price;

    public BookingOrder(int id, String bookingId, int venueId, int userId,
                        LocalDate date, LocalTime startTime, LocalTime endTime, int price) {
        this.id = id;
        this.bookingId = bookingId;
        this.venueId = venueId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("BookingOrder{id=%d, bookingId='%s', venueId=%d, userId=%d, date=%s, startTime=%s, endTime=%s, price=%d}",
                id, bookingId, venueId, userId, date, startTime, endTime, price);
    }
}
