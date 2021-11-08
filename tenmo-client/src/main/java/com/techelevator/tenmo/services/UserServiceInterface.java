package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;

public interface UserServiceInterface {
    void setAuthToken(String authToken);

    User[] findAllUsers();

    User findUserById(Long id);

    String getUsernameByAccountId(Long accountId);

    HttpEntity<Void> makeAuthEntity();

    HttpEntity<User> makeUserEntity(User user);
}
