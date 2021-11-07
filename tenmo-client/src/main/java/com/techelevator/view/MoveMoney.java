package com.techelevator.view;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class MoveMoney {

    private ConsoleServiceInterface consoleService = new ConsoleService(System.in, System.out);
    private AccountServiceInterface accountService = new AccountService();
    private TransferServiceInterface transferService = new TransferService();
    private UserServiceInterface userService = new UserService();
    private TenmoCLI tenmoCLI = new TenmoCLI();

    private static final String PENDING_TRANSFER_MENU_OPTION_APPROVE = "Approve";
    private static final String PENDING_TRANSFER_MENU_OPTION_REJECT = "Reject";
    private static final String PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT = "Don't approve or reject (Exit)";
    private static final String[] PENDING_TRANSFER_MENU_OPTIONS =
            {PENDING_TRANSFER_MENU_OPTION_APPROVE, PENDING_TRANSFER_MENU_OPTION_REJECT, PENDING_TRANSFER_MENU_OPTION_DO_NOT_APPROVE_DO_NOT_REJECT};

    public void sendBucks(AuthenticatedUser currentUser) {
        tenmoCLI.listAllUsers();
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
            tenmoCLI.viewCurrentBalance(currentUser);
        }

    }

    public void requestBucks(AuthenticatedUser currentUser) {
        tenmoCLI.listAllUsers();

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


    public void viewPendingRequests(AuthenticatedUser currentUser) {
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
            tenmoCLI.viewCurrentBalance(currentUser);
            return;
        } else if (PENDING_TRANSFER_MENU_OPTION_REJECT.equals(choice)) {
            if (requestedTransfer != null) {
                requestedTransfer.setTransferStatusId(3);
            }
            transferService.updateTransfer(requestedTransfer);
            System.out.println("Transfer rejected!");
            tenmoCLI.viewCurrentBalance(currentUser);
            return;
        } else {
            return;
        }
    }


}
