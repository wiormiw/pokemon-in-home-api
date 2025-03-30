package com.wiormiw.pokemon_in_home.exception;

import com.wiormiw.pokemon_in_home.dto.http.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static com.wiormiw.pokemon_in_home.exception.constant.ExceptionConstant.GENERIC_ERROR_RESPONSE_MESSAGE;
import static com.wiormiw.pokemon_in_home.util.DateUtil.dateTimeFormatter;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<HttpResponse<?>> handleException(Exception e, HttpServletRequest r) {
        return createHttpErrorResponse(INTERNAL_SERVER_ERROR, GENERIC_ERROR_RESPONSE_MESSAGE, e);
    }

    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason(reason)
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeStamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), httpStatus);
    }
}
