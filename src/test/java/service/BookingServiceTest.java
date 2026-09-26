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
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    IPersistence persistenceMock;
    @InjectMocks
    BookingService bookingService;

    User testUser = new User("John");

    Car testCar = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

    LocalDate startDate = LocalDate.of(2027, 1, 8);
    LocalDate endDate = LocalDate.of(2027, 1, 15);
    Booking booking = new Booking(testUser, testCar, startDate, endDate );


    @Test
    void testMakeBookingSuccessful() {
        // Given
        doNothing().when(persistenceMock).createBooking(booking);

        // When
        bookingService.makeBooking(booking);

        // Then
        Assertions.assertNotNull(booking.getStatus());
        Assertions.assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }

    @Test
    void testBookingPriceIsCalculatedCorrectly() {
        // Given
        doNothing().when(persistenceMock).createBooking(booking);

        BigDecimal priceOfCarPerDay = testCar.getPricePerDay();
        int numberOfDaysBooked = Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate));

        BigDecimal totalPrice = priceOfCarPerDay.multiply(new BigDecimal(numberOfDaysBooked));

        // When
        bookingService.makeBooking(booking);

        // Then
        Assertions.assertNotNull(booking.getStatus());
        Assertions.assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        Assertions.assertEquals(totalPrice, booking.getTotalPrice(), "total price of booking is incorrect");
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
        User testUser5 = new User("John");

        Car testCar5 = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        Booking booking5 = new Booking(testUser5, testCar5, LocalDate.of(2026, 1, 8), LocalDate.of(2026, 1, 15));

        // When & Then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> bookingService.makeBooking(booking5));
        Assertions.assertEquals("Start date cannot be in the past", exception.getMessage());
    }

    @Test
    void testShouldDeleteBookingSuccessfully() {
        //given
       doNothing().when(persistenceMock).deleteBooking(booking.getId());

       //when
        bookingService.deleteBooking(booking);

        //then
        verify(persistenceMock).deleteBooking(booking.getId());
    }

    @Test
    void testShouldGetAllUserBookedCars() {
        //given
        when(persistenceMock.getAllCarsUserBooked(testUser)).thenReturn(List.of());

        //when
        bookingService.getAllUserBookedCars(testUser);

        //then
        verify(persistenceMock).getAllCarsUserBooked(testUser);

    }

    @Test
    void testShouldGetAllBookings() {
        //given
        when(persistenceMock.getAllBookings()).thenReturn(List.of());

        //when
        bookingService.getAllBookings();

        //then
        verify(persistenceMock).getAllBookings();

    }
}