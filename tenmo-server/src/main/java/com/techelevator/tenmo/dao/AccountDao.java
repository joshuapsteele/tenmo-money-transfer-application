package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAccounts();

    Account getAccountByAccountId(Long accountId);

    String getUsernameByAccountId(Long account_id);

    Account getAccountByUserId(Long userId);

    Long getUserIdByAccountId(Long id);

    BigDecimal viewBalanceByAccountId(Long accountId);

    // As an authenticated user of the system, I need to be able to see my Account Balance.
    BigDecimal viewBalanceByUserId(Long userId);

    boolean increaseBalance(Long accountToId, BigDecimal amountToIncrease);

    boolean decreaseBalance(Long accountFromId, BigDecimal amountToIncrease);

    boolean create(Account accountToCreate);

    boolean update(Long accountId, Account accountToUpdate);

    boolean delete(Long accountId);

}
