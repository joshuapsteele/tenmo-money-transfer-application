package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class TenmoCLI {

    private ConsoleServiceInterface consoleService = new ConsoleService(System.in, System.out);
    private AccountServiceInterface accountService = new AccountService();
    private TransferServiceInterface transferService = new TransferService();
    private UserServiceInterface userService = new UserService();

    public void viewCurrentBalance(AuthenticatedUser currentUser) {
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
}
