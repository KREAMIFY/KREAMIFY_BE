package com.kreamify.domain.product.dto;

import com.kreamify.domain.product.domain.Product;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProductsResponse {

    private Long id;
    private String brand;
    private String englishName;
    private String koreanName;
    private int straightBuyPrice;
    private int straightSellPrice;
    private LocalDate releaseDate;
    private String image;

    // 기본 생성자(private) → 빈 객체 생성을 방지
    private ProductsResponse() {

    }

    public ProductsResponse(Product product, int sellLowestPrice, int buyHighestPrice) {
        this.id = product.getId();
        this.brand = product.getBrand();
        this.englishName = product.getEnglishName();
        this.koreanName = product.getKoreanName();
        this.straightBuyPrice = sellLowestPrice;
        this.straightSellPrice = buyHighestPrice;
        this.releaseDate = product.getReleaseDate();
        this.image = product.getImage();
    }
}
