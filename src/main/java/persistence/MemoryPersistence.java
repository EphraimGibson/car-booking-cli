package persistence;

import entity.Booking;
import entity.Car;
import entity.User;

import java.util.*;

public class MemoryPersistence implements IPersistence {

    private List<User> allUsers = new ArrayList<>();
    private List<Car> allCars = new ArrayList<>();
    private final Map<UUID, Booking> bookings = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(this.allUsers);
    }

    @Override
    public void setAllUsers(List<User> allUsers) {
        this.allUsers = new ArrayList<>(allUsers);
    }

    @Override
    public void setAllCars(List<Car> allCars) {
        this.allCars = new ArrayList<>(allCars);
    }

    @Override
    public List<Car> getAllCars() {
        return new ArrayList<>(this.allCars);
    }

    @Override
    public List<Car> getAllAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        List<Car> bookedCars = new ArrayList<>();
        bookings.values().forEach(booking -> bookedCars.add(booking.getCar()));

        for (Car car : allCars) {
            if (!bookedCars.contains(car)) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    @Override
    public List<Car> allElectricCars() {
        List<Car> electricCars = new ArrayList<>();
        for (Car car : allCars) {
            if (car.isElectric()) {
                electricCars.add(car);
            }
        }
        return electricCars;
    }
    @Override
    public void createBooking(Booking booking) {
        for (Booking booking1 : bookings.values()){
            if (booking1.getCar().getId().equals(booking.getCar().getId())){
                throw new IllegalArgumentException("Car is already booked");
            }
        }

        boolean userExists = false;
        for (User existingUser : allUsers) {
            if (Objects.equals(existingUser.getId(), booking.getUser().getId())) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            throw new IllegalArgumentException("User cannot be found, please use an existing user");
        }

        boolean carExists = false;
        for (Car existingCar : allCars) {
            if (Objects.equals(existingCar.getId(), booking.getCar().getId())) {
                carExists = true;
                break;
            }

        }

        if (!carExists) {
            throw new IllegalArgumentException("Car cannot be found, please use an existing car");
        }

        bookings.put(booking.getId(), booking);
    }

    @Override
    public void deleteBooking(UUID id) {
        bookings.remove(id);
    }

    @Override
    public List<Car> getAllCarsUserBooked(User user) {
        List<Car> cars = new ArrayList<>();
        for (Booking booking : bookings.values()) {
            if (booking.getUser().getId().equals(user.getId())) {
                cars.add(booking.getCar());
            }
        }
        return cars;
    }

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
}