package com.kreamify.domain.deal.controller;

import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.dto.BuyRequest;
import com.kreamify.domain.deal.dto.DealResponse;
import com.kreamify.domain.deal.model.DealStatus;
import com.kreamify.domain.deal.service.SellingService;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/selling")
@RequiredArgsConstructor
public class SellingController {

    private final SellingService sellingService;

    @Operation(summary = "판매 입찰 API",
            description = "상품Id와 신발사이즈,입찰정보(dto)를 통해 입찰을 등록 혹은 갱신합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> registerSellingBid(
            @PathVariable Long id,
            @RequestParam String size,
            @Valid @RequestBody BidRequest bidRequest
    ) {
        if (sellingService.existsSameBid(id, size, bidRequest.userId(), DealStatus.BIDDING.getStatus())) {
            return ResponseEntity.ok(ApiResponse.of(sellingService.updateSellingBid(id, size, bidRequest)));
        }
        return ResponseEntity.ok(ApiResponse.of(sellingService.registerSellingBid(id, size, bidRequest)));
    }

    @Operation(summary = "즉시 판매 API",
            description = "특정 상품과 사이즈를 선택하여, 즉시 판매가로 상품을 즉시 판매합니다.")
    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<DealResponse>> straightSellProduct(
            @PathVariable Long id,
            @RequestParam String size,
            @RequestBody BuyRequest buyRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(sellingService.straightSellProduct(id, size, buyRequest)));
    }

}
