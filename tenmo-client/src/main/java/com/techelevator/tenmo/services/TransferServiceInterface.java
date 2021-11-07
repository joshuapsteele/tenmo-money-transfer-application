package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;

public interface TransferServiceInterface {
    void setAuthToken(String authToken);

    boolean createTransfer(Transfer newTransfer);

    Transfer getCurrentUserTransferById(Long transferId);

    Transfer[] listAllTransfers();

    Transfer[] listAllTransfersCurrentUser();

    boolean updateTransfer(Transfer transfer);

    HttpEntity<Void> makeAuthEntity();

    HttpEntity<Transfer> makeTransferEntity(Transfer transfer);
}
