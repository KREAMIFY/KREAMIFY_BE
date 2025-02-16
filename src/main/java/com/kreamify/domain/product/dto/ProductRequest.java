package com.kreamify.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kreamify.domain.product.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
//상품 등록 요청을 처리하기 위한 DTO
public class ProductRequest {

    @NotBlank
    private String brand;

    @NotBlank
    private String englishName;

    @NotBlank
    private String koreanName;

    @NotBlank
    private String modelNumber;

    @NotBlank
    private String color;

    @NotBlank
    private LocalDate releaseDate;

    @NotNull
    private int releasePrice;

    //@JsonIgnore가 적용된 필드는 JSON 직렬화에서 제외 (JSON 응답에서 image 필드는 제외하여, 따로 처리)
    @JsonIgnore
    private String image;

    //클라이언트에서 전달된 상품 정보를 담음
    private List<String> sizes = new ArrayList<>();

    public ProductRequest() {

    }

    //ProductRequest 객체를 Product 엔티티로 변환
    public Product toEntity() {
        return Product
                .builder()
                .brand(brand)
                .englishName(englishName)
                .koreanName(koreanName)
                .modelNumber(modelNumber)
                .color(color)
                .releaseDate(releaseDate)
                .releasePrice(releasePrice)
                .image(image)
                .build();
    }

    //S3 이미지 업로드 처리
    //클라이언트 응답(JSON)에는 image 값은 포함하지 않고, 서버 내부적으로 image 값을 추가하고, DB에 저장 (AWS S3)
    //image 필드는 JSON 요청에는 포함되지 않지만, S3에 업로드된 후 추가됨
    public void addImage(String image) {
        this.image = image;
    }

}
