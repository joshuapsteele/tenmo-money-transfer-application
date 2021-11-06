package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

public class Authorize {
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private String currentUserToken;
    private ConsoleService consoleService;
    private AuthenticationService authenticationService;

    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();


}
