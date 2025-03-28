package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long> {

    Optional<Deal> findFirstByProductOrderByCreatedDateDesc(Product product);

    Optional<Deal> findFirstByProductAndSizeOrderByCreatedDateDesc(Product product, String size);

    List<Deal> findAllByProductOrderByCreatedDateDesc(Product product);

    List<Deal> findAllByProductAndSizeOrderByCreatedDateDesc(Product product, String size);

    List<Deal> findAllByBuyerAndBuyingStatusAndIsFinishedFalse(User user, String status);

    List<Deal> findAllByBuyerAndIsFinishedFalse(User user);

    List<Deal> findAllByBuyerAndBuyingStatusAndIsFinishedTrue(User user, String status);

    List<Deal> findAllByBuyerAndIsFinishedTrue(User user);
}
