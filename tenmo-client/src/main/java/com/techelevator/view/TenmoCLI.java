package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class TenmoCLI {

    private final ConsoleServiceInterface consoleService = new ConsoleService(System.in, System.out);
    private final AccountServiceInterface accountService;
    private final TransferServiceInterface transferService;
    private final UserServiceInterface userService;

    public TenmoCLI(AccountServiceInterface accountService, TransferServiceInterface transferService,
                    UserServiceInterface userService) {
        this.accountService = accountService;
        this.transferService = transferService;
        this.userService = userService;
    }

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

        System.out.println("-----------------------------------------------------");
        System.out.println("TRANSFERS");
        System.out.println("ID\t\t\tFROM\t\t/\t\tTO\t\t\tAMOUNT");
        System.out.println("-----------------------------------------------------");
        for (Transfer transfer : transfers) {
            Long accountFromId = transfer.getAccountFrom();
            String accountFromUsername = accountService.getUsernameByAccountId(accountFromId);
            Long accountToId = transfer.getAccountTo();
            String accountToUsername = accountService.getUsernameByAccountId(accountToId);

            System.out.println(transfer.getTransferId() + "\t\t" + accountFromUsername + "\t\t/\t\t" +
                    accountToUsername + "\t\t" + transfer.getAmount());
        }
        System.out.println("-----------------------------------------------------");
        System.out.println();

        boolean isTransferIdValid = false;
        Long requestedTransferId;

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
                    break;
                }
            }

            if (!isTransferIdValid) {
                System.out.println("Invalid transfer ID. Please try again.");
            } else {
                break;
            }
        }

        Transfer requestedTransfer;
        try {
            requestedTransfer = transferService.getCurrentUserTransferById(requestedTransferId);
        } catch (Exception e) {
            System.out.println("Unable to retrieve transfer. Please try again.");
            return;
        }

        if (requestedTransfer != null) {
            System.out.println(requestedTransfer.toString());
        } else {
            System.out.println("Unable to retrieve transfer. Please try again.");
        }
    }

    public void listAllUsers(AuthenticatedUser currentUser) {
        User[] allUsers = userService.findAllUsers();
        System.out.println("-----------------------------------------------------");
        System.out.println("LIST OF AVAILABLE USERS");
        System.out.println("-----------------------------------------------------");
        System.out.println("USERS");
        System.out.println("ID\t\t\tNAME");
        System.out.println("-----------------------------------------------------");
        for (User user : allUsers) {
            if (user.getUserId().equals(currentUser.getUser().getUserId())){
                continue;
            }
            System.out.println(user.getUserId() + "\t\t" + user.getUsername());
        }
        System.out.println("-----------------------------------------------------");
        System.out.println();
    }
}
