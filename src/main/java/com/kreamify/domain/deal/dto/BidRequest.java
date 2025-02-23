package com.kreamify.domain.deal.dto;

import jakarta.validation.constraints.Min;

public record BidRequest(
        @Min(30000) int price,
        @Min(1) int deadline,
        Long userId
) {

}
