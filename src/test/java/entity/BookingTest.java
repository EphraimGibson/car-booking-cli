package entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

class BookingTest {

    @Test
    void testCreateBookingSuccessful() {
        User testUser = new User("John");
        Car testCar = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);
        LocalDate startDate = LocalDate.of(2026, 1, 8);
        LocalDate endDate = LocalDate.of(2026, 1, 15);

        Booking testBooking = new Booking(testUser, testCar, startDate, endDate, BookingStatus.ACTIVE);

        Assertions.assertSame(testUser, testBooking.getUser());
        Assertions.assertSame(testCar, testBooking.getCar());
        Assertions.assertEquals(startDate, testBooking.getStartDate());
        Assertions.assertEquals(endDate, testBooking.getEndDate());
        Assertions.assertEquals(BookingStatus.ACTIVE, testBooking.getStatus());
    }

    @Test
    void testShouldCalculateTotalPriceAccurately() {
        User testUser = new User("John");
        Car testCar = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);
        LocalDate startDate = LocalDate.of(2026, 1, 8);
        LocalDate endDate = LocalDate.of(2026, 1, 15);

        Booking testBooking = new Booking(testUser, testCar, startDate, endDate, BookingStatus.ACTIVE);

        Assertions.assertEquals(BigDecimal.valueOf(414.61), testBooking.getTotalPrice());
    }

    @Test
    void testCreateBookingWithInvalidStartDateUnsuccessful() {
        User user = new User("John");
        Car car = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Booking(user, car, null, LocalDate.of(2026, 1, 15))
        );

        Assertions.assertEquals("Start date cannot be null", exception.getMessage());
    }

    @Test
    void testCreateBookingWithInvalidEndDateUnsuccessful() {
        User user = new User("John");
        Car car = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Booking(user, car, LocalDate.of(2026, 1, 8), null)
        );

        Assertions.assertEquals("End date cannot be null", exception.getMessage());
    }

    @Test
    void testCreateBookingWithInvalidUserThrowsError() {
        Car car = new Car("Benz", "A183jdn", BigDecimal.valueOf(59.23), Brand.MERCEDES, false);

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Booking(null, car, LocalDate.of(2026, 1, 8), LocalDate.of(2026, 1, 15))
        );

        Assertions.assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testCreateBookingWithInvalidCarThrowsError() {
        User user = new User("John");

        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Booking(user, null, LocalDate.of(2026, 1, 8), LocalDate.of(2026, 1, 15))
        );

        Assertions.assertEquals("Car cannot be null", exception.getMessage());
    }

}