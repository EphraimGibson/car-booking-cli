package service;

import entity.Car;
import persistence.IPersistence;

import java.util.List;

public class CarService {
    private final IPersistence persistence;

    public CarService(IPersistence persistence) {
        this.persistence = persistence;
    }

    public List<Car> getAllCars() {
        return persistence.getAllCars();
    }

    public List<Car> getAllAvailableCars() {
        return persistence.getAllAvailableCars();
    }

    public List<Car> getAllElectricCars() {
        return persistence.allElectricCars();
    }

}