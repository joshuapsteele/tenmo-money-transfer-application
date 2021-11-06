package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

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
    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS = {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};


    private AuthenticatedUser currentUser;
    private String currentUserToken;

    private ConsoleService consoleService = new ConsoleService(System.in, System.out);
    private AuthenticationServiceInterface authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountServiceInterface accountService = new AccountService();
    private TransferServiceInterface transferService = new TransferService();
    private UserServiceInterface userService = new UserService();

    public ApplicationService() {
    }
    public void viewCurrentBalance() {
        BigDecimal currentBalance = accountService.getCurrentUserAccountBalance(currentUser.getUser().getUserId());
        String currentBalanceFormatted = consoleService.displayAsCurrency(currentBalance);
        System.out.println("Your current balance is " + currentBalanceFormatted);
    }

    public void viewTransferHistory() {
        Transfer[] transfers = transferService.listAllTransfersCurrentUser();
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
            String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
            Long accountToId = transfer.getAccountTo();
            String accountToUsername = accountService.getUsernameByAccountId(accountToId);

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

    public void listAllUsers() {
        User[] allUsers = userService.findAllUsers();
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

    public void sendBucks() {
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

        boolean wasTransferSuccessful = transferService.createTransfer(newTransfer);

        if (wasTransferSuccessful) {
            System.out.println("Transfer was successful");
            viewCurrentBalance();
        }

    }

    public void requestBucks() {
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

        boolean transferWasSuccessful = transferService.createTransfer(newTransfer);

        if (transferWasSuccessful) {
            System.out.println("Request was successful. " +
                    "The other user will now have to accept/reject the request before funds are transferred to your account.");
        }
    }

    public void viewPendingRequests() {
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
            Long currentUserAccountId = accountService.getAccountByUserId(currentUserId).getAccountId();
            if (transfer.getTransferStatusId() == 1 && accountFromId.equals(currentUserAccountId)) {
                hasPendingRequests = true;
                String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
                Long accountToId = transfer.getAccountTo();
                String accountToUsername = accountService.getUsernameByAccountId(accountToId);
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

    public void setAuthTokens(String token) {
        accountService.setAuthToken(token);
        transferService.setAuthToken(token);
        userService.setAuthToken(token);
    }
}