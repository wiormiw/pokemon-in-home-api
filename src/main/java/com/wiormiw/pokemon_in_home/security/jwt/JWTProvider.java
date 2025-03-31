package com.wiormiw.pokemon_in_home.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wiormiw.pokemon_in_home.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.wiormiw.pokemon_in_home.security.constant.SecurityConstant.*;

@Component
public class JWTProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(User user) {
        return JWT.create().
                withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withClaim("roles", user.getRoles().stream()
                        .map(role -> ROLE_PREFIX + role.getName())
                        .collect(Collectors.toList()))
                .withClaim("token_type", "access")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    public String generateRefreshToken(User user) {
        return JWT.create().
                withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withIssuedAt(new Date())
                .withSubject(user.getUsername())
                .withClaim("token_type", "refresh_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes(StandardCharsets.UTF_8)));
    }

    // getting authorities from the token
    public List<GrantedAuthority> getAuthorities(String token) {
        List<String> claims = getClaimsFromToken(token);
        return claims.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Authentication getAuthentication(
            String username,
            List<GrantedAuthority> authorities,
            HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authToken;
    }

    public boolean isTokenValid(String username, String token, String expectedTokenType) {
        try {
            JWTVerifier verifier = getJWTVerifier();
            DecodedJWT decodedJWT = verifier.verify(token);
            return StringUtils.isNotEmpty(username) &&
                    username.equals(decodedJWT.getSubject()) &&
                    decodedJWT.getClaim("token_type").asString().equals(expectedTokenType) &&
                    !isTokenExpired(decodedJWT);
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        DecodedJWT decodedJWT = getJWTVerifier().verify(token);
        return decodedJWT.getSubject();
    }

    public long getExpirationTimeInMillis(String token) {
        try {
            DecodedJWT decodedJWT = getJWTVerifier().verify(token);
            return decodedJWT.getExpiresAt().getTime();
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException(INVALID_TOKEN_MESSAGE);
        }
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    private List<String> getClaimsFromToken(String token) {
        DecodedJWT decodedJWT = getJWTVerifier().verify(token);
        return decodedJWT.getClaim("roles").asList(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        return JWT.require(HMAC512(secret.getBytes(StandardCharsets.UTF_8)))
                .withIssuer(ISSUER)
                .build();
    }
}
