package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@Valid @RequestBody Transfer transfer) {
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
