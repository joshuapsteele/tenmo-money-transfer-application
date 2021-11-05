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

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable Long id, Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long user_id = userDao.findIdByUsername(username);
        return transferDao.findTransferByTransferId(user_id, id);
    }

    // TODO VALIDATE THE DATA (ACCOUNT IDS, BALANCE, AMOUNT, ETC.) BEFORE CHANGING ANY OF THE DATA
    // THROW EXCEPTION, 400 ERROR, MESSSAGE TO CLIENT

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@RequestBody Transfer transfer) {
        Account accountFrom = accountDao.getAccountById(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountById(transfer.getAccountTo());
        BigDecimal transferAmount = transfer.getAmount();

        if (accountFrom == null || accountTo == null) {
            // SEND 400 BAD REQUEST + MESSAGE: "UNABLE TO RETRIEVE ACCOUNTS"
            // CUSTOM EXCEPTION?
        } else if (transferAmount.compareTo(accountFrom.getBalance()) == 1) {
            // SEND 400 BAD REQUEST + MESSAGE: "INSUFFICIENT FUNDS, TRANSFER AMOUNT GREATER THAN ACCOUNT BALANCE"
            // CUSTOM EXCEPTION?
        }

        // TODO: CREATE TRANSFER FIRST, AND THEN LATER CHECK STATUS BEFORE BALANCE CHANGES.

        if (transfer.getTransferStatusId() == 2 && accountFrom != null) {
            accountDao.decreaseBalance(accountFrom.getAccountId(), transferAmount);
        }
        if (transfer.getTransferStatusId() == 2 && accountTo != null) {
            accountDao.increaseBalance(accountTo.getAccountId(), transferAmount);
        }
        return transferDao.create(transfer);
    }

    @RequestMapping(path = "my-transfers", method = RequestMethod.GET)
    public List<Transfer> viewAllTransfersByUserId(Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long userId = userDao.findIdByUsername(username);
        return transferDao.viewAllTransfersByUserId(userId);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public boolean update(@PathVariable Long id, @RequestBody Transfer transfer) {


        return transferDao.update(id, transfer);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable Long id) {
        return transferDao.delete(id);
    }

}
