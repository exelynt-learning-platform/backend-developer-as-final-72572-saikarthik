package com.example.resource_booking_system.security;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenProviderTest {

    @Test
    void generatedTokenContainsUsernameAndRoles() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "local-development-secret-change-in-production",
                86_400_000L);

        String token = provider.generateToken("admin", List.of("ROLE_ADMIN"));

        assertEquals("admin", provider.extractUsername(token));
        assertEquals(List.of("ROLE_ADMIN"), provider.extractRole(token));
        assertTrue(provider.validToken(token, "admin"));
    }
}
