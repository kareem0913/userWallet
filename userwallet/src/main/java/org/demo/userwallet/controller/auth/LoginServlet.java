package org.demo.userwallet.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.auth.LoginRequest;
import org.demo.userwallet.service.AuthService;
import org.demo.userwallet.service.impl.AuthServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
   private ObjectMapper objectMapper;
   private AuthService authService;

   @Override
   public void init(){
       objectMapper = new ObjectMapper();
       authService = new AuthServiceImpl();
   }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
       response.setContentType("application/json");
       try {
           LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
           ResponseEntity authResponse = authService.login(loginRequest);
           response.getWriter().print(objectMapper.writeValueAsString(authResponse));
       }catch (IOException e){
           logger.log(Level.SEVERE, "Error while logging in", e);
       }
    }

}
