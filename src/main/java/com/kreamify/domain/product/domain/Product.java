package com.kreamify.domain.product.domain;

import com.kreamify.domain.product.dto.ProductRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String englishName;

    @Column(nullable = false)
    private String koreanName;

    @Column(nullable = false)
    private String modelNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private int releasePrice;

    // TINYINT(0)으로 기본값 설정 (false)
    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private boolean isDeleted;

    // 하나의 상품은 여러 개의 상품 옵션을 가질 수 있다.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductOption> options = new ArrayList<>();

    // JPA에서 Product 객체를 생성할 때 기본 생성자가 필요 (외부에서의 객체 생성을 막음)
    protected Product() {

    }

    @Builder
    private Product(
            Long id,
            String brand,
            String englishName,
            String koreanName,
            String modelNumber,
            String image,
            String color,
            LocalDate releaseDate,
            int releasePrice
    ) {
        this.id = id;
        this.brand = brand;
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.modelNumber = modelNumber;
        this.image = image;
        this.color = color;
        this.releaseDate = releaseDate;
        this.releasePrice = releasePrice;
    }

    // Product 객체에 상품 옵션을 추가
    public void addOption(String option) {
        this.options.add(buildProductOption(option));
    }

    public void changeProductInfo(ProductRequest productRequest) {
        this.brand = productRequest.getBrand();
        this.englishName = productRequest.getEnglishName();
        this.koreanName = productRequest.getKoreanName();
        this.modelNumber = productRequest.getModelNumber();
        this.color = productRequest.getColor();
        this.releaseDate = productRequest.getReleaseDate();
        this.releasePrice = productRequest.getReleasePrice();
    }

    // ProductOption의 빌더 객체를 생성
    private ProductOption buildProductOption(String option) {
        return ProductOption
                .builder()
                .product(this)
                .size(option)
                .build();
    }

}