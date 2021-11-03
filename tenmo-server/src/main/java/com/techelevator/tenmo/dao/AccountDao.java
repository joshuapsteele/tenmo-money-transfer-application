package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewBalance(Long userId);

    void increaseBalance(Long accountToId, BigDecimal amountToIncrease);

    void decreaseBalance(Long accountFromId, BigDecimal amountToIncrease);

    void updateAccount(Account accountToUpdate);

    void deleteAccount(Account accountToDelete);

}
