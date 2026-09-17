package app;

import entity.Brand;
import entity.Car;
import entity.User;
import persistence.FilePersistence;
import persistence.IPersistence;
import persistence.MemoryPersistence;
import service.BookingService;
import service.CarService;
import service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContext {
    private final IPersistence persistence = new FilePersistence();

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
        List<User> seedUsers = new ArrayList<>();
        List<Car> seedCars = new ArrayList<>();

        for (int i = 1; i < 10; i++) {
            seedUsers.add(new User("User " + i));
            seedCars.add(new Car("C30" + i, "A1593" + i,
                    new BigDecimal("38" + i), Brand.MERCEDES, true));
        }

        persistence.setAllUsers(seedUsers);
        persistence.setAllCars(seedCars);
    }

}