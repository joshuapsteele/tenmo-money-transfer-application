package com.techelevator.tenmo.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final JdbcTemplate jdbcTemplate;
    private final Long accountId;
    private final Long userId;
    private Double balance;

    public Account(JdbcTemplate jdbcTemplate, Long accountId, Long userId, Double balance){
        this.jdbcTemplate = jdbcTemplate;
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List getTransfers(){
        List<SqlRowSet> userTransfers = new ArrayList<>();
        String sql = "SELECT transfers FROM transfers " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()){
            userTransfers.add(results);
        }
        return userTransfers;
    }
    
    public Transfer getTransferById(){
        Transfer requestTransfer = null;
        return requestTransfer;
    }
}
