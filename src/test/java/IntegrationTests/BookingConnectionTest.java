package IntegrationTests;

import entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookingService;
import service.CarService;
import service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingConnectionTest {

    BookingService bookingService;
    UserService userService;
    CarService carService;

    User testUser1;
    User testUser2;
    Car testCar1;
    Car testCar2;
    Booking testBooking1;
    Booking testBooking2;
    LocalDate startDate = LocalDate.of(2027, 8, 15);
    LocalDate endDate = LocalDate.of(2027, 10, 20);


    @BeforeEach
    void setUp() {

        TestApplicationContext testInstance = TestApplicationContext.getInstance();

        bookingService = testInstance.getBookingService();
        userService = testInstance.getUserservice();
        carService = testInstance.getCarService();

        List<User> allUsers = userService.getAllUsers();
        testUser1 = allUsers.getFirst();
        testUser2 = allUsers.getLast();


        List<Car> allCars = carService.getAllCars();
        testCar1 = allCars.getFirst();
        testCar2 = allCars.getLast();

        testBooking1 = new Booking(testUser1, testCar1, startDate, endDate);
        testBooking2 = new Booking(testUser2, testCar2, startDate, endDate);

        bookingService.makeBooking(testBooking1);
    }

    @AfterEach
    void tearDown(){
        bookingService.deleteBooking(testBooking1.getId());
        bookingService.deleteBooking(testBooking2.getId());
    }

    @Test
    void testShouldCreateAndSaveBookingSuccessfully() {
        //When
        bookingService.makeBooking(testBooking2);

        List<Booking> allBookings = bookingService.getAllBookings();

        assertNotNull(allBookings);
        Booking result = null;

        for (Booking booking : allBookings){
            if (booking.getId().equals(testBooking2.getId())){
                 result = booking;
                break;
            }
        }

        assertNotNull(result);

        //Then
        assertEquals(testBooking2.getCar(), result.getCar());
        assertEquals(testBooking2.getUser(), result.getUser());
        assertEquals(testBooking2.getStartDate(), result.getStartDate());
        assertEquals(testBooking2.getEndDate(), result.getEndDate());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertEquals(testBooking2.getTotalPrice(), result.getTotalPrice());
    }

    @Test
    void testShouldGetAllBookings(){
        //when
        List<Booking> allBookings = bookingService.getAllBookings();
        assertNotNull(allBookings);

        Booking result = null;

        for (Booking booking : allBookings){
            if (booking.getId().equals(testBooking1.getId())){
                result = booking;
                break;
            }
        }

        assertNotNull(result);

        //Then
        assertEquals(testBooking1.getCar(), result.getCar());
        assertEquals(testBooking1.getUser(), result.getUser());
        assertEquals(testBooking1.getStartDate(), result.getStartDate());
        assertEquals(testBooking1.getEndDate(), result.getEndDate());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertEquals(testBooking1.getTotalPrice(), result.getTotalPrice());
    }

    @Test
    void testCancelBookingWorkflowSuccessful(){
        //Given
        bookingService.makeBooking(testBooking2);

        //check if car is available

        List<Car> allAvailableCars = carService.getAllAvailableCars();

        assertNotNull(allAvailableCars);

        boolean carExists = false;
        for (Car car : allAvailableCars){
            if (car.getId().equals(testCar2.getId())){
                carExists = true;
                break;
            }
        }

        assertFalse(carExists,  "Car should not be available to book");

        List<Booking> allBookings = bookingService.getAllBookings();

        assertNotNull(allBookings);
        Booking result = null;

        for (Booking booking : allBookings){
            if (booking.getId().equals(testBooking2.getId())){
                result = booking;
                break;
            }
        }

        assertNotNull(result);
        assertEquals(testBooking2.getId(), result.getId());

        //when
        bookingService.cancelBooking(testBooking2);

        //then
         allBookings = bookingService.getAllBookings();

        assertNotNull(allBookings);
         result = null;

        for (Booking booking : allBookings){
            if (booking.getId().equals(testBooking2.getId())){
                result = booking;
                break;
            }
        }

        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, result.getStatus(), "Booking Status should be cancelled");

        //check if car is available

       allAvailableCars = carService.getAllAvailableCars();

        assertNotNull(allAvailableCars);

        for (Car car : allAvailableCars){
            if (car.getId().equals(testCar2.getId())){
                carExists = true;
                break;
            }
        }

        assertTrue(carExists,  "Car should be available to book");
    }

    @Test
    void testShouldGetAllUserBookedCarsSuccessful(){

        //when
        List<Car> allUserBookedCars = bookingService.getAllUserBookedCars(testUser1);

        //then
        assertNotNull(allUserBookedCars);
        assertEquals(1, allUserBookedCars.size(), "User should have only one car booked");
        Car result = allUserBookedCars.getFirst();
        assertEquals(result.getId(), testCar1.getId());
    }

    @Test
    void testMakeBookingWithUnknownUserUnsuccessful(){
        //given
        User unknownUser = new User("Unknown User");
        Booking booking = new Booking(unknownUser, testCar2, startDate, endDate);

        //when and then

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.makeBooking(booking));

        assertEquals("User cannot be found, please use an existing user", exception.getMessage() );
    }

    @Test
    void testMakeBookingWithUnknownCarUnsuccessful(){
        //given
        Car unknownCar = new Car("corolla", "testRegistration", new BigDecimal(8), Brand.MERCEDES, false);
        Booking booking = new Booking(testUser2, unknownCar, startDate, endDate);

        //when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.makeBooking(booking));

        assertEquals("Car cannot be found, please use an existing car", exception.getMessage());
    }

    @Test
    void testShouldThrowErrorWhenCarIsAlreadyBooked(){
        //given
        Booking testBooking = new Booking(testUser2, testCar1, startDate, endDate);

        //when and then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> bookingService.makeBooking(testBooking));

        assertEquals("Car is already booked", exception.getMessage());

        //check if previous booking is intact

        List<Booking> allBookings = bookingService.getAllBookings();

        assertNotNull(allBookings);
        Booking result = null;

        for (Booking booking : allBookings){
            if (booking.getId().equals(testBooking1.getId())){
                result = booking;
                break;
            }
        }

        assertNotNull(result);

        //Then
        assertEquals(testBooking1.getCar(), result.getCar());
        assertEquals(testBooking1.getUser(), result.getUser());
        assertEquals(testBooking1.getStartDate(), result.getStartDate());
        assertEquals(testBooking1.getEndDate(), result.getEndDate());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertEquals(testBooking1.getTotalPrice(), result.getTotalPrice());
    }


}