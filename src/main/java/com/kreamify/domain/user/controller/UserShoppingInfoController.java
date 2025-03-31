package com.kreamify.domain.user.controller;

import com.kreamify.domain.deal.dto.BuyingHistoryResponse;
import com.kreamify.domain.deal.dto.SellingHistoryResponse;
import com.kreamify.domain.deal.service.BuyingService;
import com.kreamify.domain.deal.service.DealService;
import com.kreamify.domain.deal.service.SellingService;
import com.kreamify.domain.user.dto.UserBuyingDealHistoryResponse;
import com.kreamify.domain.user.dto.UserDealHistoryResponse;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<BuyingHistoryResponse>> getBiddingHistory(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(bidStatus -> buyingService.getBiddingHistoryByStatus(userId, bidStatus))
                                .orElse(buyingService.getAllBiddingHistory(userId))
                )
        );
    }

    @Operation(summary = "판매 입찰 내역 조회", description = "사용자의 ID와 상태 값(선택)을 이용해 판매 입찰 내역을 조회합니다.")
    @GetMapping("/selling/bidding")
    public ResponseEntity<ApiResponse<SellingHistoryResponse>> getSellingBidHistories(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(
                                        biddingStatus -> sellingService.getAllSellingHistoryByStatus(
                                                userId,
                                                biddingStatus
                                        )
                                )
                                .orElse(sellingService.getAllSellingHistory(userId))
                )
        );
    }

    @Operation(summary = "구매 거래 진행 중 내역 조회 API", description = "구매 입찰 거래가 체결되어 거래 진행 중인 내역을 조회합니다.")
    @GetMapping("/buying/pending")
    public ResponseEntity<ApiResponse<UserBuyingDealHistoryResponse>> getPendingDealHistory(
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

    @Operation(summary = "판매진행중 내역 조회", description = "회원Id와 상태를 이용해 판매진행중 내역을 조회합니다.")
    @GetMapping("/selling/pending")
    public ResponseEntity<ApiResponse<UserDealHistoryResponse>> getPendingDealHistories(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(
                                        dealStatus -> dealService.getPendingDealHistoryByStatus(
                                                userId,
                                                dealStatus
                                        )
                                )
                                .orElse(dealService.getPendingDealHistory(userId))
                )
        );
    }

    @Operation(summary = "구매 거래 종료 내역 조회 API", description = "거래가 종료된 내역을 조회합니다.")
    @GetMapping("/buying/finished")
    public ResponseEntity<ApiResponse<UserBuyingDealHistoryResponse>> getFinishedDealHistory(
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

    @Operation(summary = "판매 완료 내역 조회", description = "판매가 종료된 내역을 조회합니다.")
    @GetMapping("/selling/finished")
    public ResponseEntity<ApiResponse<UserDealHistoryResponse>> getFinishedDealHistories(
            @PathVariable Long userId,
            @RequestParam Optional<String> status
    ) {
        return ResponseEntity.ok(
                ApiResponse.of(
                        status
                                .map(
                                        dealStatus -> dealService.getFinishedDealHistoryByStatus(
                                                userId,
                                                dealStatus
                                        )
                                )
                                .orElse(dealService.getFinishedDealHistory(userId))
                )
        );
    }
}
