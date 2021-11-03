package com.techelevator.tenmo.model;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Account {
//    private final JdbcTemplate jdbcTemplate;
    private final Long accountId;
    private final Long userId;
    private BigDecimal balance;

    public Account(Long accountId, Long userId, BigDecimal balance){
//        this.jdbcTemplate = jdbcTemplate;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /*
    THESE METHODS SHOULD GO ELSEWHERE, IN THE DAO AND CONTROLLER CLASSES
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
    }*/
}
