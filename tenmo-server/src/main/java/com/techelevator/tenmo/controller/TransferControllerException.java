package com.techelevator.tenmo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TransferControllerException extends Exception {
    public TransferControllerException() {
        super();
    }

    public TransferControllerException(String error) {
        super(error);
    }
}
