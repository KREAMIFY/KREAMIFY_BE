package com.kreamify.domain.product.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "product_option")
// 한 상품에 여러 개의 옵션(사이즈, 가격 등)을 부여 할 수 있다.
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // referencedColumnName 생략 가능 (Product 엔티티에서 id가 기본 키로 설정되어 있기 때문에 자동으로 기본 키 참조)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false, columnDefinition = "default 0")
    private int lowestPrice;

    @Column(nullable = false, columnDefinition = "default 0")
    private int highestPrice;

    protected ProductOption() {

    }

    // 객체를 생성할 때 빌더 패턴을 통해 각 필드를 설정할 수 있음.
    // 내부적으로 빌더 클래스가 자동으로 생성됨 (모든 필드를 메서드 체이닝 방식으로 설정할 수 있는 메서드 제공)
    @Builder
    private ProductOption(Long id, Product product, String size) {
        this.id = id;
        this.product = product;
        this.size = size;
    }

    public void updateBuyLowestPrice(int price) {
        this.lowestPrice = price;
    }

    public void updateSellHighestPrice(int price) {
        this.highestPrice = price;
    }

}
