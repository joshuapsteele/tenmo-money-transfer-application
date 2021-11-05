package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {
    private Long transfer_id;
    @NotBlank(message = "The accountFrom field is required.")
    private Long account_from;
    @NotBlank(message = "The accountTo field is required.")
    private Long account_to;
    @Positive(message = "The amount to transfer must be greater than 0.")
    private BigDecimal amount;
    @NotBlank(message = "The transferTypeId field is required.")
    private int transfer_type_id;
    @NotBlank(message = "The transferStatusIs field is required.")
    private int transfer_status_id;

    public Transfer(){
    }

    public Long getTransferId() {
        return transfer_id;
    }

    public void setTransferId(Long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public Long getAccountFrom() {
        return account_from;
    }

    public void setAccountFrom(Long account_from) {
        this.account_from = account_from;
    }

    public Long getAccountTo() {
        return account_to;
    }

    public void setAccountTo(Long account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferTypeId() {
        return transfer_type_id;
    }

    public void setTransferTypeId(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransferStatusId() {
        return transfer_status_id;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transfer_status_id = transferStatusId;
    }
}
