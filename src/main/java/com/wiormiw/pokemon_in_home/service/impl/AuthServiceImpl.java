package com.wiormiw.pokemon_in_home.service.impl;

import com.wiormiw.pokemon_in_home.domain.entity.Role;
import com.wiormiw.pokemon_in_home.domain.entity.User;
import com.wiormiw.pokemon_in_home.dto.auth.AuthRequestDTO;
import com.wiormiw.pokemon_in_home.dto.auth.AuthResponseDTO;
import com.wiormiw.pokemon_in_home.repository.RoleRepository;
import com.wiormiw.pokemon_in_home.repository.UserRepository;
import com.wiormiw.pokemon_in_home.security.jwt.JWTProvider;
import com.wiormiw.pokemon_in_home.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.wiormiw.pokemon_in_home.service.constant.ServiceConstant.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponseDTO register(AuthRequestDTO request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException(USERNAME_EXIST);
        }

        Role playerRole = roleRepository.findByName(Role.RoleType.PLAYER)
                .orElseThrow(() -> new IllegalStateException(ROLE_NOT_FOUND));

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(playerRole));
        userRepository.save(user);

        return generateTokens(user);
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        return generateTokens(user);
    }

    public AuthResponseDTO refreshToken(String refreshToken) {
        if (!jwtProvider.isTokenValid(jwtProvider.getSubject(refreshToken), refreshToken, "refresh_token")) {
            throw new RuntimeException(INVALID_REFRESH_TOKEN);
        }

        String username = jwtProvider.getSubject(refreshToken);
        String storedToken = redisTemplate.opsForValue().get("refresh:" + username);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException(NOT_FOUND_OR_EXPIRED_REFRESH_TOKEN);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));

        AuthResponseDTO response = generateTokens(user);

        redisTemplate.delete("refresh:" + username);

        return response;
    }

    public void logout(String username) {
        redisTemplate.delete("refresh:" + username);
    }

    private AuthResponseDTO generateTokens(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        redisTemplate.opsForValue().set(
                "refresh:" + user.getUsername(),
                refreshToken,
                jwtProvider.getExpirationTimeInMillis(refreshToken),
                TimeUnit.MILLISECONDS
        );

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                jwtProvider.getExpirationTimeInMillis(accessToken)
        );
    }
}
