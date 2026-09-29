package app;

import entity.Booking;
import entity.Car;
import entity.User;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class CLI {

    public static void main(String[] args) {
        startCarBookingService();
    }

    public static void startCarBookingService() {
        CLIContext app = new CLIContext();
        Scanner scanner = new Scanner(System.in);
        boolean exitMenu = false;
        do {
            System.out.println("""
                    1 - Book car
                    2 - Delete Booking
                    3 - View All User Booked Cars
                    4 - View All Bookings
                    5 - View Available Cars
                    6 - View Available Electric Cars
                    7 - View All Users
                    8 - Exit
                    """);

            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.println("Please select a User From List");
                }
                case "2" -> System.out.println();
                case "3" -> System.out.println();
                case "4" -> System.out.println();
                case "5" -> System.out.println();
                case "6" -> System.out.println();
                case "7" -> System.out.println();
                case "8" -> {
                    System.out.println("GoodBye, Thanks for using our services!!!");
                    exitMenu = true;
                }
                default -> System.out.println("Invalid option");
            }

        }
        while (!exitMenu);

    }


    public static class CLIContext {

        ApplicationContext app = ApplicationContext.getContext();

        public void bookCarFlow() {
            Scanner scanner = new Scanner(System.in);


            boolean carbooked = false;
            do {
                System.out.println("Please select a user from the list below:\n");
                User selectedUser = getUserFromInput();


                System.out.println("Please select a acr from the list below:\n");
                Car selectedCar = getCarFromInput();

                System.out.println("Please input a start date in this format YYYY-MM-DD");
                LocalDate startDate = getDateFromInput();

                System.out.println("Please input a end date in this format YYYY-MM-DD");
                LocalDate endDate = getDateFromInput();

                Booking booking = new Booking(selectedUser, selectedCar, startDate, endDate);
                BigDecimal totalPrice = booking.getTotalPrice();

                System.out.printf("""
                        Review Booking\n User: %s \n Car: %s\n, Duration: %s - %s\n, Total Price: %s\n\n
                        Would you like to make the booking?\n Input Y/N:
                        """, selectedUser, selectedCar, startDate, endDate, totalPrice);

                while (true) {

                    String confirmation = scanner.nextLine();
                    confirmation = confirmation.toLowerCase();
                    if (confirmation.equals("y") || confirmation.equals("n")) {
                        switch (confirmation) {
                            case "y": {

                                try {
                                    app.getBookingService()
                                            .makeBooking(booking);
                                    carbooked = true;
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            }
                            case "n": {
                                break;
                            }
                        }
                    }

                }

            }
            while (!carbooked);
        }


        private User getUserFromInput() {
            Scanner scanner = new Scanner(System.in);
            List<User> allUsers = app.getUserService().getAllUsers();

            StringBuilder userSelection = new StringBuilder();

            for (int i = 0; i < allUsers.size(); i++) {
                userSelection.append(i + 1)
                        .append(allUsers.get(i).getName())
                        .append(System.lineSeparator());
            }

            while (true) {
                System.out.println("Input user number: " + System.lineSeparator() + userSelection + System.lineSeparator());
                String input = scanner.nextLine();

                try {
                    int choice = Integer.parseInt(input.trim());

                    if (choice >= 1 && choice <= allUsers.size()) {
                        return allUsers.get(choice - 1);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number to select user");
                }

            }
        }

        private Car getCarFromInput() {
            Scanner scanner = new Scanner(System.in);
            List<Car> allAvailableCars = app.getCarService().getAllAvailableCars();

            StringBuilder carSelection = new StringBuilder();

            for (int i = 0; i < allAvailableCars.size(); i++) {
                carSelection.append(i + 1)
                        .append(allAvailableCars.get(i).getRegistrationNumber())
                        .append("  ")
                        .append(allAvailableCars.get(i).getBrand())
                        .append("  ")
                        .append(allAvailableCars.get(i).getModel())
                        .append("  ")
                        .append("Electric: ")
                        .append(allAvailableCars.get(i).isElectric() ? "Yes" : "No")
                        .append(System.lineSeparator());
            }

            while (true) {
                System.out.println("Input Car number: " + System.lineSeparator() + carSelection + System.lineSeparator());
                String input = scanner.nextLine();

                try {
                    int choice = Integer.parseInt(input.trim());

                    if (choice >= 1 && choice <= allAvailableCars.size()) {
                        return allAvailableCars.get(choice - 1);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number to select Car");
                }

            }
        }

        private LocalDate getDateFromInput() {
            return null;
        }
    }

}