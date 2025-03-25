package com.kreamify.domain.deal.service;

import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.deal.repository.DealRepository;
import org.springframework.stereotype.Service;

@Service
public class DealService {
    private DealRepository dealRepository;
    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }
    public Deal createDeal(Deal deal) {
        return dealRepository.save(deal);
    }
}
