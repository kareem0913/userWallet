package org.demo.userwallet.service.impl;

import org.demo.userwallet.error.dto.GlobalError;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.User;
import org.demo.userwallet.model.dto.GlobalResponse;
import org.demo.userwallet.repository.UserRepository;
import org.demo.userwallet.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService{

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }

    @Override
    public ResponseEntity userProfile(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if(!user.isPresent()){
            logger.log(Level.SEVERE, "User not found");
            return new GlobalError(404,
                    "User not found",
                    "User With Email " + email + " Not Found");
        }
        return new GlobalResponse(200,
                "User profile",
                user.get());
    }

    @Override
    public ResponseEntity findAllUsers() {
        List<User> users = userRepository.findAll();

        return new GlobalResponse(200,
                "All users",
                users);
    }
}
