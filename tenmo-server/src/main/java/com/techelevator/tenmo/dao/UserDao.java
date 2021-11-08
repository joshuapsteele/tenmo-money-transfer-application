package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findById(Long id);

    User findByUsername(String username);

    Long findIdByUsername(String username);

    boolean create(String username, String password);

    boolean update(Long id, User userToUpdate);

    boolean delete(Long id);
}
