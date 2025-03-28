package com.kreamify.domain.user.controller;

import com.kreamify.domain.deal.dto.BuyingBidResponse;
import com.kreamify.domain.deal.dto.DealHistoryResponse;
import com.kreamify.domain.deal.service.BuyingService;
import com.kreamify.domain.deal.service.DealService;
import com.kreamify.domain.deal.service.SellingService;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}")
public class UserShoppingInfoController {
    private final BuyingService buyingService;
    private final SellingService sellingService;
    private final DealService dealService;

    public UserShoppingInfoController(
            BuyingService buyingService,
            SellingService sellingService,
            DealService dealService
    ){
        this.buyingService = buyingService;
        this.sellingService = sellingService;
        this.dealService = dealService;
    }

    @Operation(summary = "구매 입찰 내역 조회", description = "사용자의 ID와 상태 값(선택)을 이용해 구매 입찰 내역을 조회합니다.")
    @GetMapping("/buying/bidding")
    public ResponseEntity<ApiResponse<List<BuyingBidResponse>>> getBiddingHistory(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(bidStatus -> buyingService.getBiddingHistoryByStatus(userId,bidStatus))
                                .orElse(buyingService.getAllBiddingHistory(userId))
                )
        );

    }
    @Operation(summary = "구매 거래 진행 중 내역 조회 API",  description = "구매 입찰 거래가 체결되어 거래 진행 중인 내역을 조회합니다.")
    @GetMapping("/buying/pending")
    public ResponseEntity<ApiResponse<List<DealHistoryResponse>>> getPendingDealHistory(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(dealStatus -> dealService.getPendingDealByStatus(userId, dealStatus))
                                .orElse(dealService.getAllPendingDealHistory(userId))
                )
        );

    }
    @Operation(summary = "구매 거래 종료 내역 조회 API", description ="거래가 종료된 내역을 조회합니다.")
    @GetMapping("/buying/finished")
    public ResponseEntity<ApiResponse<List<DealHistoryResponse>>> getFinishedDealHistory(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(dealStatus -> dealService.getFinishedDealByStatus(userId, dealStatus))
                                .orElse(dealService.getAllFinishedDealHistory(userId))
                )
        );
    }
}
