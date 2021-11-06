package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;

public class ViewOptions {
    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS = {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};

    private AuthenticatedUser currentUser;
    private String currentUserToken;
    private ConsoleService console;
    private AuthenticationService authService;

    private AccountService accountService = new AccountService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();

    public ViewOptions(ConsoleService console, AuthenticationService authService){
        this.console = console;
        this.authService = authService;
    }

    public void viewCurrentBalance() {
        BigDecimal currentBalance = accountService.getCurrentUserAccountBalance(currentUser.getUser().getUserId());
        String currentBalanceFormatted = console.displayAsCurrency(currentBalance);
        System.out.println("Your current balance is " + currentBalanceFormatted);
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    public void viewTransferHistory() {
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
        viewTransferDetails();
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    public void viewTransferDetails() {
        String prompt = "For further details on a transfer, enter its ID " +
                "(otherwise, press '0' to exit)";
        Long request = Long.valueOf(console.getUserInputInteger(prompt));
        if (request == 0) {
            return;
        }
        Transfer requestedTransfer = transferService.getCurrentUserTransferById(request);
        if (requestedTransfer != null) {
            System.out.println(requestedTransfer.toString());
        } else {
            System.out.println("Unable to retrieve transfer.");
        }

    }

    public void viewPendingRequests() {
        Transfer[] allTransfersForCurrentUser = transferService.listAllTransfersCurrentUser();

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\tPending Request Transfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
        for (Transfer transfer : allTransfersForCurrentUser) {
            Long accountFromId = transfer.getAccountFrom();
            Long currentUserId = currentUser.getUser().getUserId();
            Long currentUserAccountId = accountService.getAccountByUserId(currentUserId).getAccountId();
            if (transfer.getTransferStatusId() == 1 && accountFromId.equals(currentUserAccountId)) {
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

        Transfer requestedTransfer = transferService.getCurrentUserTransferById(request);

        while (true) {
            String choice = (String) console.getChoiceFromOptions(PENDING_TRANSFER_MENU_OPTIONS);
            if (PENDING_TRANSFER_MENU_OPTION_APPROVE.equals(choice)) {
                requestedTransfer.setTransferStatusId(2);
                transferService.updateTransfer(requestedTransfer);
                System.out.println("Transfer approved!");
                viewCurrentBalance();
                return;

            } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
                requestedTransfer.setTransferStatusId(3);
                transferService.updateTransfer(requestedTransfer);
                System.out.println("Transfer rejected!");
                viewCurrentBalance();
                return;

            } else {
                return;
            }
        }
    }
}
