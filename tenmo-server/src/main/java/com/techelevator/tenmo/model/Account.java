package com.techelevator.tenmo.model;

import javax.validation.constraints.Positive;

import java.math.BigDecimal;

public class Account {

    @Positive(message = "Account ID must be greater than 1")
    private Long accountId;

    @Positive(message = "User ID must be greater than 1")
    private Long userId;

    @Positive(message = "Balance cannot be negative")
    private BigDecimal balance;

    public Account() {

    }

    public Account(Long accountId, Long userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
