package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean create(Transfer transfer) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?);";
        return jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount()) == 1;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        List<Transfer> allTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            allTransfers.add(mapRowToTransfer(results));
        }
        return allTransfers;
    }

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

    @Override
    public Transfer getTransferByTransferId(Long transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public Transfer findCurrentUserTransferByTransferId(Long userId, Long transferId) {
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
    public boolean update(Long id, Transfer transferToUpdate) {
        String sql = "UPDATE transfers SET transfer_type_id = ?, transfer_status_id = ?, account_from = ?, " +
                "account_to = ?, amount = ? WHERE transfer_id = ?;";
        return jdbcTemplate.update(sql, transferToUpdate.getTransferTypeId(), transferToUpdate.getTransferStatusId(),
                transferToUpdate.getAccountFrom(), transferToUpdate.getAccountTo(), transferToUpdate.getAmount(), id) == 1;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM transfers WHERE transfer_id = ?;";
        return jdbcTemplate.update(sql, id) == 1;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
