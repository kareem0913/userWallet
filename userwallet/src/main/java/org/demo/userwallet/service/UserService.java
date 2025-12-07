package org.demo.userwallet.service;

import org.demo.userwallet.model.ResponseEntity;

public interface UserService {
    public ResponseEntity userProfile(String email);
    public ResponseEntity findAllUsers();
}
