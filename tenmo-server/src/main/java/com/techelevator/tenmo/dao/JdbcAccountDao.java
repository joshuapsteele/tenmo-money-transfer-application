package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allAccounts.add(mapRowToAccount(results));
        }
        return allAccounts;
    }

    @Override
    public Account getAccountById(Long id) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public Long getUserIdByAccountId(Long id) {
        String sql = "SELECT user_id FROM accounts WHERE account_id = ?;";
        Long user_id = jdbcTemplate.queryForObject(sql, Long.class, id);
        return user_id;
    }

    // As an authenticated user of the system, I need to be able to see my Account Balance.
    @Override
    public BigDecimal viewBalance(Long userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public boolean increaseBalance(Long accountToId, BigDecimal amountToIncrease) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?;";
        return jdbcTemplate.update(sql, amountToIncrease, accountToId) == 1;
    }

    @Override
    public boolean decreaseBalance(Long accountFromId, BigDecimal amountToDecrease) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?;";
        return jdbcTemplate.update(sql, amountToDecrease, accountFromId) == 1;
    }

    @Override
    public boolean create(Account accountToCreate) {
        String sql = "INSERT INTO accounts (account_id, user_id, balance) VALUES (DEFAULT, ?, ?);";
        return jdbcTemplate.update(sql, accountToCreate.getUserId(), accountToCreate.getBalance()) == 1;
    }

    @Override
    public boolean update(Long accountId, Account accountToUpdate) {
        String sql = "UPDATE account SET user_id = ?, balance = ?, WHERE account_id = ?;";
        return jdbcTemplate.update(sql, accountToUpdate.getUserId(), accountToUpdate.getBalance(), accountId) == 1;
    }

    @Override
    public boolean delete(Long accountId) {
        String sql = "DELETE FROM account WHERE account_id = ?;";
        return jdbcTemplate.update(sql, accountId) == 1;
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getLong("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
