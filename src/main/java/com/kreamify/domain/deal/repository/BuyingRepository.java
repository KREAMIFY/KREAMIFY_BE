package com.kreamify.domain.deal.repository;

import com.kreamify.domain.deal.domain.BuyingBid;
import com.kreamify.domain.deal.dto.BidDetail;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BuyingRepository extends JpaRepository<BuyingBid, Long> {

    // 상품의 각 사이즈별 입찰가 조회
    @Query(
            value =
                    "SELECT b.size, b.suggest_price as price, COUNT(*) as quantity "
                            + "FROM buying_bid b "
                            + "WHERE b.product_id = ?1 "
                            + "GROUP BY b.product_id, b.size, b.suggest_price "
                            + "ORDER BY b.suggest_price DESC",
            nativeQuery = true
    )
    List<BidDetail> findAllByProductGroupBy(Long productId);

    // 특정 사이즈의 입찰 내역을 상세 조회
    @Query(
            value =
                    "SELECT b.size, b.suggest_price as price, COUNT(*) as quantity "
                            + "FROM buying_bid b "
                            + "WHERE b.product_id = ?1 AND b.size = ?2 "
                            + "GROUP BY b.product_id, b.size, b.suggest_price "
                            + "ORDER BY b.suggest_price DESC",
            nativeQuery = true
    )
    List<BidDetail> findAllByProductAndSizeGroupBy(Long productId, String size);

    List<BuyingBid> findTop2ByProductAndSizeAndStatusOrderBySuggestPriceDescCreatedDateAsc(Product product, String size, String dealStatus);

    Optional<BuyingBid> findByIdAndUserAndStatus(Long bidId, User user, String status);

    Optional<BuyingBid> findFirstByProductAndSizeAndStatusOrderBySuggestPriceDesc(Product product, String size, String status);

    List<BuyingBid> findAllByUserAndStatus(User user, String status);

    List<BuyingBid> findAllByUser(User user);
}
