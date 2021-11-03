package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewBalance(int userId);

    void update(Account accountToUpdate);

    void delete(Account accountToDelete);

}
