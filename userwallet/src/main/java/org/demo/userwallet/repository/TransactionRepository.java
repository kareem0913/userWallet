package org.demo.userwallet.repository;

import org.demo.userwallet.model.dto.transaction.TransactionResponse;
import org.demo.userwallet.util.DatabaseUtil;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionRepository {
    private static final Logger logger = Logger.getLogger(TransactionRepository.class.getName());

    public void transferMoney(Long sender, Long receiver, BigDecimal amount){
        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement("call transfermoney(?, ?, ?)")){
            statement.setLong(1, sender);
            statement.setLong(2, receiver);
            statement.setBigDecimal(3, amount);
            statement.execute();
        }catch (PSQLException e){
            ServerErrorMessage error = e.getServerErrorMessage();
            String cleanMessage = "Database error";

            if (error != null) {
                cleanMessage = error.getMessage();
            }
            throw new RuntimeException(cleanMessage);
        }catch (SQLException e){}
    }

    public List<TransactionResponse> findSentTransactions(Long id){
        String sql = "select u.name as receiver_name, u.phone as receiver_phone, transactions.amount," +
                " transactions.status, transactions.details, transactions.created_at as transaction_date" +
                " from transactions inner join users u on transactions.receiver = u.id where transactions.sender = ?";

        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<TransactionResponse> transactions = new ArrayList<>();
            while(resultSet.next()){
                TransactionResponse transaction = new TransactionResponse(
                        resultSet.getString("receiver_name"),
                        resultSet.getString("receiver_phone"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getTimestamp("transaction_date"),
                        resultSet.getString("status"),
                        resultSet.getString("details")
                );
                transactions.add(transaction);
            }
            return transactions;
        }catch(SQLException e){
            logger.log(Level.SEVERE,e.getMessage(),e);
        }
        return Collections.emptyList();
    }

    public List<TransactionResponse> findReceivedTransactions(Long id){
        String sql = "select u.name as sender_name, u.phone as sender_phone, transactions.amount," +
                " transactions.status, transactions.details, transactions.created_at as transaction_date" +
                " from transactions inner join users u on transactions.sender = u.id where transactions.receiver = ?";

        try(Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<TransactionResponse> transactions = new ArrayList<>();
            while (resultSet.next()){
                TransactionResponse transaction = new TransactionResponse(
                        resultSet.getString("sender_name"),
                        resultSet.getString("sender_phone"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getTimestamp("transaction_date"),
                        resultSet.getString("status"),
                        resultSet.getString("details")
                );
                transactions.add(transaction);
            }
            return transactions;
        }catch(SQLException e){
            logger.log(Level.SEVERE, e.getMessage());
        }
        return Collections.emptyList();
    }

}
