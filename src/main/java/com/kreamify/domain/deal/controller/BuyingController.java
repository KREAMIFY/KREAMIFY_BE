package com.kreamify.domain.deal.controller;

import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.service.BuyingService;
import com.kreamify.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buying")
@RequiredArgsConstructor
public class BuyingController {

    private final BuyingService buyingService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> registerBuyingBid(
            @PathVariable Long id,
            @RequestParam String size,
            @RequestBody BidRequest bidRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(buyingService.registerBuyingBid(id, size, bidRequest)));
    }

}
