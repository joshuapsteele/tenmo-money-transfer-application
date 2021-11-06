package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountServiceInterface {
    // We need to determine where this method should be called from. Perhaps the App class?
    void setAuthToken(String authToken);

    Account getAccountByAccountId(Long accountId);

    Account getAccountByUserId(Long userId);

    String getUsernameByAccountId(Long accountId);

    BigDecimal getAccountBalanceByAccountId(Long accountId);

    BigDecimal getCurrentUserAccountBalance(Long accountId);

    boolean update(Account updatedAccount);
}
