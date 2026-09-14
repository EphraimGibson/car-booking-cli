package persistence;

import entity.Booking;
import entity.Car;
import entity.User;

import java.util.List;
import java.util.UUID;

public interface IPersistence {
    List<User> getAllUsers();
    void setAllUsers(List<User> allUsers);

    List<Car> getAllCars();
    List<Car> getAllAvailableCars();
    List<Car> allElectricCars();
    void setAllCars(List<Car> allCars);

    void createBooking(Booking booking);
    void deleteBooking(UUID id);
    List<Car> getAllCarsUserBooked(User user);
    List<Booking> getAllBookings();
}
