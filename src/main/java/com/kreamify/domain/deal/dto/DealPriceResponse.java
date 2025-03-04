package com.kreamify.domain.deal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DealPriceResponse {

    private String size;
    private int dealPrice;
    private String dealDate;
    
}
