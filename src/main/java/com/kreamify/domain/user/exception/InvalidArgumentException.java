package com.kreamify.domain.user.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.BusinessException;

public class InvalidArgumentException extends BusinessException {
    public InvalidArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
