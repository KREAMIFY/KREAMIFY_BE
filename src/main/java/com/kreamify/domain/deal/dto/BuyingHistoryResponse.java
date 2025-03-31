package com.kreamify.domain.deal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record BuyingHistoryResponse(
        @JsonProperty(value = "bidHistory") List<BuyingBidResponse> buyingBidResponses
) {

}
