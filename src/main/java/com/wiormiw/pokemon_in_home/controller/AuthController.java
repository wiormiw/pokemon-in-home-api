package com.wiormiw.pokemon_in_home.controller;

import com.wiormiw.pokemon_in_home.dto.auth.AuthRequestDTO;
import com.wiormiw.pokemon_in_home.dto.auth.AuthResponseDTO;
import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import com.wiormiw.pokemon_in_home.security.jwt.JWTProvider;
import com.wiormiw.pokemon_in_home.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final JWTProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authServiceImpl.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = authServiceImpl.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.substring(7); // Remove "Bearer "
        AuthResponseDTO response = authServiceImpl.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpResponse<Void>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtProvider.getSubject(token);
        authServiceImpl.logout(username);
        return ResponseEntity.noContent().build();
    }
}
