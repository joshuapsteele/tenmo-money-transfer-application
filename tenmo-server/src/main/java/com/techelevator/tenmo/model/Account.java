package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.math.BigDecimal;

public class Account {

    private Long account_id;
    @NotBlank(message = "The userId field is required.")
    private Long user_id;
    @Positive(message = "The balance cannot be negative")
    private BigDecimal balance;

    public Account() {

    }

    public Account(Long account_id, Long user_id, BigDecimal balance) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.balance = balance;
    }

    public Long getAccountId() {
        return account_id;
    }

    public void setAccountId(Long account_id) {
        this.account_id = account_id;
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
