package com.techelevator.view;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ServiceInterfaces.AccountServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.AuthenticationServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.TransferServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.UserServiceInterface;

import java.math.BigDecimal;

public class MoveMoney {
    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS = {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};

    private AuthenticatedUser currentUser;
    private String token;
    private ConsoleService console;
    private AuthenticationServiceInterface authServ;

    private AccountServiceInterface accountServiceInterface = new AccountService();
    private TransferServiceInterface transferServiceInterface = new TransferService();
    private UserServiceInterface userServiceInterface = new UserService();
    private ViewOptions viewOptions = new ViewOptions();

    public MoveMoney() {

    }

    private void listAllUsers() {
        User[] allUsers = userServiceInterface.findAllUsers();
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

    public void sendBucks(AccountServiceInterface accountServiceInterface, AuthenticatedUser currentUser,
                          TransferServiceInterface transferServiceInterface, ConsoleService console) {
        listAllUsers();

        String userIdPrompt = "Enter ID of user you are sending to (0 to cancel)";
        Long userIdTransferTo = Long.valueOf(console.getUserInputInteger(userIdPrompt));

        if (userIdTransferTo == 0) {
            return;
        }

        Account accountTransferFrom = accountServiceInterface.getAccountByUserId(currentUser.getUser().getUserId());
        Long accountIdTransferFrom = accountTransferFrom.getAccountId();

        Account accountTransferTo = accountServiceInterface.getAccountByUserId(userIdTransferTo);
        Long accountIdTransferTo = accountTransferTo.getAccountId();

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = console.getUserInputBigDecimal(transferAmountPrompt);

        Transfer newTransfer = new Transfer();
        newTransfer.setAccountFrom(accountIdTransferFrom);
        newTransfer.setAccountTo(accountIdTransferTo);
        newTransfer.setAmount(transferAmount);
        newTransfer.setTransferTypeId(2); // SEND
        newTransfer.setTransferStatusId(2); // APPROVED

        boolean wasTransferSuccessful = transferServiceInterface.createTransfer(newTransfer);

        if (wasTransferSuccessful) {
            System.out.println("Transfer was successful");
            viewOptions.viewCurrentBalance(accountServiceInterface, currentUser, console);
        }

    }

    public void requestBucks(AccountServiceInterface accountServiceInterface, AuthenticatedUser currentUser,
                             TransferServiceInterface transferServiceInterface, ConsoleService console) {
        listAllUsers();

        String userIdPrompt = "Enter ID of user you are REQUESTING money FROM (0 to cancel)";
        Long userIdTransferFrom = Long.valueOf(console.getUserInputInteger(userIdPrompt));

        if (userIdTransferFrom == 0) {
            return;
        }

        Account accountTransferFrom = accountServiceInterface.getAccountByUserId(userIdTransferFrom);
        Long accountIdTransferFrom = accountTransferFrom.getAccountId();
        BigDecimal accountTransferFromBalance = accountTransferFrom.getBalance();

        Account accountTransferTo = accountServiceInterface.getAccountByUserId(currentUser.getUser().getUserId());
        Long accountIdTransferTo = accountTransferTo.getAccountId();
        BigDecimal accountTransferToBalance = accountTransferTo.getBalance();

        String transferAmountPrompt = "Enter amount";
        BigDecimal transferAmount = console.getUserInputBigDecimal(transferAmountPrompt);
/*
        if (transferAmount.compareTo(accountService.getUserAccountBalance(requestedSender.getUserId())) == 1) {
            System.out.println("Insufficient funds. Please try again and enter a transfer amount that is lower than your current account balance.");
            return;
        }

        BigDecimal transferAmount = consoleService.getUserInputBigDecimal(transferAmountPrompt);

 */

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

    public void viewPendingRequests(AccountServiceInterface accountServiceInterface, AuthenticatedUser currentUser,
                                    TransferServiceInterface transferServiceInterface, ConsoleService console) {
        Transfer[] allTransfersForCurrentUser = transferServiceInterface.listAllTransfersCurrentUser();

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\tPending Request Transfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
        for (Transfer transfer : allTransfersForCurrentUser) {
            Long accountFromId = transfer.getAccountFrom();
            Long currentUserId = currentUser.getUser().getUserId();
            Long currentUserAccountId = accountServiceInterface.getAccountByUserId(currentUserId).getAccountId();
            if (transfer.getTransferStatusId() == 1 && accountFromId.equals(currentUserAccountId)) {
                String accountFromUsername = accountServiceInterface.getUsernameByAccountId(accountFromId);
                Long accountToId = transfer.getAccountTo();
                String accountToUsername = accountServiceInterface.getUsernameByAccountId(accountToId);
                System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t/\t" + accountToUsername + "\t\t" + transfer.getAmount());
            }
        }
        System.out.println("-------------------------------------------");
        String prompt = "Please enter transfer ID to approve/reject (0 to cancel)";
        Long request = Long.valueOf(console.getUserInputInteger(prompt));

        if (request == 0) {
            return;
        }

        Transfer requestedTransfer = transferServiceInterface.getCurrentUserTransferById(request);

        while (true) {
            String choice = (String) console.getChoiceFromOptions(PENDING_TRANSFER_MENU_OPTIONS);
            if (PENDING_TRANSFER_MENU_OPTION_APPROVE.equals(choice)) {
                requestedTransfer.setTransferStatusId(2);
                transferServiceInterface.updateTransfer(requestedTransfer);
                System.out.println("Transfer approved!");
                viewOptions.viewCurrentBalance(accountServiceInterface, currentUser, console);
                return;

            } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
                requestedTransfer.setTransferStatusId(3);
                transferServiceInterface.updateTransfer(requestedTransfer);
                System.out.println("Transfer rejected!");
                viewOptions.viewCurrentBalance(accountServiceInterface, currentUser, console);
                return;

            } else {
                return;
            }
        }
    }
}
