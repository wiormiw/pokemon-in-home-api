package com.wiormiw.pokemon_in_home.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import com.wiormiw.pokemon_in_home.security.constant.SecurityConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAuthorizationFilter implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception) throws IOException {

        HttpResponse httpResponse = HttpResponse.builder()
                .statusCode(UNAUTHORIZED.value())
                .status(UNAUTHORIZED)
                .reason(UNAUTHORIZED.getReasonPhrase().toUpperCase())
                .message(SecurityConstant.FORBIDDEN_MESSAGE)
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(UNAUTHORIZED.value());

        new ObjectMapper().writeValue(response.getOutputStream(), httpResponse);
    }
}
