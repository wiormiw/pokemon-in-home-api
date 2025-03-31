package com.wiormiw.pokemon_in_home.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static com.wiormiw.pokemon_in_home.exception.constant.ExceptionConstant.AUTHENTICATION_FAILED_MESSAGE;
import static com.wiormiw.pokemon_in_home.exception.constant.ExceptionConstant.GENERIC_ERROR_RESPONSE_MESSAGE;
import static com.wiormiw.pokemon_in_home.security.constant.SecurityConstant.ACCESS_DENIED_MESSAGE;
import static com.wiormiw.pokemon_in_home.util.DateUtil.dateTimeFormatter;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        return createHttpErrorResponse(FORBIDDEN, ACCESS_DENIED_MESSAGE, e);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<HttpResponse<?>> handleJWTVerificationException(JWTVerificationException e) {
        return createHttpErrorResponse(HttpStatus.UNAUTHORIZED, AUTHENTICATION_FAILED_MESSAGE, e);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<HttpResponse<?>> handleException(Exception e) {
        return createHttpErrorResponse(INTERNAL_SERVER_ERROR, GENERIC_ERROR_RESPONSE_MESSAGE, e);
    }

    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        //Logger will make use of exception, so ignore warning for now!
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(reason)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), httpStatus);
    }
}
