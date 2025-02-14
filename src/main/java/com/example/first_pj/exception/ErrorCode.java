package com.example.first_pj.exception;

import lombok.Getter;

@Getter
public enum ErrorCode
{
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized Exception"),
    INVALID_KEY(1001,"INVALID_KEY"),
    USER_EXISITED(1002,"User existed"),
    USERNAME_INVALID(1003,"USER INVALID"),
    INVALID_PASSWORD(1004,"INVALID PASSWORD"),
    USER_NOTEXISITED(1005," User not existed"),
    UNAUTHENTICATED(1006,"Unathenticated")

    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;


}
