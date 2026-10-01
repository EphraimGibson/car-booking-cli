package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;


public class Booking {
    private UUID id = UUID.randomUUID();
    private User user;
    private Car car;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private BigDecimal totalPrice;

    private LocalDate createdOn;

    public Booking() {
    }

    public Booking(UUID pId, User pUser, Car pCar, LocalDate pStartDate, LocalDate pEndDate, BookingStatus pStatus, BigDecimal pTotalPrice, LocalDate createdOn) {

        validateInput(pUser, pCar, pStartDate, pEndDate);

        this.id = pId;
        this.car = pCar;
        this.startDate = pStartDate;
        this.endDate = pEndDate;
        this.status = pStatus;
        this.totalPrice = pTotalPrice;
        this.createdOn = createdOn;

        BigDecimal calculatedTotalPrice = this.calculateTotalPrice();
        validateTotalPrice(calculatedTotalPrice);

        this.totalPrice = calculatedTotalPrice;
    }


    public Booking(User pUser, Car pCar, LocalDate pStartDate, LocalDate pEndDate, BookingStatus pStatus) {

        validateInput(pUser, pCar, pStartDate, pEndDate);

        this.car = pCar;
        this.startDate = pStartDate;
        this.endDate = pEndDate;
        this.status = pStatus;
        this.createdOn = LocalDate.now();

        BigDecimal calculatedTotalPrice = this.calculateTotalPrice();
        validateTotalPrice(calculatedTotalPrice);

        this.totalPrice = calculatedTotalPrice;
    }

    public Booking(User pUser, Car pCar, LocalDate pStartDate, LocalDate pEndDate) {

        validateInput(pUser, pCar, pStartDate, pEndDate);

        this.car = pCar;
        this.startDate = pStartDate;
        this.endDate = pEndDate;
        this.createdOn = LocalDate.now();


        BigDecimal calculatedTotalPrice = this.calculateTotalPrice();
        validateTotalPrice(calculatedTotalPrice);

        this.totalPrice = calculatedTotalPrice;
    }

    public void setUser(User pUser) {
        if (pUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = pUser;
    }

    public void setCar(Car pCar) {
        if (pCar == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }

        this.car = pCar;
    }

    public void setStartDate(LocalDate pStartDate) {
        if (pStartDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (pStartDate.isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        this.startDate = pStartDate;
    }

    public void setEndDate(LocalDate pEndDate) {
        if (pEndDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (startDate.isAfter(pEndDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        this.endDate = pEndDate;
    }

    public UUID getId() {
        return id;
    }
    public User getUser() {
        return user;
    }

    public Car getCar() {
        return car;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    private void validateInput(User pUser, Car pCar, LocalDate pStartDate, LocalDate pEndDate) {
        if (pUser == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        this.user = pUser;

        if (pCar == null) {
            throw new IllegalArgumentException("Car cannot be null");
        }

        if (pStartDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (pEndDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (pStartDate.isAfter(pEndDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    private int getNumberOfDays() {
        if (startDate != null && endDate != null) {
            return Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate));
        }
        return 0;
    }

    private BigDecimal calculateTotalPrice() {
        return this.car != null ?
                this.car.getPricePerDay().multiply(new BigDecimal(getNumberOfDays())) : null;
    }

    private void validateTotalPrice(BigDecimal pTotalPrice) {
        if (pTotalPrice == null || pTotalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total price of booking must be positive");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}