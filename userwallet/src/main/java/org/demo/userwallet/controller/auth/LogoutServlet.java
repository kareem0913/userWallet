package org.demo.userwallet.controller.auth;

import org.demo.userwallet.service.AuthService;
import org.demo.userwallet.service.impl.AuthServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());
    private AuthService authService;

   @Override
   public void init(){
       authService = new AuthServiceImpl();
   }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
       response.setContentType("application/json");
       String token = request.getHeader("Authorization").substring(7);
       authService.logout(token);
       response.setStatus(HttpServletResponse.SC_NO_CONTENT);
       try{
           response.getWriter().print("");
       }catch(IOException e){
           logger.log(Level.SEVERE, "Error while logging out", e);
       }
    }
}
