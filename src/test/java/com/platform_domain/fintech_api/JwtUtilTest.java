package com.platform_domain.fintech_api;

import com.platform_domain.fintech_api.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generatesAndValidatesToken() {
        String token = jwtUtil.generateToken("test@mail.com");
        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals("test@mail.com", jwtUtil.extractEmail(token));
    }

    @Test
    void rejectsGarbageToken() {
        assertFalse(jwtUtil.isTokenValid("not.a.token"));
    }
}
