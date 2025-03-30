package com.wiormiw.pokemon_in_home.service;

import com.wiormiw.pokemon_in_home.domain.entity.User;
import com.wiormiw.pokemon_in_home.dto.auth.AuthRequestDTO;
import com.wiormiw.pokemon_in_home.dto.auth.AuthResponseDTO;

public interface AuthService {
    public AuthResponseDTO register(AuthRequestDTO request);
    public AuthResponseDTO login(AuthRequestDTO request);
    public AuthResponseDTO refreshToken(String refreshToken);
    public void logout(String username);
}
