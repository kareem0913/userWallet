package org.demo.userwallet.service.impl;

import org.demo.userwallet.error.dto.GlobalError;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.GlobalResponse;
import org.demo.userwallet.model.dto.transaction.TransactionResponse;
import org.demo.userwallet.model.dto.transaction.TransferMoneyRequest;
import org.demo.userwallet.repository.TransactionRepository;
import org.demo.userwallet.repository.UserRepository;
import org.demo.userwallet.service.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = Logger.getLogger(TransactionServiceImpl.class.getName());
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl() {
        this.transactionRepository = new TransactionRepository();
        this.userRepository = new UserRepository();
    }

    @Override
    public ResponseEntity sentTransactions(Long id) {
        List<TransactionResponse> transactions = transactionRepository.findSentTransactions(id);

        return new GlobalResponse(
               200,
               "Transactions",
               transactions
        );
    }

    @Override
    public ResponseEntity receivedTransactions(Long id){
        List<TransactionResponse> transactions = transactionRepository.findReceivedTransactions(id);

        return new GlobalResponse(
                200,
                "Transactions",
                transactions
        );
    }

    @Override
    public ResponseEntity transferMoney(TransferMoneyRequest transferMoneyRequest, Long senderId) {
        try{
            Optional<Long> receiverId = userRepository.findUserIdByPhone(transferMoneyRequest.getReceiverPhone());
            if(!receiverId.isPresent()){
                return new GlobalError(
                        404,
                        "Receiver not found",
                        "Receiver not found"
                );
            }

            transactionRepository.transferMoney(senderId, receiverId.get(), transferMoneyRequest.getAmount());
            return new GlobalResponse(
                    200,
                    "Transfer successful",
                    null
            );
        }catch(RuntimeException e){
            logger.log(Level.SEVERE,e.getMessage(),e);
            return new GlobalError(
                    500,
                    "Transfer failed",
                    e.getMessage()
            );
        }
    }
}