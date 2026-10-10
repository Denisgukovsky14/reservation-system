package com.designhub;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private static final String TEST_SECRET =
            Base64.getEncoder().encodeToString("0123456789abcdef0123456789abcdef".getBytes());

    private final JwtService jwtService = new JwtService(TEST_SECRET, 60_000);

    @Test
    void generatedTokenContainsUserIdAndType() {
        String token = jwtService.generateToken(5L, "customer");

        Claims claims = jwtService.parseToken(token);

        assertEquals("5", claims.getSubject());
        assertEquals("customer", claims.get("userType", String.class));
    }

    @Test
    void tamperedTokenIsRejected() {
        String token = jwtService.generateToken(5L, "customer");
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertThrows(JwtException.class, () -> jwtService.parseToken(tampered));
    }

    @Test
    void expiredTokenIsRejected() {
        JwtService expiring = new JwtService(TEST_SECRET, -1_000);
        String token = expiring.generateToken(5L, "customer");

        assertThrows(JwtException.class, () -> expiring.parseToken(token));
    }
}
