package service;

import entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.IPersistence;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    IPersistence persistenceMock;
    @InjectMocks
    BookingService bookingService;

    @Test
    void testMakeBookingSuccessful() {
        // Given
        User testUser = new User("John");

        Car testCar = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        Booking booking = new Booking(testUser, testCar, LocalDate.of(2027, 1, 8), LocalDate.of(2027, 1, 15));

        doNothing().when(persistenceMock).createBooking(booking);

        // When
        bookingService.makeBooking(booking);

        // Then
        Assertions.assertNotNull(booking.getStatus());
        Assertions.assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }

    @Test
    void testGetAllBookingsSuccessfully() {
        //Given
        List<Booking> testBookings = List.of(new Booking(), new Booking());
        when(persistenceMock.getAllBookings()).thenReturn(testBookings);

        //When
        List<Booking> result = bookingService.getAllBookings();

        //Then
        Assertions.assertEquals(testBookings.size(), result.size(), "Expected number of bookings does not match");
        Assertions.assertSame(testBookings, result);

        verify(persistenceMock, times(1)).getAllBookings();

    }

    @Test
    void testMakeBookingShouldThrowErrorWhenStartDateIsInThePast(){
        // Given
        User testUser = new User("John");

        Car testCar = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        Booking booking = new Booking(testUser, testCar, LocalDate.of(2026, 1, 8), LocalDate.of(2026, 1, 15));

        // When & Then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bookingService.makeBooking(booking));
        Assertions.assertEquals("Start date cannot be in the past", exception.getMessage());
    }
}