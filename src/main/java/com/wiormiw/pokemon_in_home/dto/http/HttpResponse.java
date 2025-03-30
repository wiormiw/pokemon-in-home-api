package com.wiormiw.pokemon_in_home.dto.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.wiormiw.pokemon_in_home.util.DateUtil.dateTimeFormatter;

@Data
@SuperBuilder
@JsonInclude(NON_NULL)
public class HttpResponse<T> {

    private String message;
    private HttpStatus status;
    private int statusCode;
    private String reason;
    protected String timeStamp;

    public HttpResponse(int statusCode, HttpStatus status, String reason, String message) {
        this.timeStamp = LocalDateTime.now().format(dateTimeFormatter());
        this.statusCode = statusCode;
        this.status = status;
        this.reason = reason;
        this.message = message;
    }
}
