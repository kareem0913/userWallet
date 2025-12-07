package org.demo.userwallet.model.dto.transaction;

import java.math.BigDecimal;

public class TransferMoneyRequest {
    private String receiverPhone;
    private BigDecimal amount;

    public TransferMoneyRequest() {
    }

    public TransferMoneyRequest(String receiverPhone, BigDecimal amount) {
        this.receiverPhone = receiverPhone;
        this.amount = amount;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
