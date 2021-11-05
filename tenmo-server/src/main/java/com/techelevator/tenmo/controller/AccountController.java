package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/accounts/")
public class AccountController {
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return accountDao.getAccounts();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable Long id) {
        return accountDao.getAccountById(id);
    }

    @RequestMapping(path = "user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable Long id) {
        return accountDao.getAccountByUserId(id);
    }

    // As an authenticated user of the system, I need to be able to see my Account Balance.

    @RequestMapping(path = "my-account-balance", method = RequestMethod.GET)
    public BigDecimal viewBalance(Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long userId = userDao.findIdByUsername(username);
        BigDecimal balance = accountDao.viewBalance(userId);
        return balance;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@Valid @RequestBody Account account) {
        return accountDao.create(account);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public boolean update(@Valid @PathVariable Long id, @RequestBody Account account) {
        return accountDao.update(id, account);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public boolean delete(@Valid @PathVariable Long id) {
        return accountDao.delete(id);
    }


}
