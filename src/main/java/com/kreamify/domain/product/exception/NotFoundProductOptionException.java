package com.kreamify.domain.product.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.NotFoundException;

public class NotFoundProductOptionException extends NotFoundException {

    public NotFoundProductOptionException(ErrorCode errorCode) {
        super(errorCode);
    }

}
