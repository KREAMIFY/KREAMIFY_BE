package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.BuyingBid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyingRepository extends JpaRepository<BuyingBid, Long> {
}
