package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/transfers/")
public class TransferController {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Transfer> getTransfers() {
        return transferDao.getAllTransfers();
    }

    @RequestMapping(path = "my-transfers/{id}", method = RequestMethod.GET)
    public Transfer getCurrentUserTransferById(@PathVariable Long id, Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long user_id = userDao.findIdByUsername(username);
        return transferDao.findCurrentUserTransferByTransferId(user_id, id);
    }

    // TODO VALIDATE THE DATA (ACCOUNT IDS, BALANCE, AMOUNT, ETC.) BEFORE CHANGING ANY OF THE DATA
    // THROW EXCEPTION, 400 ERROR, MESSSAGE TO CLIENT

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@RequestBody Transfer transfer) throws TransferControllerException {
        Account accountFrom = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountId(transfer.getAccountTo());
        BigDecimal transferAmount = transfer.getAmount();

        if (accountFrom == null || accountTo == null) {
            throw new TransferControllerException("UNABLE TO RETRIEVE ACCOUNTS");
        } else if (transferAmount.compareTo(accountFrom.getBalance()) > 0) {
            throw new TransferControllerException("INSUFFICIENT FUNDS, TRANSFER AMOUNT GREATER THAN ACCOUNT BALANCE");
        }
        // If the if/else-if block above passes, we know that the accounts exist and that the account balance is sufficient.
        // We can therefore create the transfer in the database here with the following line.
        boolean wasCreated = transferDao.create(transfer);

        // However, before changing any account balances, we now need to check the status of the transfer.
        // Balances should only be changed if/when the transfer status is 2 for Approved.
        // If the status is 1 for Pending, we do nothing other than what we've already done
        // --create the transfer and log it in the database.
        // The only other status possibility right now is 3 for Rejected,
        // but that should only take place when UPDATING a transfer (below), not creating it.

        if (transfer.getTransferStatusId() == 2) {
            accountDao.decreaseBalance(transfer.getAccountFrom(), transferAmount);
            accountDao.increaseBalance(transfer.getAccountTo(), transferAmount);
        }

        return wasCreated;
        }

    @RequestMapping(path = "my-transfers", method = RequestMethod.GET)
    public List<Transfer> viewAllTransfersByUserId(Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long userId = userDao.findIdByUsername(username);
        return transferDao.viewAllTransfersByUserId(userId);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public boolean update(@PathVariable Long id, @RequestBody Transfer transfer) throws TransferControllerException {
        boolean wasUpdated = false;
        boolean isNewApproval = false;

        Account accountFrom = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountByAccountId(transfer.getAccountTo());
        BigDecimal transferAmount = transfer.getAmount();

        if (accountFrom == null || accountTo == null) {
            throw new TransferControllerException("UNABLE TO RETRIEVE ACCOUNTS");
        } else if (transferAmount.compareTo(accountFrom.getBalance()) > 0) {
            throw new TransferControllerException("INSUFFICIENT FUNDS, TRANSFER AMOUNT GREATER THAN ACCOUNT BALANCE");
        }

        // If the transfer to be updated in the database ALREADY has an approved status,
        // then just update the transfer and return true, without updating any account balances.
        if (transferDao.getTransferByTransferId(id).getTransferStatusId() == 2) {
            return transferDao.update(id, transfer);
        }

        wasUpdated = transferDao.update(id, transfer);

        // If the transfer that was just updated NOW has an approved status,
        // then adjust the relevant account balances here.
        if (transferDao.getTransferByTransferId(id).getTransferStatusId() == 2) {
            accountDao.decreaseBalance(transfer.getAccountFrom(), transferAmount);
            accountDao.increaseBalance(transfer.getAccountTo(), transferAmount);
        }

        return wasUpdated;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable Long id) {
        return transferDao.delete(id);
    }


//    I don't think we need this method below, because I think that the approval checks in CREATE and UPDATE above need to be slightly different.

//    private void isApproved(Transfer transfer, Account accountFrom, Account accountTo, BigDecimal transferAmount){
//        if (transferAmount.compareTo(accountFrom.getBalance()) == 2 && accountFrom != null) {
//            accountDao.decreaseBalance(accountFrom.getAccountId(), transferAmount);
//        }
//        if (transferAmount.compareTo(accountFrom.getBalance()) == 2 && accountTo != null) {
//            accountDao.increaseBalance(accountTo.getAccountId(), transferAmount);
//        }
//    }

}
