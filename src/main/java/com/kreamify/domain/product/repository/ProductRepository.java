package com.kreamify.domain.product.repository;

import com.kreamify.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // 삭제되지 않은 특정 id의 상품을 조회
    List<Product> findAllByIsDeletedFalse();
    Optional<Product> findByIdAndIsDeletedFalse(Long id);
}
