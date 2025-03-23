package com.kreamify.domain.deal.controller;

import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.service.BuyingService;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buying")
@RequiredArgsConstructor
public class BuyingController {

    private final BuyingService buyingService;

    @Operation(summary = "구매 입찰 API",
            description = "특정 상품과 해당 상품의 사이즈를 선택하여 원하는 가격을 입력, 입찰 마감기한을 선택하여 구매 입찰합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> registerBuyingBid(
            @PathVariable Long id,
            @RequestParam String size,
            @Valid @RequestBody BidRequest bidRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(buyingService.registerBuyingBid(id, size, bidRequest)));
    }

}
