
package com.hltech.authservice.auth;

import java.util.UUID;

public record AuthResponse(
        String message,
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        UUID userId,
        String fullName,
        String email,
        String phoneNumber
) {
}
