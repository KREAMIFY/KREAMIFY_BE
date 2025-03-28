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
        ProductOption productOption = productService.findProductOptionByProductIdAndSize(id, size);

        if (productOption.getHighestPrice() < bidRequest.price()) {
            productOption.updateSellBidPrice(bidRequest.price());
        }

        User user = userService.findActiveUser(bidRequest.userId());
        BuyingBid buyingBid = buyingRepository.save(
                BuyingBid
                        .builder()
                        .user(user)
                        .product(productOption.getProduct())
                        .size(size)
                        .suggestPrice(bidRequest.price())
                        .deadline(bidRequest.deadline())
                        .build()
        );

        String expiredDate = buyingBid
                .getCreatedDate()
                .plusDays(buyingBid.getDeadline())
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return new BidResponse(buyingBid.getSuggestPrice(), buyingBid.getDeadline(), expiredDate);
    }

    //즉시 구매 요청
    @Transactional
    public DealResponse straightBuyProduct(Long productId, String size, BuyRequest buyRequest) {
        Product product = productService.findActiveProduct(productId); //구매 가능한 상품만 조회

        List<SellingBid> sellingBids = sellingRepository.findFirst2ByProductAndSizeAndStatusOrderBySuggestPriceAscCreatedDateAsc(
                product, size, DealStatus.BIDDING.getStatus()
        ); //입찰중

        if (sellingBids.isEmpty()) {
            throw new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE);
        }

        ProductOption productOption = productService.findProductOptionByProductIdAndSize(productId, size);

        SellingBid topSellingBid = sellingBids.get(FIRST_BID); //최저가 판매 입찰

        topSellingBid.changeStatus(DealStatus.BID_COMPLETED); //최저가 입찰 완료 (=즉시 구매하면 가장 저렴한 입찰이 낙찰)

        if (sellingBids.size() < TWO_BIDS) {
            productOption.updateSellBidPrice(VALUE_ZERO);
        } else if (sellingBids.size() == TWO_BIDS) {
            productOption.updateSellBidPrice(sellingBids.get(SECOND_BID).getSuggestPrice());
        }

        //거래 성공
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
    public List<BuyingBidResponse>  getBiddingHistoryByStatus(Long userId, String status) {
        return buyingRepository
                .findAllByUserAndStatus(
                    userService.findActiveUser(userId),
                    status
        )
                .stream()
                .map(BuyingBid::toResponse)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<BuyingBidResponse> getAllBiddingHistory(Long userId) {
        return buyingRepository
                .findAllByUser(userService.findActiveUser(userId))
                .stream()
                .map(BuyingBid ::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BuyingBid> findBuyingBid(Long productId, String size, DealStatus status) {
        Product product = productService.findActiveProduct(productId);

        List<BuyingBid> buyingBids = buyingRepository
                .findTop2ByProductAndSizeAndStatusOrderBySuggestPriceDescCreatedDateAsc(
                        product,
                        size,
                        status.getStatus()
                );

        if (buyingBids.isEmpty()) {
            throw new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE);
        }

        return buyingBids;
    }

}
