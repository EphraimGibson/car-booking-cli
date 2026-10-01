package entity;

import utils.StringUtility;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Car {
    private UUID id = UUID.randomUUID();
    private String model;
    private String registrationNumber;
    private BigDecimal pricePerDay;
    private Brand brand;
    private boolean isElectric;

    public Car(String pModel, String pRegistrationNumber, BigDecimal pPrice, Brand pbrand, boolean pIsElectric) {

        if (StringUtility.isStringNullOrBlank(pRegistrationNumber)) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }

        if (pPrice == null || pPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        registrationNumber = pRegistrationNumber;
        model = pModel;
        pricePerDay = pPrice;
        brand = pbrand;
        isElectric = pIsElectric;
    }

    public Car(UUID pId, String pModel, String pRegistrationNumber, BigDecimal pPrice, Brand pbrand, boolean pIsElectric) {

        if (StringUtility.isStringNullOrBlank(pRegistrationNumber)) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }

        if (pPrice == null || pPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        this.id = pId;
        registrationNumber = pRegistrationNumber;
        model = pModel;
        pricePerDay = pPrice;
        brand = pbrand;
        isElectric = pIsElectric;
    }

    public void setRegistrationNumber(String pRegNumber) {
        if (StringUtility.isStringNullOrBlank(pRegNumber)) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        registrationNumber = pRegNumber;
    }

    public void setPricePerDay(BigDecimal pPricePerDay) {
        if (pPricePerDay == null || pPricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }

        pricePerDay = pPricePerDay;
    }

    public UUID getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public boolean isElectric() {
        return isElectric;
    }

    public void setElectric(boolean electric) {
        isElectric = electric;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}