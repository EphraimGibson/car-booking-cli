package service;

import entity.User;
import persistence.IPersistence;


import java.util.List;

public class UserService {
    private final IPersistence persistence;

    public UserService(IPersistence persistence) {
        this.persistence = persistence;
    }

    public List<User> getAllUsers() {
        return persistence.getAllUsers();
    }


}