package com.kreamify.domain.deal.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)


public class DealHistoryResponse {
    private final Long id;
    private final String image;
    private final String productName;
    private final String size;
    private final String stauts;
    private String buyDate;

    //구매 날짜가 필요하지 않는 경우
    public DealHistoryResponse(
            Long id,
            String image,
            String productName,
            String size,
            String stauts
    )
    {
        this.id = id;
        this.image = image;
        this.productName = productName;
        this.size = size;
        this.stauts = stauts;
    }
   ///구매 날짜가 필요하는 경우
    public DealHistoryResponse(
            Long id,
            String image,
            String productName,
            String size,
            String stauts,
            String buyDate
    )
    {
        this.id = id;
        this.image = image;
        this.productName = productName;
        this.size = size;
        this.stauts = stauts;
        this.buyDate = buyDate;
    }
}

