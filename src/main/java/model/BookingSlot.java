package model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


// POJO Class
@Setter
@Getter
public class BookingSlot {
    // Setters
    // Getters
    private int id;
    private int venueId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int price;

    public BookingSlot(int id, int venueId, LocalDate date,
                       LocalTime startTime, LocalTime endTime, int price) {
        this.id = id;
        this.venueId = venueId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("BookingSlot{id=%d, venueId=%d, date=%s, startTime=%s, endTime=%s, price=%d}",
                id, venueId, date, startTime, endTime, price);
    }
}
