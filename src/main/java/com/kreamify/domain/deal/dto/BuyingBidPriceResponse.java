package com.kreamify.domain.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BuyingBidPriceResponse {

    private String size;
    private int buyingPrice;
    private int quantity;

}
