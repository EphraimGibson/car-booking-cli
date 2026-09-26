package IntegrationTests;

import app.ApplicationContext;
import service.BookingService;
import service.CarService;
import service.UserService;

public class TestApplicationContext {
    ApplicationContext applicationContext;

    public static TestApplicationContext getInstance() {
        return new TestApplicationContext();
    }

    public TestApplicationContext() {
        applicationContext = ApplicationContext.getContext();
    }

    public UserService getUserservice() {
        return applicationContext.getUserService();
    }

    public BookingService getBookingService() {
        return applicationContext.getBookingService();
    }

    public CarService getCarService() {
        return applicationContext.getCarService();
    }


}