package app;

import entity.Booking;
import entity.Car;
import entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CLI {

    public static void main(String[] args) {
        startCarBookingService();
    }

    public static void startCarBookingService() {
        Scanner scanner = new Scanner(System.in);
        CLIContext app = new CLIContext(scanner);
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
                case "1" -> app.bookCarFlow();
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
        private final Scanner scanner;

        public CLIContext(Scanner scanner) {
            this.scanner = scanner;
        }

        public void bookCarFlow() {

            boolean carbooked = false;
            do {
                System.out.println("Please select a user from the list below:\n");
                User selectedUser = getUserFromInput();
                if (selectedUser == null) {
                    return;
                }

                System.out.println("Please select a car from the list below:\n");
                Car selectedCar = getCarFromInput();
                if (selectedCar == null) {
                    return;
                }

                System.out.println("Please input a start date in this format YYYY-MM-DD");
                LocalDate startDate = getDateFromInput();

                System.out.println("Please input a end date in this format YYYY-MM-DD");
                LocalDate endDate = getDateFromInput();

                Booking booking;
                try {
                    booking = new Booking(selectedUser, selectedCar, startDate, endDate);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + ". Please start again.");
                    continue;
                }
                BigDecimal totalPrice = booking.getTotalPrice();

                System.out.printf("""
                        Review Booking
                         User: %s
                         Car: %s
                         Duration: %s - %s
                         Total Price: %s

                        Would you like to make the booking?
                         Input Y/N:
                        """, selectedUser.getName(), selectedCar, startDate, endDate, totalPrice);

                while (true) {

                    String confirmation = scanner.nextLine();
                    confirmation = confirmation.toLowerCase();
                    if (confirmation.equals("y")) {
                        try {
                            app.getBookingService()
                                    .makeBooking(booking);
                            System.out.println("Booking made!");
                            carbooked = true;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    } else if (confirmation.equals("n")) {
                        System.out.println("Booking cancelled, starting again");
                        break;
                    } else {
                        System.out.println("Please enter Y or N");
                    }
                }

            }
            while (!carbooked);
        }


        private User getUserFromInput() {
            List<User> allUsers = app.getUserService().getAllUsers();

            if (allUsers.isEmpty()) {
                System.out.println("There are no users available");
                return null;
            }

            StringBuilder userSelection = new StringBuilder();

            for (int i = 0; i < allUsers.size(); i++) {
                userSelection.append(i + 1)
                        .append(" - ")
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
                    System.out.println("Please choose a number between 1 and " + allUsers.size());

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number to select user");
                }

            }
        }

        private Car getCarFromInput() {
            List<Car> allAvailableCars = app.getCarService().getAllAvailableCars();

            if (allAvailableCars.isEmpty()) {
                System.out.println("There are no available cars");
                return null;
            }

            StringBuilder carSelection = new StringBuilder();

            for (int i = 0; i < allAvailableCars.size(); i++) {
                carSelection.append(i + 1)
                        .append(" - ")
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
                    System.out.println("Please choose a number between 1 and " + allAvailableCars.size());

                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number to select Car");
                }

            }
        }

        private LocalDate getDateFromInput() {
            while (true) {
                try {
                    LocalDate date = LocalDate.parse(scanner.nextLine().trim());
                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("Date cannot be in the past, try again");
                        continue;
                    }
                    return date;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date, use the format YYYY-MM-DD");
                }
            }
        }
    }

}