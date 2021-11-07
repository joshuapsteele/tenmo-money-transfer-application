package com.techelevator.view;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.ServiceInterfaces.AccountServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.AuthenticationServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.TransferServiceInterface;
import com.techelevator.tenmo.services.ServiceInterfaces.UserServiceInterface;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;

public class ViewOptions {
    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS = {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};

    public ViewOptions(){}

    public void viewCurrentBalance(AccountServiceInterface accountServiceInterface, AuthenticatedUser currentUser, ConsoleService console) {
        BigDecimal currentBalance = accountServiceInterface.getCurrentUserAccountBalance(currentUser.getUser().getUserId());
        String currentBalanceFormatted = console.displayAsCurrency(currentBalance);
        System.out.println("Your current balance is " + currentBalanceFormatted);
    }

    // TODO: MOVE TO CONSOLEUSERINTERFACE

    public void viewTransferHistory(AccountServiceInterface accountServiceInterface,
                                    TransferServiceInterface transferServiceInterface) {
        Transfer[] transfers = transferServiceInterface.listAllTransfersCurrentUser();
        if (transfers == null || transfers.length == 0) {
            System.out.println("Unable to retrieve transfer history.");
            return;
        }

        System.out.println("-------------------------------------------");
        System.out.println("\t\t\t\tTransfers");
        System.out.println("ID\t\tFrom/to\t\tAmount");
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

    public void viewTransferDetails(AccountServiceInterface accountServiceInterface, AuthenticatedUser currentUser,
                                    TransferServiceInterface transferServiceInterface, ConsoleService console) {
        viewTransferHistory(accountServiceInterface, transferServiceInterface);
        String prompt = "For further details on a transfer, enter its ID " +
                "(otherwise, press '0' to exit)";
        Long request = Long.valueOf(console.getUserInputInteger(prompt));
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
                viewCurrentBalance(accountServiceInterface, currentUser, console);
                return;

            } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
                requestedTransfer.setTransferStatusId(3);
                transferServiceInterface.updateTransfer(requestedTransfer);
                System.out.println("Transfer rejected!");
                viewCurrentBalance(accountServiceInterface, currentUser, console);
                return;

            } else {
                return;
            }
        }
    }
}
