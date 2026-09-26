package entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class CarTest {

    @Test
    void testShouldThrowErrorWhenRegistrationNumberIsNull() {
        assertInvalidCar("C300", null, new BigDecimal("55.32"), Brand.MERCEDES, true,
                "Registration number cannot be empty");
    }

    @Test
    void testShouldThrowErrorWhenRegistrationNumberIsEmpty() {
        assertInvalidCar("C300", "", new BigDecimal("55.32"), Brand.MERCEDES, true,
                "Registration number cannot be empty");
    }

    @Test
    void testShouldThrowErrorWhenPricePerDayIsNegative() {
        assertInvalidCar("C300", "A15DFT", new BigDecimal("-55"), Brand.MERCEDES, true,
                "Price must be positive");
    }

    @Test
    void testShouldThrowErrorWhenPricePerDayIsZero() {
        assertInvalidCar("C300", "A15DFT", BigDecimal.ZERO, Brand.MERCEDES, true,
                "Price must be positive");
    }

    private void assertInvalidCar(String model, String registrationNumber, BigDecimal pricePerDay,
                                  Brand brand, boolean isElectric, String expectedMessage) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Car(model, registrationNumber, pricePerDay, brand, isElectric));

        assertEquals(expectedMessage, exception.getMessage());
    }

}