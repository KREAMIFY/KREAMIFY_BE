package com.kreamify.domain.product.dto;

import com.kreamify.domain.deal.dto.BuyingBidPriceResponse;
import com.kreamify.domain.deal.dto.DealPriceResponse;
import com.kreamify.domain.deal.dto.SellingBidPriceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DetailResponse {

    private int recentDealPrice;
    private List<DealPriceResponse> dealPriceResponses = new ArrayList<>();
    private List<BuyingBidPriceResponse> buyingBidPriceResponses = new ArrayList<>();
    private List<SellingBidPriceResponse> sellingBidPriceResponses = new ArrayList<>();

    private DetailResponse() {

    }

}
