package org.demo.userwallet.service;

import org.demo.userwallet.model.User;
import org.demo.userwallet.model.ResponseEntity;
import org.demo.userwallet.model.dto.auth.LoginRequest;

public interface AuthService {
     ResponseEntity register(User user);
     ResponseEntity login(LoginRequest loginRequest);
     void logout(String token);
}
