package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.SellingBid;
import com.kreamify.domain.deal.dto.BidDetail;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SellingRepository extends JpaRepository<SellingBid, Long> {

    Optional<SellingBid> findByUserAndProductAndSize(User user, Product product, String size);

    // 상품의 각 사이즈별 판매 입찰 내역 조회
    @Query(
            value =
                    "SELECT s.size, s.suggest_price as price, COUNT(*) as quantity "
                            + "FROM selling_bid s "
                            + "WHERE s.product_id = ?1 "
                            + "GROUP BY s.product_id, s.size, s.suggest_price "
                            + "ORDER BY s.suggest_price",
            nativeQuery = true
    )
    List<BidDetail> findAllByProductGroupBy(Long productId);

    // 특정 사이즈의 판매 입찰 내역을 상세 조회
    @Query(
            value =
                    "SELECT s.size, s.suggest_price as price, COUNT(*) as quantity "
                            + "FROM selling_bid s "
                            + "WHERE s.product_id = ?1 AND s.size = ?2 "
                            + "GROUP BY s.product_id, s.size, s.suggest_price "
                            + "ORDER BY s.suggest_price",
            nativeQuery = true
    )
    List<BidDetail> findAllByProductAndSizeGroupBy(Long productId, String size);
}
