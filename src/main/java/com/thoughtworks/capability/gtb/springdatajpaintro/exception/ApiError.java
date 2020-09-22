package com.thoughtworks.capability.gtb.springdatajpaintro.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
public class ApiError {
    private int status;
    private String error;
    private String message;

    public ApiError(HttpStatus httpStatus, String message) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

    public ApiError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public String getTimestamp() {
        return OffsetDateTime.now().toString();
    }

}
