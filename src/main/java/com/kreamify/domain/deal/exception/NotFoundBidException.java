package com.kreamify.domain.deal.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.NotFoundException;

public class NotFoundBidException extends NotFoundException {

    public NotFoundBidException(ErrorCode errorCode) {
        super(errorCode);
    }

}
