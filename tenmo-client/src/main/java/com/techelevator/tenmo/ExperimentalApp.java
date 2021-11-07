package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ServiceInterfaces.AccountServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.AuthenticationServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.TransferServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.UserServiceInterface;
import com.techelevator.view.ConsoleService;
import com.techelevator.view.MoveMoney;
import com.techelevator.view.ViewOptions;

import java.math.BigDecimal;


public class ExperimentalApp {

    private static final String API_BASE_URL = "http://localhost:8080/";

    // TODO: MOVE TO CONSOLEUSERINTERFACE

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

    public AuthenticatedUser currentUser;
    public String currentUserToken;
    public ConsoleService consoleService;
    public AuthenticationServiceInterface authenticationServiceInterface;

    public AccountServiceInterface accountServiceInterface = new AccountService();
    public TransferServiceInterface transferServiceInterface = new TransferService();
    public UserServiceInterface userServiceInterface = new UserService();
    public MoveMoney moveMoney = new MoveMoney();
    public ViewOptions viewOptions = new ViewOptions();

    public static void main(String[] args) {
        ExperimentalApp app = new ExperimentalApp(
                new ConsoleService(System.in, System.out),
                new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public ExperimentalApp(ConsoleService consoleService, AuthenticationServiceInterface authenticationServiceInterface) {
        this.consoleService = consoleService;
        this.authenticationServiceInterface = authenticationServiceInterface;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();

        MoveMoney moveMoney = new MoveMoney();

        ViewOptions viewOptions = new ViewOptions();
        //consoleUI.mainMenu();
        mainMenu();
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE
    private void mainMenu() {
        while (true) {
            String choice = (String) consoleService.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewOptions.viewCurrentBalance(accountServiceInterface, currentUser, consoleService);
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewOptions.viewTransferDetails(accountServiceInterface, currentUser, transferServiceInterface, consoleService);
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                moveMoney.viewPendingRequests(accountServiceInterface, currentUser, transferServiceInterface, consoleService);
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                moveMoney.sendBucks(accountServiceInterface, currentUser, transferServiceInterface, consoleService);
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                moveMoney.requestBucks(accountServiceInterface, currentUser, transferServiceInterface, consoleService);
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    private void viewCurrentBalance() {
        BigDecimal currentBalance = accountServiceInterface.getCurrentUserAccountBalance(currentUser.getUser().getUserId());
        String currentBalanceFormatted = consoleService.displayAsCurrency(currentBalance);
        System.out.println("Your current balance is " + currentBalanceFormatted);
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    private void viewTransferHistory() {
        Transfer[] transfers = transferServiceInterface.listAllTransfersCurrentUser();
        if (transfers == null || transfers.length == 0) {
            System.out.println("Unable to retrieve transfer history.");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\t\t\tTransfers");
        System.out.println("ID\t\t\t\t\tFrom/To\t\t\t\t\tAmount");
        for (Transfer transfer : transfers) {
            Long accountFromId = transfer.getAccountFrom();
            String accountFromUsername = accountServiceInterface.getUsernameByAccountId(accountFromId);
            Long accountToId = transfer.getAccountTo();
            String accountToUsername = accountServiceInterface.getUsernameByAccountId(accountToId);

            System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t\t/\t\t" + accountToUsername + "\t\t" + transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    private void viewTransferDetails() {
        viewTransferHistory();
        String prompt = "For further details on a transfer, enter its ID " +
                "(otherwise, press '0' to exit)";
        Long request = Long.valueOf(consoleService.getUserInputInteger(prompt));
        if (request == 0) {
            return;
        }
        Transfer requestedTransfer = transferServiceInterface.getCurrentUserTransferById(request);
        if (requestedTransfer != null) {
            System.out.println(requestedTransfer.toString());
        } else {
            System.out.println("Unable to retrieve transfer.");
        }

    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE
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
                authenticationServiceInterface.register(credentials);
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
                currentUser = authenticationServiceInterface.login(credentials);
                currentUserToken = currentUser.getToken();
            } catch (AuthenticationServiceException | NullPointerException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
                continue;
            }
            if (currentUserToken != null) {
                accountServiceInterface.setAuthToken(currentUserToken);
                transferServiceInterface.setAuthToken(currentUserToken);
                userServiceInterface.setAuthToken(currentUserToken);
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
