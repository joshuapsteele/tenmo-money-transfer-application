package com.techelevator.tenmo.services.ServiceInterfaces;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationServiceException;

public interface AuthenticationServiceInterface {
    AuthenticatedUser login(UserCredentials credentials) throws AuthenticationServiceException;

    void register(UserCredentials credentials) throws AuthenticationServiceException;
}
