package com.techelevator.tenmo.services;

import java.math.BigDecimal;

public interface ConsoleServiceInterface {

    Object getChoiceFromOptions(Object[] options);

    Object getChoiceFromUserInput(Object[] options);

    void displayMenuOptions(Object[] options);

    String getUserInput(String prompt);

    Integer getUserInputInteger(String prompt);

    BigDecimal getUserInputBigDecimal(String prompt);

    String displayAsCurrency(BigDecimal bigDecimal);


}
