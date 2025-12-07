package org.demo.userwallet.model.dto.transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionResponse {
    private final String name;
    private final String phone;
    private final BigDecimal amount;
    private final Timestamp transactionDate;
    private final String status;
    private final String details;

    public TransactionResponse(String name, String phone, BigDecimal amount, Timestamp transactionDate,
                               String status, String details) {
        this.name = name;
        this.phone = phone;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.status = status;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}
