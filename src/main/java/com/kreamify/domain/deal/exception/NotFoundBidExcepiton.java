package com.kreamify.domain.deal.exception;

import com.kreamify.global.error.ErrorCode;
import com.kreamify.global.error.exception.NotFoundException;

public class NotFoundBidExcepiton extends NotFoundException {

    public NotFoundBidExcepiton(ErrorCode errorCode) {
        super(errorCode);
    }

}
