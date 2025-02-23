package com.kreamify.domain.product.controller;

import com.kreamify.domain.product.dto.ProductRequest;
import com.kreamify.domain.product.dto.ProductResponse;
import com.kreamify.domain.product.dto.ProductsResponse;
import com.kreamify.domain.product.service.ProductService;
import com.kreamify.global.response.ApiResponse;
import com.kreamify.global.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminController {

    private static final String DIRECTORY = "products";

    private final S3Uploader s3Uploader;
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

    @Operation(summary = "상품 등록",
            description = "이미지와 함께 상품을 등록할 수 있습니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerProduct(
            @RequestPart MultipartFile file,
            @Valid @RequestPart ProductRequest request
    ) throws IOException {
        String image = s3Uploader.upload(file, DIRECTORY);
        request.addImage(image);

        return ResponseEntity.ok(ApiResponse.of(productService.registerProduct(request)));
    }

    @Operation(summary = "상품 수정",
            description = "상품의 정보를 수정할 수 있습니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> modifyProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(productService.modifyProduct(id, productRequest)));
    }

}