package IntegrationTests;

import entity.Booking;
import service.BookingService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class BookingFacade {

    public static void deleteBookingByCarId(UUID id, BookingService bookingService) {
        List<Booking> allBookings = bookingService.getAllBookings();

        for (Booking booking : allBookings){
            if (booking.getCar().getId() == id){
                allBookings.remove(booking);
                break;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("Bookings.csv"))){
            for (Booking booking : allBookings){
                StringBuilder bookingCsvFormat = new StringBuilder();

                bookingCsvFormat.append(booking.getId()).append(",");
                bookingCsvFormat.append(booking.getUser().getId()).append(",");
                bookingCsvFormat.append(booking.getCar().getId()).append(",");
                bookingCsvFormat.append(booking.getStartDate()).append(",");
                bookingCsvFormat.append(booking.getEndDate()).append(",");
                bookingCsvFormat.append(booking.getStatus()).append(",");
                bookingCsvFormat.append(booking.getTotalPrice()).append(",");
                bookingCsvFormat.append(booking.getCreatedOn()).append(",");

                writer.write(bookingCsvFormat.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot update booking file", e);
        }
    }
}