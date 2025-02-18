package com.kreamify.domain.user.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.BusinessException;

public  class DuplicateUserException extends BusinessException {
    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
