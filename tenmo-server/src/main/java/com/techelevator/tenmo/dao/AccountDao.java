package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal viewBalance(int userId);

    Account create(Account accountToCreate);

    Account update(Account accountToUpdate);

    Account delete(Account accountToDelete);

}
