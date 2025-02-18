package com.kreamify.domain.product.service;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.dto.ProductRequest;
import com.kreamify.domain.product.exception.NotFoundProductException;
import com.kreamify.domain.product.repository.ProductOptionRepository;
import com.kreamify.domain.product.repository.ProductRepository;
import com.kreamify.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
//상품 등록 로직을 구현하는 서비스 계층
public class ProductService {

    //리포지토리를 통해 실제 데이터베이스에 상품 데이터를 저장
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

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

    @Transactional
    public Long modifyProduct(Long id, ProductRequest productRequest) {
        Product product = findActiveProduct(id);
        product.changeProductInfo(productRequest);
        for (String size : productRequest.getSizes()) {
            modifyOption(product, size);
        }
        return product.getId();
    }

    @Transactional(readOnly = true)
    public Product findActiveProduct(Long id) {
        return productRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new NotFoundProductException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    private void modifyOption(Product product, String size) {
        boolean isExist = productOptionRepository.existsByProductAndSize(product, size);
        if (!isExist) {
            product.addOption(size);
        }
    }

}