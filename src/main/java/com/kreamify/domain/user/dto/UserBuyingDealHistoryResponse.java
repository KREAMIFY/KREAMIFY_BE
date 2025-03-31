package com.kreamify.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kreamify.domain.deal.dto.DealHistoryResponse;
import java.util.List;

public record UserBuyingDealHistoryResponse(
        @JsonProperty(value = "dealHistory") List<DealHistoryResponse> dealHistoryResponses
) {

}
