package com.kreamify.domain.user.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.NotFoundException;

public class NotFoundUserException extends NotFoundException {
    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
