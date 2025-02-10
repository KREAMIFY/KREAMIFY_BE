package com.kreamify.global.error;

public record ErrorResponse (

    String message,
    int status

) {

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage(), errorCode.getStatus());
    }

    public static ErrorResponse of(final String message, final int status) {
        return new ErrorResponse(message, status);

    }
}
