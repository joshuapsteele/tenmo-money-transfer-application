package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // As an authenticated user of the system, I need to be able to see my Account Balance.
    @Override
    public BigDecimal viewBalance(Long userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public void increaseBalance(Long accountToId, BigDecimal amountToIncrease) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amountToIncrease, accountToId);
    }

    @Override
    public void decreaseBalance(Long accountFromId, BigDecimal amountToDecrease) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amountToDecrease, accountFromId);
    }


    @Override
    public void updateAccount(Account accountToUpdate) {
        String sql = "UPDATE account SET user_id = ?, balance = ?, WHERE account_id = ?;";
//        PUT THIS LINE BACK IN AFTER ACCOUNT CLASS IS CREATED
//        jdbcTemplate.update(sql, accountToUpdate.getUserId(), accountToUpdate.getBalance(), accountToUpdate.getAccountId());
    }

    @Override
    public void deleteAccount(Account accountToDelete) {
        String sql = "DELETE FROM account WHERE account_id = ?;";
//        PUT THIS LINE BACK IN AFTER ACCOUNT CLASS IS CREATED
//        jdbcTemplate.update(sql, accountToDelete.getAccountId());
    }
}