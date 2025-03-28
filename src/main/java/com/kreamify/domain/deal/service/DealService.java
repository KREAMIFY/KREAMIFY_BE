package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.BuyingBid;
import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.deal.dto.DealHistoryResponse;
import com.kreamify.domain.deal.model.DealStatus;
import com.kreamify.domain.deal.repository.DealRepository;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import com.kreamify.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DealService {
    private final DealRepository dealRepository;
    private final UserService userService;

    public DealService(DealRepository dealRepository, UserService userService) {
        this.dealRepository = dealRepository;
        this.userService = userService;
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

    @Transactional(readOnly = true)
    public List<DealHistoryResponse> getPendingDealByStatus(Long userId, String status) {
        return dealRepository
                .findAllByBuyerAndBuyingStatusAndIsFinishedFalse(
                        userService.findActiveUser(userId),
                        status
                )
                .stream()
                .map(Deal::toHistoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DealHistoryResponse> getAllPendingDealHistory(Long userId) {
        return dealRepository
                .findAllByBuyerAndIsFinishedFalse(userService.findActiveUser(userId))
                .stream()
                .map(Deal::toHistoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DealHistoryResponse> getFinishedDealByStatus(Long userId, String status) {
        return dealRepository.findAllByBuyerAndBuyingStatusAndIsFinishedTrue(
                        userService.findActiveUser(userId),
                        status
                )
                .stream()
                .map(Deal::toHistoryDateResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DealHistoryResponse> getAllFinishedDealHistory(Long userId) {
        return dealRepository.findAllByBuyerAndIsFinishedTrue(
                        userService.findActiveUser(userId))
                .stream()
                .map(Deal::toHistoryDateResponse)
                .toList();
    }


}
