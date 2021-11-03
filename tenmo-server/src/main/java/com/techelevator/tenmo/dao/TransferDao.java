package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer createSendMoneyTransfer(int fromUserId, int toUserId, BigDecimal amountToTransfer);

    List<Transfer> viewAllTransfersByUserId(int userId);

    Transfer findTransferByTransferId(int transferId);

    Transfer update(Transfer transferToUpdate);

    boolean delete(Transfer transferToDelete);

}
