package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpEntity;

import java.math.BigDecimal;

public interface AccountServiceInterface {

    void setAuthToken(String authToken);

    Account getAccountByAccountId(Long accountId);

    Account getAccountByUserId(Long userId);

    String getUsernameByAccountId(Long accountId);

    BigDecimal getAccountBalanceByAccountId(Long accountId);

    BigDecimal getCurrentUserAccountBalance(Long userId);

    boolean update(Account updatedAccount);

    HttpEntity<Void> makeAuthEntity();

    HttpEntity<Account> makeAccountEntity(Account account);
}
