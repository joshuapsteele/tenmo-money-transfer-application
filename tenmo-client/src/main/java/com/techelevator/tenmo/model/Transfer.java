package com.techelevator.tenmo.model;

import io.cucumber.core.internal.gherkin.StringUtils;

import java.math.BigDecimal;

public class Transfer {
    private Long transferId;
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
    private int transferTypeId;
    private int transferStatusId;

    public Transfer(){

    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    @Override
    public String toString(){
        return "\n--------------------------------------------" +
                "\n Transfer Details: " +
                "\n--------------------------------------------" +
                "\n ID:     " + transferId +
                "\n From:   " + accountFrom +
                "\n To:     " + accountTo +
                "\n Type:   " + transferTypeId +
                "\n Status: " + transferStatusId +
                "\n Amount: " + amount +
                "\n--------------------------------------------";
    }
}
