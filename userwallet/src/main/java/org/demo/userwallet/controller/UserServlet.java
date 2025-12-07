package org.demo.userwallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.demo.userwallet.error.dto.GlobalError;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.service.UserService;
import org.demo.userwallet.service.impl.UserServiceImpl;
import org.demo.userwallet.util.JwUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(UserServlet.class.getName());
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init(){
        userService = new UserServiceImpl();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json");
        String action = request.getParameter("action");
        String token = request.getHeader("Authorization").substring(7);
        String email = JwUtil.getUsernameFromToken(token);

        ResponseEntity httpResponse;

        switch (action){
            case "profile":
                httpResponse = userService.userProfile(email);
                break;
            case "allUsers":
                httpResponse = userService.findAllUsers();
                break;
            default:
                httpResponse = new GlobalError(400,
                        "Invalid action",
                        "Invalid action");
                break;
        }

        try {
            response.getWriter().print(objectMapper.writeValueAsString(httpResponse));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while getting user profile", e);
        }

    }
}
