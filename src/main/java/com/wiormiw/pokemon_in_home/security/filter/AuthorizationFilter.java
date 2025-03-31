package com.wiormiw.pokemon_in_home.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import com.wiormiw.pokemon_in_home.security.jwt.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.wiormiw.pokemon_in_home.security.constant.SecurityConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(TOKEN_PREFIX.length());
            String username = jwtProvider.getSubject(token);

            if (jwtProvider.isTokenValid(username, token, "access")) {
                List<GrantedAuthority> authorities = jwtProvider.getAuthorities(token);
                Authentication authentication = jwtProvider.getAuthentication(
                        username, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
                throw new JWTVerificationException(INVALID_TOKEN_MESSAGE);
            }
        } catch (JWTVerificationException e) {
            SecurityContextHolder.clearContext();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(FORBIDDEN.value());
            new ObjectMapper().writeValue(response.getOutputStream(),
                    HttpResponse.builder()
                            .statusCode(FORBIDDEN.value())
                            .status(FORBIDDEN)
                            .reason(FORBIDDEN.getReasonPhrase().toUpperCase())
                            .message(e.getMessage())
                            .build());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
