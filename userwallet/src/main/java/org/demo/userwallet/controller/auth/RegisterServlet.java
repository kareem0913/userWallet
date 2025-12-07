package org.demo.userwallet.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.User;
import org.demo.userwallet.service.AuthService;
import org.demo.userwallet.service.impl.AuthServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());
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
           User user = objectMapper.readValue(request.getReader(), User.class);
           ResponseEntity authResponse =  authService.register(user);
           response.getWriter().print(objectMapper.writeValueAsString(authResponse));
       }catch (IOException exception){
           logger.log(Level.SEVERE, "Error while registering user", exception);
       }
    }

}