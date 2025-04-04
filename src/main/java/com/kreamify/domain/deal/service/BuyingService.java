package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.BuyingBid;
import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.deal.domain.SellingBid;
import com.kreamify.domain.deal.dto.*;
import com.kreamify.domain.deal.exception.NotFoundBidException;
import com.kreamify.domain.deal.model.DealStatus;
import com.kreamify.domain.deal.repository.BuyingRepository;
import com.kreamify.domain.deal.repository.SellingRepository;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.domain.ProductOption;
import com.kreamify.domain.product.service.ProductService;
import com.kreamify.domain.user.domain.User;
import com.kreamify.domain.user.service.UserService;
import com.kreamify.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyingService {

    private static final int FIRST_BID = 0;
    private static final int SECOND_BID = 1;
    private static final int TWO_BIDS = 2;
    private static final int VALUE_ZERO = 0;

    private final BuyingRepository buyingRepository;
    private final SellingRepository sellingRepository;
    private final ProductService productService;
    private final UserService userService;
    private final DealService dealService;

    @Transactional
    public BidResponse registerBuyingBid(Long id, String size, BidRequest bidRequest) {
        User user = userService.findActiveUser(bidRequest.userId());
        ProductOption productOption = productService.findProductOptionByProductIdAndSize(id, size);

        Optional<BuyingBid> existingBid = buyingRepository
                .findByProductAndSizeAndStatusAndUser(
                        productOption.getProduct(),
                        size,
                        DealStatus.BIDDING.getStatus(),
                        user
                );
        if (existingBid.isPresent()) {
            return updateExistingBuyingBid(existingBid.get(), size, bidRequest, productOption);
        }

        updateHighestPrice(bidRequest.price(), productOption);
        BuyingBid newBuyingBid = buyingRepository.save(
                BuyingBid
                        .builder()
                        .user(user)
                        .product(productOption.getProduct())
                        .size(size)
                        .suggestPrice(bidRequest.price())
                        .deadline(bidRequest.deadline())
                        .build()
        );
        return newBuyingBid.toBidResponse();
    }
    //즉시 구매 요청
    @Transactional
    public DealResponse straightBuyProduct(Long productId, String size, BuyRequest buyRequest) {
        Product product = productService.findActiveProduct(productId);
        List<SellingBid> sellingBids = sellingRepository
                .findFirst2ByProductAndSizeAndStatusOrderBySuggestPriceAscCreatedDateAsc(
                        product,
                        size,
                        DealStatus.BIDDING.getStatus()
                );

        if (sellingBids.isEmpty()) {
            throw new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE);
        }

        ProductOption productOption = productService
                .findProductOptionByProductIdAndSize(
                        productId,
                        size
                );
        SellingBid topSellingBid = sellingBids.get(FIRST_BID);
        topSellingBid.changeStatus(DealStatus.BID_COMPLETED);
        if (sellingBids.size() < TWO_BIDS) {
            productOption.updateSellBidPrice(VALUE_ZERO);
        } else if (sellingBids.size() == TWO_BIDS) {
            productOption.updateSellBidPrice(
                    sellingBids
                            .get(SECOND_BID)
                            .getSuggestPrice()
            );
        }

        return dealService
                .createDeal(
                        Deal
                                .builder()
                                .buyer(userService.findActiveUser(buyRequest.userId()))
                                .seller(topSellingBid.getUser())
                                .product(topSellingBid.getProduct())
                                .size(size)
                                .price(topSellingBid.getSuggestPrice())
                                .build()
                )
                .toResponse();
    }
    @Transactional(readOnly = true)
    public BuyingHistoryResponse getBiddingHistoryByStatus(Long userId, String status) {
        return new BuyingHistoryResponse(
                buyingRepository
                        .findAllByUserAndStatus(
                                userService.findActiveUser(userId),
                                status
                        )
                        .stream()
                        .map(BuyingBid::toResponse)
                        .toList());
    }

    @Transactional(readOnly = true)
    public BuyingHistoryResponse getAllBiddingHistory(Long userId) {
        return new BuyingHistoryResponse(
                buyingRepository
                        .findAllByUser(userService.findActiveUser(userId))
                        .stream()
                        .map(BuyingBid::toResponse)
                        .toList()
        );
    }



    @Transactional
    public void cancelBuyingBid(Long userId, Long bidId) {
        BuyingBid buyingBid = buyingRepository
                .findByIdAndUserAndStatus(
                        bidId,
                        userService.findActiveUser(userId),
                        DealStatus.BIDDING.getStatus()
                )
                .orElseThrow(() -> new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE));
        buyingBid.cancel();

        ProductOption productOption = productService.findProductOptionByProductIdAndSize(
                buyingBid.getProduct().getId(), buyingBid.getSize()
        );

        // 입찰 취소 후 남아있는 입찰 중 최고 가격의 입찰을 찾아 업데이트
        Optional<BuyingBid> topPriceBid = buyingRepository
                .findFirstByProductAndSizeAndStatusOrderBySuggestPriceDesc(
                        buyingBid.getProduct(),
                        buyingBid.getSize(),
                        DealStatus.BIDDING.getStatus()
                );

        // 최고 입찰(topPriceBid)이 존재하면 해당 가격으로 업데이트, 없는 경우 0으로 설정
        productOption.updateBuyBidPrice(
                topPriceBid.map(BuyingBid::getSuggestPrice).orElse(VALUE_ZERO)
        );
    }

    private BidResponse updateExistingBuyingBid(
            BuyingBid buyingBid,
            String size,
            BidRequest bidRequest,
            ProductOption productOption
    ) {
        buyingBid.update(bidRequest.price(), bidRequest.deadline());
        buyingRepository
                .findFirstByProductAndSizeAndStatusOrderBySuggestPriceDesc(
                        buyingBid.getProduct(),
                        size,
                        DealStatus.BIDDING.getStatus()
                )
                .ifPresent(
                        topPriceBid -> productOption.updateBuyBidPrice(
                                topPriceBid.getSuggestPrice())
                );
        return buyingBid.toBidResponse();
    }

    private void updateHighestPrice(int price, ProductOption productOption) {
        if (productOption.getHighestPrice() < price) {
            productOption.updateBuyBidPrice(price);
        }
    }

}
