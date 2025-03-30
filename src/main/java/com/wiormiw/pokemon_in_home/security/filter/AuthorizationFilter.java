package com.wiormiw.pokemon_in_home.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wiormiw.pokemon_in_home.security.jwt.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNullApi;

import java.io.IOException;
import java.util.List;

import static com.wiormiw.pokemon_in_home.security.constant.SecurityConstant.OPTIONS_HTTP_METHOD;
import static com.wiormiw.pokemon_in_home.security.constant.SecurityConstant.TOKEN_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Skip OPTIONS requests
        if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
            response.setStatus(OK.value());
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION);

        // Allow non-secured endpoints to pass through
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(TOKEN_PREFIX.length());
            String username = jwtProvider.getSubject(token);

            if (jwtProvider.isTokenValid(username, token)) {
                List<GrantedAuthority> authorities = jwtProvider.getAuthorities(token);
                Authentication authentication = jwtProvider.getAuthentication(
                        username,
                        authorities,
                        request
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } catch (JWTVerificationException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(FORBIDDEN.value(), "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
