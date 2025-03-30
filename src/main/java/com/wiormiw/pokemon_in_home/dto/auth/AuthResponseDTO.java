package com.wiormiw.pokemon_in_home.dto.auth;

public record AuthResponseDTO(
        String accessCode,
        String refreshCode,
        long expiresAt
) {
}
