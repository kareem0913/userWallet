package org.demo.userwallet.model;

public class JwtToken {
    private String token;
    private Long userId;

    public JwtToken(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public JwtToken() {
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }
}
