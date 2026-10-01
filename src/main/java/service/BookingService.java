package service;

import entity.Booking;
import entity.BookingStatus;
import entity.Car;
import entity.User;
import persistence.IPersistence;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private final IPersistence persistence;

    public BookingService(IPersistence persistence) {
        this.persistence = persistence;
    }

    public void makeBooking(Booking booking) {
        if (booking.getUser() == null ) {
            throw new IllegalArgumentException("Invalid user for booking");
        }

        if (booking.getCar() == null ){
            throw new IllegalArgumentException("Invalid car for booking");
        }

        if (booking.getStartDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        booking.setStatus(BookingStatus.ACTIVE);
        persistence.createBooking(booking);
    }

    public void cancelBooking(Booking booking){
        persistence.cancelBooking(booking.getId());
    }

    public List<Car> getAllUserBookedCars(User user){
        return persistence.getAllCarsUserBooked(user);
    }

    public List<Booking> getAllBookings() {
        return persistence.getAllBookings();
    }

    public void deleteBooking(UUID id){
        persistence.deleteBooking(id);
    }

}