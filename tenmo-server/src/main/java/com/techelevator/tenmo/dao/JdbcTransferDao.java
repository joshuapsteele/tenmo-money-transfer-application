package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    As an authenticated user of the system, I need to be able to send a transfer of a specific amount of TE Bucks to a registered user.
    - I should be able to choose from a list of users to send TE Bucks to.
    - A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
    - The receiver's account balance is increased by the amount of the transfer.
    - The sender's account balance is decreased by the amount of the transfer.
    - I can't send more TE Bucks than I have in my account.
    - A Sending Transfer has an initial status of "approve."
    */

    @Override
    public Transfer createSendMoneyTransfer(Transfer sendMoneyTransferToCreate) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id";
        Long newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, sendMoneyTransferToCreate.getAccountFrom,
                sendMoneyTransferToCreate.getAccountTo, sendMoneyTransferToCreate.getAmountToTransfer).longValue();
        return findTransferByTransferId(sendMoneyTransferToCreate.getAccountFrom, newTransferId);
    }

    // As an authenticated user of the system, I need to be able to see transfers I have sent or received.
    @Override
    public List<Transfer> viewAllTransfersByUserId(Long userId) {
        List<Transfer> allTransfersByUserId = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers JOIN accounts ON account_from = account_id OR account_to = account_id WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            allTransfersByUserId.add(mapRowToTransfer(results));
        }
        return allTransfersByUserId;
    }

    // As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
    @Override
    public Transfer findTransferByTransferId(Long userId, Long transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers JOIN accounts ON account_from = account_id OR account_to = account_id WHERE user_id = ? AND transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public void update(Transfer transferToUpdate) {
        String sql = "UPDATE transfers SET transfer_type_id = ?, transfer_status_id = ?, account_from = ?, " +
                "account_to = ?, amount = ? WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transferToUpdate.getTransferTypeId, transferToUpdate.getTransferStatusId,
                transferToUpdate.getAccountFrom, transferToUpdate.getAccountTo, transferToUpdate.getAmount);
    }

    @Override
    public void delete(Long transferId) {
        String sql = "DELETE FROM transfers WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rs.getLong("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
