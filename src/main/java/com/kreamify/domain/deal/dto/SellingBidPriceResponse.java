package com.kreamify.domain.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellingBidPriceResponse {

    private String size;
    private int sellingPrice;
    private int quantity;

}
