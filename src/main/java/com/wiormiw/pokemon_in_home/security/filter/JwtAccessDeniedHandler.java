package com.wiormiw.pokemon_in_home.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import com.wiormiw.pokemon_in_home.security.constant.SecurityConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {

        HttpResponse httpResponse = HttpResponse.builder()
                .statusCode(FORBIDDEN.value())
                .status(FORBIDDEN)
                .reason(FORBIDDEN.getReasonPhrase().toUpperCase())
                .message(SecurityConstant.ACCESS_DENIED_MESSAGE)
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        new ObjectMapper().writeValue(response.getOutputStream(), httpResponse);
    }
}
