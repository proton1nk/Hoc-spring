package com.example.first_pj.exception;

import com.example.first_pj.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;




@ControllerAdvice
public class GlobalExceptionHandler {

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
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumkey  = e.getBindingResult().getFieldError().getDefaultMessage();
        ErrorCode errorcode =  ErrorCode.valueOf(enumkey);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorcode.getCode());
        apiResponse.setMessage(errorcode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
