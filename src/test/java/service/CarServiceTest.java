package service;

import entity.Brand;
import entity.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import persistence.IPersistence;

import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {
    @Mock
    IPersistence persistenceMock;

    @InjectMocks
    CarService carService;

    @Test
    void testShouldReturnAllCars() {
        // Given
        Car car = new Car("C300", "A15DFT", new BigDecimal(55), Brand.MERCEDES, true);

        when(persistenceMock.getAllCars()).thenReturn(List.of(car));

        // When
        List<Car> result = carService.getAllCars();

        // Then
        Assertions.assertEquals(1, result.size(), "Expected 1 car");
        Assertions.assertEquals(car, result.getFirst(), "Expected car not found");

        verify(persistenceMock, times(1)).getAllCars();
    }

    @Test
    void testShouldReturnAllAvailableCars() {
        // Given
        Car car = new Car("C300", "A15DFT", new BigDecimal(55), Brand.MERCEDES, true);
        Car car2 = new Car("C300", "BAPEFT", new BigDecimal("30.1"), Brand.MERCEDES, true);

        when(persistenceMock.getAllAvailableCars()).thenReturn(List.of(car, car2));

        // When
        List<Car> result = carService.getAllAvailableCars();

        // Then
        Assertions.assertEquals(2, result.size(), "Expected 2 cars");
        Assertions.assertEquals(car, result.get(0), "Expected car not found");
        Assertions.assertEquals(car2, result.get(1), "Expected car not found");

        verify(persistenceMock, times(1)).getAllAvailableCars();
    }

    @Test
    void testShouldReturnAllElectricCars() {
        // Given
        Car car = new Car("C300", "A15DFT", new BigDecimal("55.32"), Brand.MERCEDES, true);
        Car car2 = new Car("C300", "BAPEFT", new BigDecimal("30.1"), Brand.MERCEDES, true);

        when(persistenceMock.allElectricCars()).thenReturn(List.of(car, car2));

        // When
        List<Car> result = carService.getAllElectricCars();

        // Then
        Assertions.assertEquals(2, result.size(), "Expected 2 electric cars");
        Assertions.assertEquals(car, result.get(0), "Expected electric car not found");
        Assertions.assertEquals(car2, result.get(1), "Expected electric car not found");

        verify(persistenceMock, times(1)).allElectricCars();
    }
}