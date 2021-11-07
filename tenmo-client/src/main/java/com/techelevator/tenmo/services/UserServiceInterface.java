package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;

public interface UserServiceInterface {
    void setAuthToken(String authToken);

    User[] findAllUsers();

    User findUserById(Long id);

    String getUsernameByAccountId(Long accountId);
}
