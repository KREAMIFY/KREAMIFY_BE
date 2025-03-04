package com.kreamify.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionResponse {

    private String size;
    private int straightBuyPrice;
    private int straightSellPrice;

}
