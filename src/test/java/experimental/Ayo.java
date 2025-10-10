package experimental;

import model.BookingSlot;
import model.BookingSlotRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ayo {

    public static void main(String[] args) {
        BookingSlotRepository repo = new BookingSlotRepository();

        System.out.println("=== Find All ===");
        repo.findAll().forEach(System.out::println);

        System.out.println("\n=== Find By ID (12) ===");
        repo.findById(12).ifPresent(System.out::println);

        System.out.println("\n=== Find By Venue ID (15) ===");
        repo.findByVenueId(15).forEach(System.out::println);

        System.out.println("\n=== Find By Price > 900000 ===");
        repo.findByPriceGreaterThan(900000).forEach(System.out::println);

        System.out.println("\n=== Add New Slot ===");
        repo.save(new BookingSlot(14, 15,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                1500000));
        System.out.println("Total slots: " + repo.findAll().size());

        System.out.println("\n=== Update Price for ID 11 ===");
        repo.updatePrice(11, 850000);
        repo.findById(11).ifPresent(System.out::println);

        System.out.println("\n=== Delete ID 14 ===");
        repo.deleteById(14);
        System.out.println("Total slots after delete: " + repo.findAll().size());
    }
}

