package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAccounts();

    Account getAccountById(Long id);

    Long getUserIdByAccountId(Long id);

    BigDecimal viewBalance(Long userId);

    boolean increaseBalance(Long accountToId, BigDecimal amountToIncrease);

    boolean decreaseBalance(Long accountFromId, BigDecimal amountToIncrease);

    boolean create(Account accountToCreate);

    boolean update(Long accountId, Account accountToUpdate);

    boolean delete(Long accountId);

}
