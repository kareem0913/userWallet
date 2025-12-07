package org.demo.userwallet.service.impl;

import org.demo.userwallet.error.dto.GlobalError;
import org.demo.userwallet.model.User;
import org.demo.userwallet.model.dto.auth.AuthResponse;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.auth.LoginRequest;
import org.demo.userwallet.repository.JwtTokenRepository;
import org.demo.userwallet.repository.UserRepository;
import org.demo.userwallet.service.AuthService;
import org.demo.userwallet.util.JwUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthServiceImpl implements AuthService {
    private final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;

    public AuthServiceImpl() {
        this.userRepository = new UserRepository();
        this.jwtTokenRepository = new JwtTokenRepository();
    }

    @Override
    public ResponseEntity register(final User registerUser) {
        boolean userExists = userRepository.userExistByEmailOrPhone(registerUser.getEmail(), registerUser.getPhone());

        if (userExists) {
            logger.log(Level.SEVERE, "User already exists");
            return new GlobalError(409,
                    "User already exists",
                    "User With Email " + registerUser.getEmail() + " Already Exists");
        }

        String hashedPassword = BCrypt.hashpw(registerUser.getPassword(), BCrypt.gensalt());
        registerUser.setPassword(hashedPassword);
        registerUser.setWalletBalance(100.0); // static balance
        User savedUser = userRepository.save(registerUser);

        String token = JwUtil.generateToken(savedUser.getEmail(), savedUser.getId());
        jwtTokenRepository.save(token, savedUser.getId());
        return new AuthResponse(201,
                "User registered successfully",
                token,
                savedUser
        );
    }

    @Override
    public ResponseEntity login(final LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if(!user.isPresent()){
            return new GlobalError(404,
                    "User not found",
                    "User With Email " + loginRequest.getEmail() + " Not Found");
        }

        if(!user.get().getStatus()){
            return new GlobalError(401,
                    "User not active",
                    "User With Email " + loginRequest.getEmail() + " Not Active");
        }

        if(!BCrypt.checkpw(loginRequest.getPassword(), user.get().getPassword())){
            return new GlobalError(401,
                    "Invalid credentials",
                    "Invalid user credentials");
        }

        String token = JwUtil.generateToken(user.get().getEmail(), user.get().getId());
        jwtTokenRepository.save(token, user.get().getId());
        return new AuthResponse(200,
                "User registered successfully",
                token,
                user.get()
        );
    }

    @Override
    public void logout(String token) {
        jwtTokenRepository.delete(token);
    }
}
