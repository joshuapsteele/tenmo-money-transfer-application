package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ServiceInterfaces.AccountServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.AuthenticationServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.TransferServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.UserServiceInterface;

public class Authorize {
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private String currentUserToken;
    private ConsoleService consoleService;
    private AuthenticationServiceInterface authenticationServiceInterface;

    private AccountServiceInterface accountServiceInterface = new AccountService();
    private TransferServiceInterface transferServiceInterface = new TransferService();
    private UserServiceInterface userServiceInterface = new UserService();


}
