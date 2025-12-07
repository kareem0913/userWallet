package org.demo.userwallet.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwUtil {
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("jwt-secret-key-for-userwallet-jwt-secret-key-for-userwallet".getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 864_000_000;

    private JwUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generateToken(String username, Long id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    private static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    public static Long getIdFromToken(String token) {
        return parseToken(token).get("id", Long.class);
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
