package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/users")
public class UserController {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public UserController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public Map<Long, String> viewUserIdsAndUsernames() {
        return userDao.listUserIdsAndUsernames();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id) {
        return userDao.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public boolean create(@RequestBody String username, String password) {
        return userDao.create(username, password);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public boolean update(@PathVariable Long id, @RequestBody User user) {
        return userDao.update(id, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public boolean delete(@PathVariable Long id) {
        return userDao.delete(id);
    }

}