package com.kreamify.domain.deal.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SellingHistoryResponse {

    private List<SellingBidResponse> userSellingBidResponses;


    public SellingHistoryResponse(
            List<SellingBidResponse> userSellingBidResponses
    ) {
        this.userSellingBidResponses = userSellingBidResponses;
    }
}
