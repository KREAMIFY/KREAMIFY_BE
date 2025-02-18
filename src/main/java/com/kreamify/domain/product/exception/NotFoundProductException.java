package com.kreamify.domain.product.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.BusinessException;

public class NotFoundProductException extends BusinessException {

    public NotFoundProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
