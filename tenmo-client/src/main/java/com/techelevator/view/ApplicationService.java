package com.techelevator.view;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class ApplicationService {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};



    private AuthenticatedUser currentUser;
    private String currentUserToken;

    private ConsoleServiceInterface consoleService = new ConsoleService(System.in, System.out);
    private AuthenticationServiceInterface authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountServiceInterface accountService = new AccountService();
    private TransferServiceInterface transferService = new TransferService();
    private UserServiceInterface userService = new UserService();

    private TenmoCLI tenmoCLI = new TenmoCLI();
    private MoveMoney moveMoney = new MoveMoney();


//    private ConsoleServiceInterface consoleService;
//    private AuthenticationServiceInterface authenticationService;
//    private AccountServiceInterface accountService;
//    private TransferServiceInterface transferService;
//    private UserServiceInterface userService;

    public ApplicationService() {
    }

//    public ApplicationService(ConsoleServiceInterface consoleService,
//                              AuthenticationServiceInterface authenticationService,
//                              AccountServiceInterface accountService,
//                              TransferServiceInterface transferService,
//                              UserServiceInterface userService) {
//    }

    public void mainMenu() {
        while (true) {
            String choice = (String) consoleService.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                tenmoCLI.viewCurrentBalance(currentUser);
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                tenmoCLI.viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                moveMoney.viewPendingRequests(currentUser);
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                moveMoney.sendBucks(currentUser);
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                moveMoney.requestBucks(currentUser);
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    public void exitProgram() {
        System.exit(0);
    }

    public void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) consoleService.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in

        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
                currentUserToken = currentUser.getToken();
            } catch (AuthenticationServiceException | NullPointerException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
                continue;
            }
            if (currentUserToken != null) {
                setAuthTokens(currentUserToken);
            } else {
                System.out.println("USER AUTHENTICATION ERROR: Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = consoleService.getUserInput("Username");
        String password = consoleService.getUserInput("Password");
        return new UserCredentials(username, password);
    }

    public void setAuthTokens(String token) {
        accountService.setAuthToken(token);
        transferService.setAuthToken(token);
        userService.setAuthToken(token);
    }
}
