package app;

import entity.Brand;
import entity.Car;
import entity.User;
import persistence.FilePersistence;
import persistence.IPersistence;
import service.BookingService;
import service.CarService;
import service.UserService;
import utils.FileReader;
import utils.FileWriter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationContext {
    public static final Path BOOKINGS_CSV_PATH = Path.of(System.getProperty("user.home"),
            ".car-booking", "Bookings.csv");
    private final FileWriter fileWriter = new FileWriter(BOOKINGS_CSV_PATH);
    private final FileReader fileReader = new FileReader();
    private final IPersistence persistence = new FilePersistence(fileReader, fileWriter);
    private final BookingService bookingService = new BookingService(persistence);
    private final UserService userService = new UserService(persistence);
    private final CarService carService = new CarService(persistence);

    public static ApplicationContext getContext() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.start();
        return applicationContext;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CarService getCarService() {
        return carService;
    }

    private void start() {
        init();
    }

    private void init() {
        persistence.setAllUsers(loadUsersFromFile());
        persistence.setAllCars(loadCarsFromFile());
    }

    private List<Car> loadCarsFromFile() {
        List<Car> cars = new ArrayList<>();
        List<String> allCarsFromFile;

        try {
            allCarsFromFile = fileReader.readFile("Cars.csv");
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
        return cars;
    }

    private List<User> loadUsersFromFile() {
        List<String> allUsersFromFile;
        List<User> users = new ArrayList<>();

        try {
            allUsersFromFile = fileReader.readFile("Users.csv");
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
        return users;
    }
}