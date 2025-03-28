package com.kreamify.domain.deal.dto;

public record BuyingBidResponse (
    Long id,
    String image,
    String productName,
    String size,
    int suggestPrice,
    String status,
    String createdDate
) {

}
