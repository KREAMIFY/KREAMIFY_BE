package com.kreamify.domain.product.service;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.dto.ProductRequest;
import com.kreamify.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//상품 등록 로직을 구현하는 서비스 계층
public class ProductService {

    //리포지토리를 통해 실제 데이터베이스에 상품 데이터를 저장
    private final ProductRepository productRepository;

    //DI
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Long registerProduct(ProductRequest productRequest) {
        //DTO 객체의 toEntity() 메서드를 호출하여 Product 엔티티 객체로 변환
        Product product = productRequest.toEntity();
        for (String size : productRequest.getSizes()) {
            product.addOption(size);
        }

        return productRepository
                .save(product)
                .getId();
    }

}