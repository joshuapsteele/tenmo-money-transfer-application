package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Transfer {
    private Long transferId;
    private Long accountFrom;
    private Long accountTo;
    private BigDecimal amount;
    private int transferTypeId;
    private int transferStatusId;

    public Transfer() {
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

    public String displayAsCurrency(BigDecimal bigDecimal) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(bigDecimal);
    }

    public String displayTransferType(Integer transferTypeId) {
        if (transferTypeId == 1) {
            return "Request Money";
        } else if (transferTypeId == 2) {
            return "Send Money";
        }
        return transferTypeId.toString();
    }

    public String displayTransferStatus(Integer transferStatusId) {
        if (transferStatusId == 1) {
            return "Pending";
        } else if (transferStatusId == 2) {
            return "Approved";
        } else if (transferStatusId == 3) {
            return "Rejected";
        } else {
            return transferStatusId.toString();
        }
    }

    @Override
    public String toString() {

        return "\n--------------------------------------------" +
                "\n Transfer Details: " +
                "\n--------------------------------------------" +
                "\n Transfer ID:    " + transferId +
                "\n From Account:   " + accountFrom +
                "\n To Account:     " + accountTo +
                "\n Type:           " + displayTransferType(transferTypeId) +
                "\n Status:         " + displayTransferStatus(transferStatusId) +
                "\n Amount:         " + displayAsCurrency(amount) +
                "\n--------------------------------------------";
    }
}
