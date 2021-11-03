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
@RequestMapping("/transfers")
public class TransferController {
    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    /*
    As an authenticated user of the system, I need to be able to send a transfer of a specific amount of TE Bucks to a registered user.
    - I should be able to choose from a list of users to send TE Bucks to.
    - A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
    - The receiver's account balance is increased by the amount of the transfer.
    - The sender's account balance is decreased by the amount of the transfer.
    - I can't send more TE Bucks than I have in my account.
    - A Sending Transfer has an initial status of "approve."
    */

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer createSendMoneyTransfer(@Valid @RequestBody Transfer sendMoneyTransferToCreate) {
        accountDao.decreaseBalance(sendMoneyTransferToCreate.getAccountFrom, sendMoneyTransferToCreate.getAmountToTransfer);
        accountDao.increaseBalance(sendMoneyTransferToCreate.getAccountTo, sendMoneyTransferToCreate.getAmountToTransfer);
        return transferDao.createSendMoneyTransfer(sendMoneyTransferToCreate);
    }

    @RequestMapping(path = "my-transfers", method = RequestMethod.GET)
    public List<Transfer> viewAllTransfersByUserId(Principal whoIsLoggedIn) {
        String username = whoIsLoggedIn.getName();
        Long userId = userDao.findIdByUsername(username);
        return transferDao.viewAllTransfersByUserId(userId);
    }
}
