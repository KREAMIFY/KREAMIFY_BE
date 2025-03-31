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
import com.kreamify.domain.user.service.UserService;
import com.kreamify.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellingService {

    private static final int ZERO = 0;
    private static final int FIRST_BID = 0;
    private static final int SECOND_BID = 1;
    private static final int ONLY_ONE_BID = 1;
    private static final int ALL_BID = 2;

    private final SellingRepository sellingRepository;
    private final BuyingRepository buyingRepository;
    private final ProductService productService;
    private final UserService userService;
    private final DealService dealService;

    @Transactional
    public DealResponse straightSellProduct(Long productId, String size, BuyRequest buyRequest) {
        Product product = productService.findActiveProduct(productId);
        List<BuyingBid> buyingBids = buyingRepository.findTop2ByProductAndSizeAndStatusOrderBySuggestPriceDescCreatedDateAsc(
                product,
                size,
                DealStatus.BIDDING.getStatus()
        );

        if (buyingBids.isEmpty()) {
            throw new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE);
        }

        BuyingBid firstBuyingBid = buyingBids.get(FIRST_BID);

        Deal deal = dealService.createDeal(
                firstBuyingBid,
                size,
                userService.findActiveUser(buyRequest.userId()),
                productService.findActiveProduct(productId)
        );

        ProductOption productOption = productService.findProductOptionByProductIdAndSize(
                productId, size
        );

        if (buyingBids.size() == ONLY_ONE_BID) {
            productOption.updateBuyBidPrice(ZERO);
        } else if (buyingBids.size() == ALL_BID) {
            BuyingBid secondBuyingBid = buyingBids.get(SECOND_BID);
            productOption.updateBuyBidPrice(secondBuyingBid.getSuggestPrice());
        }

        return deal.toResponse();
    }

    @Transactional
    public BidResponse registerSellingBid(Long id, String size, BidRequest bidRequest) {
        ProductOption productOption = productService.findProductOptionByProductIdAndSize(id, size);

        updateLowestPrice(bidRequest, productOption);

        SellingBid sellingBid = SellingBid
                .builder()
                .product(productOption.getProduct())
                .size(size)
                .user(userService.findActiveUser(bidRequest.userId()))
                .suggestPrice(bidRequest.price())
                .deadline(bidRequest.deadline())
                .build();

        sellingRepository.save(sellingBid);

        return sellingBid.toBidResponse(bidRequest);
    }

    @Transactional
    public BidResponse updateSellingBid(Long id, String size, BidRequest bidRequest) {
        SellingBid sellingBid = findSellingBid(id, size, bidRequest.userId());
        sellingBid.updateSellingBid(bidRequest.price(), bidRequest.deadline());

        ProductOption productOption = productService.findProductOptionByProductIdAndSize(id, size);

        updateLowestPrice(bidRequest, productOption);

        return sellingBid.toBidResponse(bidRequest);
    }

    @Transactional(readOnly = true)
    public SellingBid findSellingBid(Long productId, String size, Long userId) {
        return sellingRepository
                .findByUserAndProductAndSize(
                        userService.findActiveUser(userId),
                        productService.findActiveProduct(productId),
                        size)

                .orElseThrow(() -> new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    public boolean existsSameBid(Long productId, String size, Long userId) {
        return sellingRepository.existsByUserAndProductAndSize(
                userService.findActiveUser(userId),
                productService.findActiveProduct(productId),
                size);
    }

    private void updateLowestPrice(BidRequest bidRequest, ProductOption productOption) {
        if (productOption.getLowestPrice() > bidRequest.price()
                || productOption.getLowestPrice() == ZERO) {
            productOption.updateSellBidPrice(bidRequest.price());
        }
    }

    @Transactional(readOnly = true)
    public SellingHistoryResponse getAllSellingHistory(
            Long id
    ) {
        return new SellingHistoryResponse(
                sellingRepository
                        .findAllByUser(
                                userService.findActiveUser(id)
                        )
                        .stream()
                        .map(SellingBid::toSellingBidResponse)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public SellingHistoryResponse getAllSellingHistoryByStatus(
            Long id,
            String status
    ) {
        return new SellingHistoryResponse(
                sellingRepository
                        .findAllByUserAndStatus(
                                userService.findActiveUser(id),
                                status
                        )
                        .stream()
                        .map(SellingBid::toSellingBidResponse)
                        .toList()
        );
    }

}
