package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    boolean create(Transfer transfer);

    List<Transfer> getAllTransfers();

    List<Transfer> viewAllTransfersByUserId(Long userId);

    Transfer getTransferByTransferId(Long transferId);

    Transfer findCurrentUserTransferByTransferId(Long userId, Long transferId);

    boolean update(Long id, Transfer transferToUpdate);

    boolean delete(Long id);

}
