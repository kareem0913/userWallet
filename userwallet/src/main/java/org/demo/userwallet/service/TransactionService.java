package org.demo.userwallet.service;

import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.transaction.TransferMoneyRequest;

public interface TransactionService {
    public ResponseEntity sentTransactions(Long id);
    public ResponseEntity transferMoney(TransferMoneyRequest transferMoneyRequest, Long senderId);
    public ResponseEntity receivedTransactions(Long id);
}
