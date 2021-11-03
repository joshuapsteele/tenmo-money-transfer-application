package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createSendMoneyTransfer(int fromUserId, int toUserId, BigDecimal amountToTransfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id";
        int newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId, amountToTransfer);

        return findTransferByTransferId(newTransferId);
    }

    @Override
    public List<Transfer> viewAllTransfersByUserId(int userId) {
        return null;
    }

    @Override
    public Transfer findTransferByTransferId(int transferId) {
        return null;
    }

    @Override
    public Transfer update(Transfer transferToUpdate) {
        return null;
    }

    @Override
    public boolean delete(Transfer transferToDelete) {
        return false;
    }
}
