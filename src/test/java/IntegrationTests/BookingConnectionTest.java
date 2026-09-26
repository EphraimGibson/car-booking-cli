package IntegrationTests;

import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookingService;
import service.CarService;
import service.UserService;

import java.time.LocalDate;
import java.util.List;

class BookingConnectionTest {

    BookingService bookingService;
    UserService userService;
    CarService carService;

    User testUser;

    Car testCar;

    @BeforeEach
    void setUp() {

        TestApplicationContext testInstance = TestApplicationContext.getInstance();

        bookingService = testInstance.getBookingService();
        userService = testInstance.getUserservice();
        carService = testInstance.getCarService();

        List<User> allUsers = userService.getAllUsers();
        testUser = allUsers.getFirst();

        List<Car> allCars = carService.getAllCars();

        testCar = allCars.getFirst();

    }

    @AfterEach
    void tearDown(){
        BookingFacade.deleteBookingByCarId(testCar.getId(), bookingService);
    }

    @Test
    void testShouldCreateAndSaveBookingSuccessfully() {
        //Given
        LocalDate startDate = LocalDate.of(2027, 8, 15);
        LocalDate endDate = LocalDate.of(2027, 10, 20);

        Booking booking = new Booking(testUser, testCar, startDate, endDate);

        //When
        bookingService.makeBooking(booking);

        List<Booking> allBookings = bookingService.getAllBookings();

        Assertions.assertNotNull(allBookings);
        Assertions.assertEquals(1, allBookings.size());
        Booking result = allBookings.getFirst();

        //Then
        Assertions.assertEquals(booking.getCar(), result.getCar());
        Assertions.assertEquals(booking.getUser(), result.getUser());
        Assertions.assertEquals(booking.getStartDate(), result.getStartDate());
        Assertions.assertEquals(booking.getEndDate(), result.getEndDate());
        Assertions.assertEquals(BookingStatus.ACTIVE, result.getStatus());
        Assertions.assertEquals(booking.getTotalPrice(), result.getTotalPrice());
    }

}