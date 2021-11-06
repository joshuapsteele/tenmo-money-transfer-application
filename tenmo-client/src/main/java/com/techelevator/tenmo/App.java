package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

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
    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS = {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};

    private AuthenticatedUser currentUser;
    private String currentUserToken;
    private ApplicationService applicationService;
    private ConsoleService consoleService;
    private AuthenticationService authenticationService;

    public static void main(String[] args) {
        App app = new App(new ApplicationService(),
                new ConsoleService(System.in, System.out),
                new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ApplicationService applicationService, ConsoleService consoleService, AuthenticationService authenticationService) {
        this.applicationService = applicationService;
        this.consoleService = consoleService;
        this.authenticationService = authenticationService;
    }

    public void run() {

        System.out.println("***************************************************************");
        System.out.println();
        System.out.println("WELCOME TO");
        System.out.println("\n" +
                "$$$$$$$$\\ $$$$$$$$\\                                   \n" +
                "\\__$$  __|$$  _____|                                  \n" +
                "   $$ |   $$ |      $$$$$$$\\  $$$$$$\\$$$$\\   $$$$$$\\  \n" +
                "   $$ |   $$$$$\\    $$  __$$\\ $$  _$$  _$$\\ $$  __$$\\ \n" +
                "   $$ |   $$  __|   $$ |  $$ |$$ / $$ / $$ |$$ /  $$ |\n" +
                "   $$ |   $$ |      $$ |  $$ |$$ | $$ | $$ |$$ |  $$ |\n" +
                "   $$ |   $$$$$$$$\\ $$ |  $$ |$$ | $$ | $$ |\\$$$$$$  |\n" +
                "   \\__|   \\________|\\__|  \\__|\\__| \\__| \\__| \\______/ \n");
        System.out.println("****************************************************************");
        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) consoleService.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                applicationService.viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                applicationService.viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                applicationService.viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                applicationService.sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                applicationService.requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
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

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
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

        // TODO FIND A WAY TO GIVE THE USER AN OPTION TO BREAK OUT OF THIS LOOP?
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
                applicationService.setAuthTokens(currentUserToken);
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
}
