package com.techelevator.tenmo.model;

<<<<<<< HEAD
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
        String sql = "SELECT * FROM transfers " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()){
            userTransfers.add(results);
        }
        return userTransfers;
    }
    
    public Transfer getTransferById(Long transferId){
        String sql = "SELECT * FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet requestTransfer = jdbcTemplate.queryForRowSet(sql, transferId);
        return requestTransfer;
    }
=======
public class Account {
>>>>>>> 30bb0483a16755930af0a54a7959cd4d67e9c4fb
}
