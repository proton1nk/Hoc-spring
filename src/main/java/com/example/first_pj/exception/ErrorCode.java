package com.example.first_pj.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode
{
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001,"INVALID_KEY",HttpStatus.BAD_REQUEST),
    USER_EXISITED(1002,"User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"USER INVALID",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004,"INVALID PASSWORD",HttpStatus.BAD_REQUEST),
    USER_NOTEXISITED(1005," User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006,"Unathenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007,"You dont have permission ",HttpStatus.FORBIDDEN),
    INVALID_DOB (1008,"You dont elder enough",HttpStatus.UNAUTHORIZED)
    ;

    ErrorCode(int code, String message,HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode= httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;


}
