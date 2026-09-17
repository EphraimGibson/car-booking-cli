package persistence;

import entity.*;
import utils.FileReader;
import utils.FileWriter;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class FilePersistence implements IPersistence {
    public static final String BOOKINGS_CSV_FIlENAME = "Bookings.csv";
    private final FileWriter bookingWriter;
    private final FileReader fileReader;
    private List<Car> allCars;
    private List<User> allUsers;

    private List<Booking> bookings;

    public FilePersistence() {
        this.bookingWriter = new FileWriter(Path.of(BOOKINGS_CSV_FIlENAME));
        fileReader = new FileReader();
    }

    @Override
    public List<User> getAllUsers() {
        if (allUsers != null) {
            return new ArrayList<>(allUsers);
        }

        List<String> allUsersFromFile;
        List<User> users = new ArrayList<>();

        try {
            allUsersFromFile = fileReader.readFile(Path.of("Users.csv"));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to retrieve users from file", e);
        }

        if (!allUsersFromFile.isEmpty()) {
            //skip car csv header

            for (String line : allUsersFromFile.subList(1, allUsersFromFile.size())) {
                String[] userLineSplit = line.split(",");

                users.add(new User(UUID.fromString(userLineSplit[0]), userLineSplit[1]));
            }
        }
        this.allUsers = users;


        return new ArrayList<>(users);
    }

    @Override
    public void setAllUsers(List<User> allUsers) {
        // data comes from file
    }

    @Override
    public List<Car> getAllCars() {

        if (allCars != null) {
            return new ArrayList<>(allCars);
        }

        List<String> allCarsFromFile;
        List<Car> cars = new ArrayList<>();

        try {
            allCarsFromFile = fileReader.readFile(Path.of("Cars.csv"));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to retrieve cars from file", e);
        }

        if (!allCarsFromFile.isEmpty()) {
            //skip car csv header
            for (String line : allCarsFromFile.subList(1, allCarsFromFile.size())) {
                String[] carFields = line.split(",");

                UUID carId = UUID.fromString(carFields[0]);
                String model = carFields[1];
                String registration = carFields[2];
                BigDecimal price = new BigDecimal(carFields[3]);
                boolean isElectric = Boolean.parseBoolean(carFields[5]);

                Brand brand;

                try {
                    brand = Brand.valueOf(carFields[4]);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Car brand cannot be identified", e);
                }

                cars.add(new Car(carId, model, registration, price, brand, isElectric));
            }
        }

        this.allCars = cars;

        return new ArrayList<>(cars);
    }

    @Override
    public List<Car> getAllAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        List<Car> bookedCars = new ArrayList<>();
        getAllBookings().forEach(booking -> bookedCars.add(booking.getCar()));

        for (Car car : getAllCars()) {
            if (!bookedCars.contains(car)) {
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
    public void setAllCars(List<Car> allCars) {
        //data comes from file
    }

    @Override
    public void createBooking(Booking booking) {

        for (Booking existingBooking : getAllBookings()) {
            if (existingBooking.getCar().getId().equals(booking.getCar().getId())) {
                throw new IllegalArgumentException("Car is already booked");
            }
        }

        boolean userExists = false;
        for (User existingUser : getAllUsers()) {
            if (Objects.equals(existingUser.getId(), booking.getUser().getId())) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            throw new IllegalArgumentException("User cannot be found, please use an existing user");
        }

        boolean carExists = false;
        for (Car existingCar : getAllCars()) {
            if (Objects.equals(existingCar.getId(), booking.getCar().getId())) {
                carExists = true;
                break;
            }

        }

        if (!carExists) {
            throw new IllegalArgumentException("Car cannot be found, please use an existing car");
        }


        StringBuilder bookingCsvFormat = new StringBuilder();

        bookingCsvFormat.append(booking.getId()).append(",");
        bookingCsvFormat.append(booking.getUser().getId()).append(",");
        bookingCsvFormat.append(booking.getCar().getId()).append(",");
        bookingCsvFormat.append(booking.getStartDate()).append(",");
        bookingCsvFormat.append(booking.getEndDate()).append(",");
        bookingCsvFormat.append(booking.getStatus()).append(",");
        bookingCsvFormat.append(booking.getTotalPrice()).append(",");
        bookingCsvFormat.append(booking.getCreatedOn());

        try {
            bookingWriter.writeLineToFile(bookingCsvFormat.toString());
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save booking on file ", e);
        }

        bookings.add(booking);
    }

    @Override
    public void deleteBooking(UUID id) {
        List<Booking> allBookings = getAllBookings();

        for (Booking booking : allBookings) {
            if (booking.getId().equals(id)) {
                allBookings.remove(booking);
                break;
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(BOOKINGS_CSV_FIlENAME))) {
            for (Booking booking : allBookings) {
                StringBuilder bookingCsvFormat = new StringBuilder();

                bookingCsvFormat.append(booking.getId()).append(",");
                bookingCsvFormat.append(booking.getUser().getId()).append(",");
                bookingCsvFormat.append(booking.getCar().getId()).append(",");
                bookingCsvFormat.append(booking.getStartDate()).append(",");
                bookingCsvFormat.append(booking.getEndDate()).append(",");
                bookingCsvFormat.append(booking.getStatus()).append(",");
                bookingCsvFormat.append(booking.getTotalPrice()).append(",");
                bookingCsvFormat.append(booking.getCreatedOn());

                writer.write(bookingCsvFormat.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot update booking file", e);
        }
    }

    @Override
    public List<Car> getAllCarsUserBooked(User user) {
        return List.of();
    }

    @Override
    public List<Booking> getAllBookings() {
        if (bookings != null) {
            return new ArrayList<>(bookings);
        }

        List<String> allBookingFromFile;
        List<Booking> bookingList = new ArrayList<>();

        try {
            allBookingFromFile = fileReader.readFile(Path.of(BOOKINGS_CSV_FIlENAME));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to retrieve booking from file", e);
        }

        if (!allBookingFromFile.isEmpty()) {
            for (String line : allBookingFromFile) {
                Booking bookingFromLine = createBookingFromLine(line);

                bookingList.add(bookingFromLine);
            }
        }

        this.bookings = bookingList;

        return new ArrayList<>(bookingList);
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

        String[] startDateSplitted = bookingField[3].split("-");
        LocalDate startDate = LocalDate.of(
                Integer.parseInt(startDateSplitted[0]),
                Integer.parseInt(startDateSplitted[1]),
                Integer.parseInt(startDateSplitted[2])
        );
        String[] endDateSplitted = bookingField[4].split("-");
        LocalDate endDate = LocalDate.of(
                Integer.parseInt(endDateSplitted[0]),
                Integer.parseInt(endDateSplitted[1]),
                Integer.parseInt(endDateSplitted[2])
        );

        BookingStatus status;
        try {
            status = BookingStatus.valueOf(bookingField[5]);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Booking status cannot be identified", e);
        }

        BigDecimal totalPrice = new BigDecimal(bookingField[6]);

        String[] createdOnSplitted = bookingField[7].split("-");
        LocalDate createdOn = LocalDate.of(
                Integer.parseInt(createdOnSplitted[0]),
                Integer.parseInt(createdOnSplitted[1]),
                Integer.parseInt(createdOnSplitted[2])
        );

        return new Booking(UUID.fromString(bookingId), user, car, startDate,
                endDate, status, totalPrice, createdOn);
    }

}