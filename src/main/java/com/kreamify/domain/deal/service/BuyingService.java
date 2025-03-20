package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.BuyingBid;
import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.repository.BuyingRepository;
import com.kreamify.domain.product.domain.ProductOption;
import com.kreamify.domain.product.service.ProductService;
import com.kreamify.domain.user.domain.User;
import com.kreamify.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BuyingService {

    private final BuyingRepository buyingRepository;
    private final ProductService productService;
    private final UserService userService;

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

}
