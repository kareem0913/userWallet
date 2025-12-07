package org.demo.userwallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.userwallet.error.dto.GlobalError;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.transaction.TransferMoneyRequest;
import org.demo.userwallet.service.TransactionService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.demo.userwallet.service.impl.TransactionServiceImpl;
import org.demo.userwallet.util.JwUtil;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TransactionServlet.class.getName());
    private ObjectMapper objectMapper;
    private TransactionService transactionService;

    @Override
    public void init(){
        objectMapper = new ObjectMapper();
        transactionService = new TransactionServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        String token = request.getHeader("Authorization").substring(7);
        Long id = JwUtil.getIdFromToken(token);
        String action = request.getParameter("action");
        ResponseEntity httpResponse;
        try{
            switch (action){
                case "sentTransactions":
                    httpResponse = transactionService.sentTransactions(id);
                    break;
                case "receivedTransactions":
                    httpResponse = transactionService.receivedTransactions(id);
                    break;
                default:
                    httpResponse = new GlobalError(400,
                            "Invalid action",
                            "Invalid action");
                    break;
            }
            response.getWriter().print(objectMapper.writeValueAsString(httpResponse));
        }catch (IOException e){
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        String token = request.getHeader("Authorization").substring(7);
        Long id = JwUtil.getIdFromToken(token);

        try{
            TransferMoneyRequest transferMoneyRequest = objectMapper.readValue(request.getReader(), TransferMoneyRequest.class);
            ResponseEntity httpResponse = transactionService.transferMoney(transferMoneyRequest, id);
            response.getWriter().print(objectMapper.writeValueAsString(httpResponse));
        }catch(IOException e){
            logger.log(Level.SEVERE, "Error while transferring money", e);
        }
    }
}
