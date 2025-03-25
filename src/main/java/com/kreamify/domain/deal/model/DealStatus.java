package com.kreamify.domain.deal.model;

import lombok.Getter;

@Getter
public enum DealStatus {

    BIDDING("입찰중"),
    EXPIRED("기한 만료"),
    BID_COMPLETED("입찰 완료"),
    UNDER_INSPECTION("검수 중"),
    SHIP_COMPLETED("배송 완료"),
    ;

    private final String status;

    DealStatus(String status) {
        this.status = status;
    }
}
