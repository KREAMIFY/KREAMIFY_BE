package com.kreamify.domain.product.controller;

import com.kreamify.domain.product.dto.ProductResponse;
import com.kreamify.domain.product.dto.ProductsResponse;
import com.kreamify.domain.product.service.ProductService;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 리스트 조회",
            description = "특정 조건에 맞는 상품들을 조회할 수 있습니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductsResponse>>> getProducts() {
        return ResponseEntity.ok(ApiResponse.of(productService.getProducts()));
    }

    @Operation(summary = "특정 상품 조회",
            description = "특정 상품의 기본 정보를 조회할 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(productService.getProduct(id)));
    }

}