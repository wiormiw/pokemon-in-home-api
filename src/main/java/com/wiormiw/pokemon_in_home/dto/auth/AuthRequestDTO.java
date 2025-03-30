package com.wiormiw.pokemon_in_home.dto.auth;

import jakarta.validation.constraints.NotBlank;

import static com.wiormiw.pokemon_in_home.dto.constant.DTOConstant.LOGIN_VALIDATION_MESSAGE;

public record AuthRequestDTO(
        @NotBlank(message = LOGIN_VALIDATION_MESSAGE)
        String username,
        @NotBlank(message = LOGIN_VALIDATION_MESSAGE)
        String password
) {
}
