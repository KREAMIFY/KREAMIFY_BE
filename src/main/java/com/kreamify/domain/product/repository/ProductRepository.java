package com.kreamify.domain.product.repository;

import com.kreamify.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 삭제되지 않은 특정 id의 상품을 조회
    Optional<Product> findByIdAndIsDeleted(Long id, boolean deleted);
}
