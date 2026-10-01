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
            if (booking.getCar().getId().equals(id)){
                allBookings.remove(booking);
                break;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("Bookings.csv"))){
            for (Booking booking : allBookings){

                String bookingCsvFormat = booking.getId() + "," +
                        booking.getUser().getId() + "," +
                        booking.getCar().getId() + "," +
                        booking.getStartDate() + "," +
                        booking.getEndDate() + "," +
                        booking.getStatus() + "," +
                        booking.getTotalPrice() + "," +
                        booking.getCreatedOn() + ",";

                writer.write(bookingCsvFormat);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot update booking file", e);
        }
    }
}