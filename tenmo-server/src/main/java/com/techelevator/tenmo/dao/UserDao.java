package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    Long findIdByUsername(String username);

    // As a user of the system, I need to be able to register myself with a username and password.
    boolean create(String username, String password);

    User update(User userToUpdate);

    boolean delete(User userToDelete);
}
