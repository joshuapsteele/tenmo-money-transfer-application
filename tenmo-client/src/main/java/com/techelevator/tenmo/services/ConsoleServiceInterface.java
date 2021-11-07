package com.techelevator.tenmo.services;

import java.math.BigDecimal;

public interface ConsoleServiceInterface {

    public Object getChoiceFromOptions(Object[] options);
    public Object getChoiceFromUserInput(Object[] options);
    public void displayMenuOptions(Object[] options);
    public String getUserInput(String prompt);
    public Integer getUserInputInteger(String prompt);
    public BigDecimal getUserInputBigDecimal(String prompt);
    public String displayAsCurrency(BigDecimal bigDecimal);


}
