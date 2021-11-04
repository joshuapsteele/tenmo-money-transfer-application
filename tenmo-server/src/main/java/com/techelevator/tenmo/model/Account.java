package com.techelevator.tenmo.model;

<<<<<<< HEAD
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
=======
>>>>>>> 43ac32892d7c411d472c3695b1c2e3ad2f9f5a3b
import java.math.BigDecimal;

public class Account {
<<<<<<< HEAD
//    private final JdbcTemplate jdbcTemplate;

    @Positive(message = "Account ID must be greater than 1")
    private final Long accountId;

    @Positive(message = "User ID must be greater than 1")
    private final Long userId;

    @Positive(message = "Balance cannot be negative")
=======
    private Long accountId;
    private Long userId;
>>>>>>> 43ac32892d7c411d472c3695b1c2e3ad2f9f5a3b
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
