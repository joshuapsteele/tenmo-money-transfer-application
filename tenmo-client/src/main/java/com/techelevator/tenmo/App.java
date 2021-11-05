package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import com.techelevator.view.ConsoleService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private ConsoleService console;
    private AuthenticationService authenticationService;

    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    public static void main(String[] args) {
        App app = new App(
                new ConsoleService(System.in, System.out),
                new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.console = console;
        this.authenticationService = authenticationService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
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

    private void viewCurrentBalance() {
        BigDecimal currentBalance = accountService.getUserAccountBalance(currentUser.getUser().getUserId());
        String currentBalanceFormatted = console.displayAsCurrency(currentBalance);
        System.out.println("Your current balance is " + currentBalanceFormatted);
    }

    private void viewTransferHistory() {
        Transfer[] transfers = transferService.listAllTransfersCurrentUser();
        if (transfers == null || transfers.length == 0) {
            System.out.println("Unable to retrieve transfer history.");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\t\tTransfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
        for (Transfer transfer : transfers) {
            Long accountFromId = transfer.getAccountFrom();
            String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
            Long accountToId = transfer.getAccountTo();
            String accountToUsername = accountService.getUsernameByAccountId(accountToId);

            System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t\t/\t\t" + accountToUsername + "\t\t" + transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
        System.out.println();
    }

    private void viewTransferDetails() {
        viewTransferHistory();
        String prompt = "For further details on a transfer, enter its ID " +
                "(otherwise, press '0' to exit)";
        Long request = Long.valueOf(console.getUserInputInteger(prompt));
        if (request == 0) {
            return;
        }
        Transfer requestedTransfer = transferService.getTransferById(request);
        if (requestedTransfer != null) {
            System.out.println(requestedTransfer.toString());
        } else {
            System.out.println("Unable to retrieve transfer.");
        }

    }

    private void listAllUsers() {
        User[] allUsers = userService.findAllUsers();
        System.out.println("LIST OF ALL USERS");
        System.out.println("-------------------------------------------");
        System.out.println("\t\t\t\tUSERS");
        System.out.println("ID\t\t\t\tNAME");
        System.out.println("-------------------------------------------");
        for (User user : allUsers) {
            System.out.println(user.getUserId() + "\t\t\t\t" + user.getUsername());
        }
        System.out.println("---------");
        System.out.println();
    }

    private void sendBucks() {
        listAllUsers();

        String userIdPrompt = "Enter ID of user you are sending to (0 to cancel)";
        Long userIdTransferTo = Long.valueOf(console.getUserInputInteger(userIdPrompt));

        if (userIdTransferTo == 0) {
            return;
        }

        Account accountTransferFrom = accountService.getAccountByUserId(currentUser.getUser().getUserId());
        Long accountIdTransferFrom = accountTransferFrom.getAccountId();

        Account accountTransferTo = accountService.getAccountByUserId(userIdTransferTo);
        Long accountIdTransferTo = accountTransferTo.getAccountId();

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = console.getUserInputBigDecimal(transferAmountPrompt);

        if (transferAmount.compareTo(accountService.getUserAccountBalance(currentUser.getUser().getUserId())) == 1) {
            System.out.println("Insufficient funds. Please try again and enter a transfer amount that is lower than your current account balance.");
            return;
        }

        // Perhaps make another constructor here.
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(accountIdTransferFrom);
        newTransfer.setAccountTo(accountIdTransferTo);
        newTransfer.setAmount(transferAmount);
        newTransfer.setTransferTypeId(2); // SEND
        newTransfer.setTransferStatusId(2); // APPROVED

        transferService.createTransfer(newTransfer);

    }

    // TODO IMPLEMENT STRUCTURAL/DESIGN CHANGES FROM ABOVE TO THIS METHOD BELOW
    private void requestBucks() {
        listAllUsers();

        String userIdPrompt = "Enter ID of user you are REQUESTING money FROM (0 to cancel)";
        Long userIdTransferFrom = Long.valueOf(console.getUserInputInteger(userIdPrompt));

        if (userIdTransferFrom == 0) {
            return;
        }

        Account accountTransferFrom = accountService.getAccountByUserId(userIdTransferFrom);
        Long accountIdTransferFrom = accountTransferFrom.getAccountId();

        Account accountTransferTo = accountService.getAccountByUserId(currentUser.getUser().getUserId());
        Long accountIdTransferTo = accountTransferTo.getAccountId();

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = console.getUserInputBigDecimal(transferAmountPrompt);

        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(accountIdTransferFrom);
        newTransfer.setAccountTo(accountIdTransferTo);
        newTransfer.setAmount(transferAmount);
        newTransfer.setTransferTypeId(1); // REQUEST
        newTransfer.setTransferStatusId(1); // PENDING

        // Tells server to create new transfer and store in the database.
        transferService.createTransfer(newTransfer);

        // TODO FIGURE OUT HOW/WHERE TO PAUSE THE PROCESS, BECAUSE WE DON'T WANT TO CHANGE THE BALANCES UNTIL THE TRANSFER STATUS CHANGES FROM PENDING TO APPROVED.

        // Same structure, more or less, as above. Validate the numbers on the CLIENT SIDE before creating a Transfer Object.
        // Transfer Object will have a DIFFERENT transfer_type_id and transfer_status than the sendBucks transfer.
        // ASK user to approve or reject the transfer.
        // DON'T tell the server to update account balances until the transfer is approved.

    }

    private void viewPendingRequests() {
        // Get a Transfer[], but JUST Transfers that have a certain transfer_status_id.
        Transfer[] allTransfersForCurrentUser = transferService.listAllTransfersCurrentUser();
        System.out.println("-------------------------------------------");
        System.out.println("\t\t\tPending Transfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
        for (Transfer transfer : allTransfersForCurrentUser) {
            Long accountFromId = transfer.getAccountFrom();
            Long currentUserId = currentUser.getUser().getUserId();
            if (transfer.getTransferStatusId() == 1 && accountFromId.equals(currentUserId)) {
                String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
                Long accountToId = transfer.getAccountTo();
                String accountToUsername = accountService.getUsernameByAccountId(accountToId);
                System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t/\t" + accountToUsername + "\t\t" + transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
        String prompt = "Please enter transfer ID to approve/reject (0 to cancel)";
        Long request = Long.valueOf(console.getUserInputInteger(prompt));
        if (request == 0) {
            return;
        }
        Transfer requestedTransfer = transferService.getTransferById(request);

        // INSERT CHECK TO SEE IF TRANSFER AMOUNT EXCEEDS USER ACCOUNT BALANCE.

        while (true) {
            String choice = (String) console.getChoiceFromOptions(PENDING_TRANSFER_MENU_OPTIONS);
            if (PENDING_TRANSFER_MENU_OPTION_APPROVE.equals(choice)) {
                // APPROVE TRANSFER
                requestedTransfer.setTransferStatusId(2);
                transferService.updateTransfer(requestedTransfer);
                // TODO HAVE THE SERVER UPDATE THE BALANCES. NOT THE CLIENT.

            } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
                requestedTransfer.setTransferStatusId(3);
                transferService.updateTransfer(requestedTransfer);
                // TODO HAVE THE SERVER UPDATE THE BALANCES. NOT THE CLIENT.

            } else {
                return;
            }
        }
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
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
                accountService.setAuthToken(currentUserToken);
                transferService.setAuthToken(currentUserToken);
                userService.setAuthToken(currentUserToken);
            } else {
                System.out.println("USER AUTHENTICATION ERROR: Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}
