package com.techelevator.tenmo.services.ServiceInterfaces;

import com.techelevator.tenmo.model.Transfer;

public interface TransferServiceInterface {
    void setAuthToken(String authToken);

    boolean createTransfer(Transfer newTransfer);

    Transfer getCurrentUserTransferById(Long transferId);

    Transfer[] listAllTransfers();

    Transfer[] listAllTransfersCurrentUser();

    boolean updateTransfer(Transfer transfer);
}
