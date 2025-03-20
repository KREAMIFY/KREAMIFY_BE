package com.kreamify.domain.deal.controller;

import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.service.SellingService;
import com.kreamify.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/selling")
@RequiredArgsConstructor
public class SellingController {

    private final SellingService sellingService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> registerSellingBid(
            @PathVariable Long id,
            @RequestParam String size,
            @Valid @RequestBody BidRequest bidRequest
    ) {
        if (sellingService.existsSameBid(id, size, bidRequest.userId())) {
            return ResponseEntity.ok(ApiResponse.of(sellingService.updateSellingBid(id, size, bidRequest)));
        }
        return ResponseEntity.ok(ApiResponse.of(sellingService.registerSellingBid(id, size, bidRequest)));
    }
}
