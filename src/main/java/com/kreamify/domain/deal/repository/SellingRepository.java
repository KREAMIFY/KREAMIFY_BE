package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.SellingBid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellingRepository extends JpaRepository<SellingBid, Long> {
}
