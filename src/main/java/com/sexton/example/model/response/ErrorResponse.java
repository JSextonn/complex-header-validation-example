package com.sexton.example.model.response;

import lombok.Builder;
import lombok.Data;
import lombok.val;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String status;
    private String message;

    public static ErrorResponse badRequest(Set<? extends ConstraintViolation<?>> violations) {
        val errorDetails = buildErrorDetails(violations);
        return create(HttpStatus.BAD_REQUEST,
                String.format("Given inputs were incorrect. Consult the below details for more information: %s", errorDetails));
    }

    public static ErrorResponse create(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .code(status.value())
                .status(status.getReasonPhrase())
                .message(message)
                .build();
    }

    private static String buildErrorDetails(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
