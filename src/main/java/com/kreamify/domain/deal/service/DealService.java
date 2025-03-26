package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.BuyingBid;
import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.deal.model.DealStatus;
import com.kreamify.domain.deal.repository.DealRepository;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DealService {

    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Transactional
    public Deal createDeal(BuyingBid buyingBid, String size, User user, Product product) {
        Deal deal = Deal
                .builder()
                .buyer(buyingBid.getUser())
                .seller(user)
                .product(product)
                .size(size)
                .price(buyingBid.getSuggestPrice())
                .build();

        buyingBid.changeStatus(DealStatus.BID_COMPLETED);

        return dealRepository.save(deal);
    }

    public Deal createDeal(Deal deal) {
        return dealRepository.save(deal);
    }

}
