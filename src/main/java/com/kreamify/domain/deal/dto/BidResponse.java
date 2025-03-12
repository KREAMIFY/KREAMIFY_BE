package com.kreamify.domain.deal.dto;

public record BidResponse(
        int price,
        int deadline,
        String expiredDate
) {

}
