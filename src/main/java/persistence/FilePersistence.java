package persistence;

import entity.*;
import utils.FileReader;
import utils.FileWriter;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class FilePersistence implements IPersistence {
    private final FileWriter bookingFileWriter;
    private final FileReader fileReader;
    private List<Car> allCars;
    private List<User> allUsers;

    private Map<UUID, Booking> bookings;

    public FilePersistence(FileReader pFileReader, FileWriter pBookingWriter) {
        fileReader = pFileReader;
        bookingFileWriter = pBookingWriter;
    }

    @Override
    public List<User> getAllUsers() {
        if (allUsers != null) {
            return new ArrayList<>(allUsers);
        } else {
            throw new IllegalStateException("Users have not been seeded into the system");
        }
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
        if (allCars != null) {
            return new ArrayList<>(allCars);
        } else {
            throw new IllegalStateException("Cars have not been seeded into the system");
        }
    }

    @Override
    public List<Car> getAllAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        Set<UUID> bookedCarIds = new HashSet<>();

        getAllBookings().forEach(booking -> {
            if (booking.getStatus() == BookingStatus.ACTIVE ){
                bookedCarIds.add(booking.getCar().getId());
            }
        });

        for (Car car : getAllCars()) {
            if (!bookedCarIds.contains(car.getId())){
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    @Override
    public List<Car> allElectricCars() {
        List<Car> cars = getAllCars();
        List<Car> electricCars = new ArrayList<>();

        for (Car car : cars) {
            if (car.isElectric()) {
                electricCars.add(car);
            }
        }
        return electricCars;
    }

    @Override
    public void createBooking(Booking booking) {

        for (Booking existingBooking : getAllBookings()) {
            if (existingBooking.getCar().equals(booking.getCar()) && existingBooking.getStatus().equals(BookingStatus.ACTIVE)) {
                throw new IllegalArgumentException("Car is already booked");
            }
        }

        boolean userExists = false;
        for (User existingUser : getAllUsers()) {
            if (existingUser.equals(booking.getUser())) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            throw new IllegalArgumentException("User cannot be found, please use an existing user");
        }

        boolean carExists = false;
        for (Car existingCar : getAllCars()) {
            if (existingCar.equals(booking.getCar())) {
                carExists = true;
                break;
            }

        }

        if (!carExists) {
            throw new IllegalArgumentException("Car cannot be found, please use an existing car");
        }

        String bookingCsvFormat = createBookingCsvFormat(booking);

        try {
            bookingFileWriter.writeLineToFile(bookingCsvFormat);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save booking on file ", e);
        }

        bookings.put(booking.getId(), booking);
    }

    private String createBookingCsvFormat(Booking booking) {

        return booking.getId() + "," +
                booking.getUser().getId() + "," +
                booking.getCar().getId() + "," +
                booking.getStartDate() + "," +
                booking.getEndDate() + "," +
                booking.getStatus() + "," +
                booking.getTotalPrice() + "," +
                booking.getCreatedOn();
    }

    @Override
    public void cancelBooking(UUID id) {
        getAllBookings();
        Booking bookingToCancel = bookings.get(id);

        if (bookingToCancel != null){
            bookingToCancel.setStatus(BookingStatus.CANCELLED);
        }
        else{
            return;
        }
        updateBookingFile();
    }

    @Override
    public List<Car> getAllCarsUserBooked(User user) {

        List<Car> cars = new ArrayList<>();
        for (Booking booking : getAllBookings()) {
            if (booking.getUser().equals(user)) {
                cars.add(booking.getCar());
            }
        }
        return cars;
    }

    @Override
    public List<Booking> getAllBookings() {
        if (bookings != null) {
            return new ArrayList<>(bookings.values());
        }

        List<String> allBookingFromFile;
        Map<UUID, Booking> bookingMap = new LinkedHashMap<>();

        try {
            allBookingFromFile = fileReader.readFile(bookingFileWriter.getPath());
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to retrieve booking from file", e);
        }

        if (!allBookingFromFile.isEmpty()) {
            for (String line : allBookingFromFile) {
                Booking bookingFromLine = createBookingFromLine(line);

                bookingMap.put(bookingFromLine.getId(), bookingFromLine);
            }
        }

        this.bookings = bookingMap;

        return new ArrayList<>(bookingMap.values());
    }

    private Booking createBookingFromLine(String line) {
        String[] bookingField = line.split(",");

        String bookingId = bookingField[0];

        User user = null;

        for (User existingUser : getAllUsers()) {
            if (Objects.equals(existingUser.getId().toString(), bookingField[1])) {
                user = existingUser;
                break;
            }
        }
        if (user == null) {
            throw new IllegalArgumentException("can't find the user in the booking file");
        }

        Car car = null;

        for (Car existingCar : getAllCars()) {
            if (Objects.equals(existingCar.getId().toString(), bookingField[2])) {
                car = existingCar;
                break;
            }
        }
        if (car == null) {
            throw new IllegalArgumentException("can't find the car in booking file");
        }

        String[] startDateSplit = bookingField[3].split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(startDateSplit[0]), Integer.parseInt(startDateSplit[1]), Integer.parseInt(startDateSplit[2]));
        String[] endDateSplit = bookingField[4].split("-");
        LocalDate endDate = LocalDate.of(Integer.parseInt(endDateSplit[0]), Integer.parseInt(endDateSplit[1]), Integer.parseInt(endDateSplit[2]));

        BookingStatus status;
        try {
            status = BookingStatus.valueOf(bookingField[5]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Booking status cannot be identified", e);
        }

        BigDecimal totalPrice = new BigDecimal(bookingField[6]);

        String[] createdOnSplit = bookingField[7].split("-");
        LocalDate createdOn = LocalDate.of(Integer.parseInt(createdOnSplit[0]), Integer.parseInt(createdOnSplit[1]), Integer.parseInt(createdOnSplit[2]));

        return new Booking(UUID.fromString(bookingId), user, car, startDate, endDate, status, totalPrice, createdOn);
    }

    @Override
    public void deleteBooking(UUID id) {
        getAllBookings();
        bookings.remove(id);

        updateBookingFile();
    }

    private void updateBookingFile() {
        List<String> bookingsToCsvList = new ArrayList<>();

        bookings.values().forEach(booking -> bookingsToCsvList.add(createBookingCsvFormat(booking)));

        try {
            bookingFileWriter.writeListToFile(bookingsToCsvList);

        } catch (IOException e) {
            throw new UncheckedIOException("Cannot update booking file", e);
        }
    }

}