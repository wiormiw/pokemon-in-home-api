package com.wiormiw.pokemon_in_home.service;

import com.wiormiw.pokemon_in_home.dto.auth.AuthRequestDTO;
import com.wiormiw.pokemon_in_home.dto.auth.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO register(AuthRequestDTO request);
    AuthResponseDTO login(AuthRequestDTO request);
    AuthResponseDTO refreshToken(String refreshToken);
    void logout(String username);
}
