package com.techelevator.tenmo.services.ServiceInterfaces;

import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;

public interface UserServiceInterface {
    void setAuthToken(String authToken);

    User[] findAllUsers();

    User findUserById(Long id);

    String getUsernameByAccountId(Long accountId);

    interface ConsoleServiceInterface {
        Object getChoiceFromOptions(Object[] options);

        String getUserInput(String prompt);

        Integer getUserInputInteger(String prompt);

        BigDecimal getUserInputBigDecimal(String prompt);

        String displayAsCurrency(BigDecimal bigDecimal);
    }
}
