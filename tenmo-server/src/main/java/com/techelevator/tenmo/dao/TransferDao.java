package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    boolean create(Transfer transfer);

    List<Transfer> getAllTransfers();

    List<Transfer> viewAllTransfersByUserId(Long userId);

    Transfer getTransferByTransferId(Long transferId);

    Transfer findCurrentUserTransferByTransferId(Long userId, Long transferId);

    boolean update(Long id, Transfer transferToUpdate);

    boolean delete(Long id);

    /*
    As an authenticated user of the system, I need to be able to request a transfer of a specific amount of TE Bucks from another registered user.
    I should be able to choose from a list of users to request TE Bucks from.
    A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
    A Request Transfer has an initial status of "pending."
    No account balance changes until the request is approved.
    The transfer request should appear in both users' list of transfers (use case #5).

    As an authenticated user of the system, I need to be able to see my "pending" transfers.

    As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
    I can't "approve" a given Request Transfer for more TE Bucks than I have in my account.
    The Request Transfer status is "approved" if I approve, or "rejected" if I reject the request.
    If the transfer is approved, the requester's account balance is increased by the amount of the request.
    If the transfer is approved, the requestee's account balance is decreased by the amount of the request.
    If the transfer is rejected, no account balance changes.
    */

}
