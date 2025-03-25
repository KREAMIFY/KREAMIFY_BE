package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.SellingBid;
import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.exception.NotFoundBidException;
import com.kreamify.domain.deal.model.DealStatus;
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

    private final SellingRepository sellingRepository;
    private final ProductService productService;
    private final UserService userService;

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
        if (productOption.getLowestPrice() > bidRequest.price() || productOption.getLowestPrice() == ZERO) {
            productOption.updateSellBidPrice(bidRequest.price());
        }
    }
    //
    @Transactional(readOnly = true)
    public List<SellingBid> findSellingBidsOfLowestPrice(
            Product product, String size, DealStatus dealStatus
    ) {
        List<SellingBid> sellingBids = sellingRepository
                //findFist2By -> Limit 2 역할 (가격이 낮고 등록 오래된 상품 2개 - SuggestPrice *가격이 낮은순 &  CreatedDate *같은 가격이면 오래된 순)
                .findFirst2ByProductAndSizeAndStatusOrderBySuggestPriceAscCreatedDateAsc(
                        product,
                        size,
                        dealStatus.getStatus()
                );
        //상품 없는 경우
        if (sellingBids.isEmpty()) {
            throw new NotFoundBidException(ErrorCode.NOT_FOUND_RESOURCE);
        }
        return sellingBids;
    }
}
