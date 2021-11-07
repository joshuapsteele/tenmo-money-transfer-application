/*package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ServiceInterfaces.AccountServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.AuthenticationServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.TransferServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.UserServiceInterface;
import com.techelevator.view.ConsoleService;
//import com.techelevator.view.ConsoleUserInterface;
//import jdk.swing.interop.SwingInterOpUtils;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

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
    private ConsoleService consoleService;
    private AuthenticationServiceInterface authenticationServiceInterface;

    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    public static void main(String[] args) {
        App app = new App(
                new ConsoleService(System.in, System.out),
                new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService consoleService, AuthenticationServiceInterface authenticationServiceInterface) {
        this.consoleService = consoleService;
        this.authenticationServiceInterface = authenticationServiceInterface;
    }

    public void run() {
//        ConsoleUserInterface consoleUI = new ConsoleUserInterface(
//                new ConsoleService(System.in, System.out),
//                new AuthenticationService(API_BASE_URL));
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) consoleService.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferDetails();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
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

    private void viewTransferHistory() {
        Transfer[] transfers = transferServiceInterface.listAllTransfersCurrentUser();
        if (transfers == null || transfers.length == 0) {
            System.out.println("Unable to retrieve transfer history.");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("TRANSFERS");
        System.out.println("ID\t\t\tFROM\t\t/\t\tTO\t\t\tAMOUNT");
        System.out.println("-------------------------------------------");
        for (Transfer transfer : transfers) {
            Long accountFromId = transfer.getAccountFrom();
            String accountFromUsername = accountServiceInterface.getUsernameByAccountId(accountFromId);
            Long accountToId = transfer.getAccountTo();
            String accountToUsername = accountServiceInterface.getUsernameByAccountId(accountToId);

            System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t\t/\t\t" + accountToUsername + "\t\t" + transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
        System.out.println();

        boolean isTransferIdValid = false;
        Long requestedTransferId = null;

        while (true) {
            String prompt = "For further details on a transfer, enter its ID " +
                    "(otherwise, press '0' to exit)";
            requestedTransferId = Long.valueOf(consoleService.getUserInputInteger(prompt));

            if (requestedTransferId == 0) {
                return;
            }

            for (Transfer transfer : transfers) {
                if (transfer.getTransferId().equals(requestedTransferId)) {
                    isTransferIdValid = true;
                }
            }

            if (!isTransferIdValid) {
                System.out.println("Invalid transfer ID. Please try again.");
                continue;
            } else {
                break;
            }
        }

        Transfer requestedTransfer = null;
        try {
            requestedTransfer = transferService.getCurrentUserTransferById(requestedTransferId);
        } catch (Exception e) {
            System.out.println("Unable to retrieve transfer." + e.getMessage());
            return;
        }
        if (requestedTransfer != null) {
            System.out.println(requestedTransfer.toString());
        } else {
            System.out.println("Unable to retrieve transfer.");
        }
    }

    private void listAllUsers() {
        User[] allUsers = userServiceInterface.findAllUsers();
        System.out.println("LIST OF ALL USERS");
        System.out.println("-------------------------------------------");
        System.out.println("USERS");
        System.out.println("ID\t\t\tNAME");
        System.out.println("-------------------------------------------");
        for (User user : allUsers) {
            System.out.println(user.getUserId() + "\t\t" + user.getUsername());
        }
        System.out.println("---------");
        System.out.println();
    }

    private void sendBucks() {
        listAllUsers();
        Long userIdTransferTo = null;
        boolean isUserIdValid = false;

        while (true) {
            String userIdPrompt = "Enter ID of user you are sending to (0 to cancel)";
            userIdTransferTo = Long.valueOf(consoleService.getUserInputInteger(userIdPrompt));

            if (userIdTransferTo == 0) {
                return;
            }

            User[] allUsers = userService.findAllUsers();
            for (User user : allUsers) {
                if (user.getUserId().equals(userIdTransferTo)) {
                    isUserIdValid = true;
                }
            }

            if (isUserIdValid) {
                break;
            } else {
                System.out.println("Invalid User ID. Please try again.");
            }
        }

        Account accountTransferFrom = null;
        Long accountIdTransferFrom = null;
        Account accountTransferTo = null;
        Long accountIdTransferTo = null;

        try {
            accountTransferFrom = accountService.getAccountByUserId(currentUser.getUser().getUserId());
            accountIdTransferFrom = accountTransferFrom.getAccountId();

            accountTransferTo = accountService.getAccountByUserId(userIdTransferTo);
            accountIdTransferTo = accountTransferTo.getAccountId();
        } catch (Exception e) {
            System.out.println("Unable to retrieve accounts for the transfer." + e.getMessage());
            return;
        }

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = consoleService.getUserInputBigDecimal(transferAmountPrompt);

        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(accountIdTransferFrom);
        newTransfer.setAccountTo(accountIdTransferTo);
        newTransfer.setAmount(transferAmount);
        newTransfer.setTransferTypeId(2); // SEND
        newTransfer.setTransferStatusId(2); // APPROVED

        boolean wasTransferSuccessful = transferServiceInterface.createTransfer(newTransfer);

        if (wasTransferSuccessful) {
            System.out.println("Transfer was successful");
            viewCurrentBalance();
        }

    }

    private void requestBucks() {
        listAllUsers();

        String userIdPrompt = "Enter ID of user you are REQUESTING money FROM (0 to cancel)";
        Long userIdTransferFrom = Long.valueOf(consoleService.getUserInputInteger(userIdPrompt));

        if (userIdTransferFrom == 0) {
            return;
        }

        Account accountTransferFrom = null;
        Long accountIdTransferFrom = null;
        BigDecimal accountTransferFromBalance = null;
        Account accountTransferTo = null;
        Long accountIdTransferTo = null;
        BigDecimal accountTransferToBalance = null;

        try {
            accountTransferFrom = accountService.getAccountByUserId(userIdTransferFrom);
            accountIdTransferFrom = accountTransferFrom.getAccountId();
            accountTransferFromBalance = accountTransferFrom.getBalance();

            accountTransferTo = accountService.getAccountByUserId(currentUser.getUser().getUserId());
            accountIdTransferTo = accountTransferTo.getAccountId();
            accountTransferToBalance = accountTransferTo.getBalance();
        } catch (Exception e) {
            System.out.println("Unable to retrieve accounts for requested transfer." + e.getMessage());
            return;
        }

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = consoleService.getUserInputBigDecimal(transferAmountPrompt);



        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(accountIdTransferFrom);
        newTransfer.setAccountTo(accountIdTransferTo);
        newTransfer.setAmount(transferAmount);
        newTransfer.setTransferTypeId(1); // REQUEST
        newTransfer.setTransferStatusId(1); // PENDING

        boolean transferWasSuccessful = transferServiceInterface.createTransfer(newTransfer);

        if (transferWasSuccessful) {
            System.out.println("Request was successful. " +
                    "The other user will now have to accept/reject the request before funds are transferred to your account.");
        }
    }

    private void viewPendingRequests() {
        Transfer[] allTransfersForCurrentUser = transferService.listAllTransfersCurrentUser();
        boolean hasPendingRequests = false;

        if (allTransfersForCurrentUser == null || allTransfersForCurrentUser.length == 0) {
            System.out.println("Unable to retrieve pending requests.");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\tPending Request Transfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
        System.out.println("-------------------------------------------");

        for (Transfer transfer : allTransfersForCurrentUser) {
            Long accountFromId = transfer.getAccountFrom();
            Long currentUserId = currentUser.getUser().getUserId();
            Long currentUserAccountId = accountServiceInterface.getAccountByUserId(currentUserId).getAccountId();
            if (transfer.getTransferStatusId() == 1 && accountFromId.equals(currentUserAccountId)) {
                hasPendingRequests = true;
                String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
                Long accountToId = transfer.getAccountTo();
                String accountToUsername = accountServiceInterface.getUsernameByAccountId(accountToId);
                System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t/\t" + accountToUsername + "\t\t" + transfer.getAmount());
            }
        }

        if (!hasPendingRequests) {
            System.out.println("\nYou have 0 pending requests at this time.");
            return;
        }

        System.out.println("-------------------------------------------");
        String prompt = "Please enter transfer ID to approve/reject (0 to cancel)";
        Long request = Long.valueOf(consoleService.getUserInputInteger(prompt));

        if (request == 0) {
            return;
        }

        Transfer requestedTransfer = null;

        try {
            requestedTransfer = transferService.getCurrentUserTransferById(request);
        } catch (Exception e) {
            System.out.println("Unable to retrieve transfer." + e.getMessage());
        }

        String choice = (String) consoleService.getChoiceFromOptions(PENDING_TRANSFER_MENU_OPTIONS);
        if (PENDING_TRANSFER_MENU_OPTION_APPROVE.equals(choice)) {
            if (requestedTransfer != null) {
                requestedTransfer.setTransferStatusId(2);
            }
            transferService.updateTransfer(requestedTransfer);
            System.out.println("Transfer approved!");
            viewCurrentBalance();
            return;
        } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
            if (requestedTransfer != null) {
                requestedTransfer.setTransferStatusId(3);
            }
            transferService.updateTransfer(requestedTransfer);
            System.out.println("Transfer rejected!");
            viewCurrentBalance();
            return;
        } else {
            return;
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
 */