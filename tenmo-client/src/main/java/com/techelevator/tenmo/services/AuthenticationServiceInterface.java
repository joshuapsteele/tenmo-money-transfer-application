package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;

public interface AuthenticationServiceInterface {
    AuthenticatedUser login(UserCredentials credentials) throws AuthenticationServiceException;

    void register(UserCredentials credentials) throws AuthenticationServiceException;
}
