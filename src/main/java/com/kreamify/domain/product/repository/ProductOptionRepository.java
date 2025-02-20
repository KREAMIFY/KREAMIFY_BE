package com.kreamify.domain.product.repository;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    // 해당 상품(Product)에 특정 사이즈(size) 옵션이 있는지 확인
    boolean existsByProductAndSize(Product product, String size);

    Optional<ProductOption> findFirstByProductAndLowestPriceNotOrderByLowestPrice(
            Product product,
            int zero
    );

    Optional<ProductOption> findFirstByProductOrderByHighestPriceDesc(Product product);

    Optional<ProductOption> findByProductAndSize(Product product, String size);
}
