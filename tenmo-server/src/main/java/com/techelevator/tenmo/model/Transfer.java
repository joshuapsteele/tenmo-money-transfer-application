package com.techelevator.tenmo.model;

<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestBody;

public class Transfer {
    private int transferId;
    private int accountFrom;
    private int accountTo;
    private Double amount;
    private boolean transferType;
    private String transferStatus;

    public Transfer(boolean transferType, String transferStatus){
        this.transferType = transferType;
        this.transferStatus = transferStatus;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isTransferType() {
        return transferType;
    }

    public boolean getTransferType() {
        return transferType;
    }

    public void setTransferType(boolean transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
=======
public class Transfer {
>>>>>>> 30bb0483a16755930af0a54a7959cd4d67e9c4fb
}
