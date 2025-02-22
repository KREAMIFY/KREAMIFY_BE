package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
