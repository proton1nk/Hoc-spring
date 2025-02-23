package com.example.first_pj.exception;

import com.example.first_pj.dto.ApiResponse;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import java.awt.*;
import java.util.Map;
import java.util.Objects;


@ControllerAdvice
public class GlobalExceptionHandler {

    private  static  final String MIN_ATTRIBUTES = "min";

    @ExceptionHandler(value=Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
    apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
    return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(value=AppException.class)
    ResponseEntity<ApiResponse> handleAppExcception(AppException e) {


        ErrorCode errorCode= e.getErrorcode();
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e)
    {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build()
        );

    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumkey  = e.getBindingResult().getFieldError().getDefaultMessage();
            ErrorCode errorcode = ErrorCode.INVALID_KEY;
            Map attributes= null;
        try {
            errorcode = ErrorCode.valueOf(enumkey);
            var constraintViolation = e.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);

           attributes =  constraintViolation.getConstraintDescriptor().getAttributes();

        }
        catch (IllegalArgumentException e1) {}
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorcode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes)?mapAttributes(errorcode.getMessage(),attributes): errorcode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
    private String mapAttributes (String message, Map<String,Object> attributes)
    {
        String minValue= String.valueOf(attributes.get(MIN_ATTRIBUTES));

        return message.replace("{"+MIN_ATTRIBUTES+"}",minValue);
    }
}
