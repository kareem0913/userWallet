package org.demo.userwallet.model.dto.auth;

import org.demo.userwallet.model.ResponseEntity;

public class AuthResponse implements ResponseEntity {
    private final Integer code;
    private final String message;
    private final String token;
    private final Object data;

    public AuthResponse(Integer code, String message, String token, Object data) {
        this.code = code;
        this.token = token;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public Object getData() {
        return data;
    }
}
