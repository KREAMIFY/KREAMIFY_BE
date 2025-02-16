package com.kreamify.domain.product.controller;

import com.kreamify.domain.product.dto.ProductRequest;
import com.kreamify.domain.product.service.ProductService;
import com.kreamify.global.response.ApiResponse;
import com.kreamify.global.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/products")
public class AdminController {

    private static final String DIRECTORY = "products";

    private final S3Uploader s3Uploader;
    private final ProductService productService;

    public AdminController(
            S3Uploader s3Uploader,
            ProductService productService
    ) {
        this.s3Uploader = s3Uploader;
        this.productService = productService;
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

}