package experimental;

import model.BookingOrder;
import model.BookingOrderRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public class Ayo2 {

    public static void main(String[] args) {
        BookingOrderRepository repo = new BookingOrderRepository();

        System.out.println("=== Find All Booking Orders ===");
        repo.findAll().forEach(System.out::println);

        System.out.println("\n=== Find By ID (1001) ===");
        repo.findById(1001).ifPresent(System.out::println);

        System.out.println("\n=== Find By Booking ID (BK/000001) ===");
        repo.findByBookingId("BK/000001").ifPresent(System.out::println);

        System.out.println("\n=== Find By User ID (12) ===");
        repo.findByUserId(12).forEach(System.out::println);

        System.out.println("\n=== Find By Venue ID (15) ===");
        repo.findByVenueId(15).forEach(System.out::println);

        System.out.println("\n=== Add New Booking Order ===");
        repo.save(new BookingOrder(1005, "BK/000005", 15, 12,
                LocalDate.of(2022, 12, 10),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                1000000));
        System.out.println("Total orders: " + repo.count());

        System.out.println("\n=== Find By Date (2022-12-10) ===");
        repo.findByDate(LocalDate.of(2022, 12, 10)).forEach(System.out::println);

        System.out.println("\n=== Find By Venue ID (15) and Date (2022-12-11) ===");
        repo.findByVenueIdAndDate(15, LocalDate.of(2022, 12, 11)).forEach(System.out::println);

        System.out.println("\n=== Find By Price Between 1000000 and 1500000 ===");
        repo.findByPriceBetween(1000000, 1500000).forEach(System.out::println);

        System.out.println("\n=== Check If Booking Exists (BK/000001) ===");
        System.out.println("Exists: " + repo.existsByBookingId("BK/000001"));

        System.out.println("\n=== Update Price for ID 1001 ===");
        repo.updatePrice(1001, 1300000);
        repo.findById(1001).ifPresent(System.out::println);

        System.out.println("\n=== Delete By Booking ID (BK/000002) ===");
        repo.deleteByBookingId("BK/000002");
        System.out.println("Total orders after delete: " + repo.count());
    }
}
