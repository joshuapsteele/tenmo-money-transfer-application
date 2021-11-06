package com.techelevator.view;

import java.math.BigDecimal;

public interface ConsoleServiceInterface {
    Object getChoiceFromOptions(Object[] options);

    String getUserInput(String prompt);

    Integer getUserInputInteger(String prompt);

    BigDecimal getUserInputBigDecimal(String prompt);

    String displayAsCurrency(BigDecimal bigDecimal);
}
