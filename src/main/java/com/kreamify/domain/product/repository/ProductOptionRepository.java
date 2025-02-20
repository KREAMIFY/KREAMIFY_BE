package com.kreamify.domain.product.repository;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    // 해당 상품(Product)에 특정 사이즈(size) 옵션이 있는지 확인
    boolean existsByProductAndSize(Product product, String size);
}
