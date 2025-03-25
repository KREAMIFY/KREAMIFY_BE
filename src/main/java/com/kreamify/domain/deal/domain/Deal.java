package com.kreamify.domain.deal.domain;

import com.kreamify.domain.deal.dto.DealResponse;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import com.kreamify.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Entity
@Table(name = "deal")
public class Deal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int price;

    @Column(columnDefinition = "VARCHAR(45) default '검수 중'")
    private String buyingStatus = "검수 중";

    @Column(columnDefinition = "VARCHAR(45) default '검수 중'")
    private String sellingStatus = "검수 중";

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private boolean isFinished;

    protected Deal() {

    }

    @Builder
    private Deal(
            Long id,
            User buyer,
            User seller,
            Product product,
            String size,
            int price
    ) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        this.size = size;
        this.price = price;
    }

    public DealResponse toResponse() {
            return DealResponse.of(
                    id,
                    product.getEnglishName(),
                    size,
                    price,
                    convertDateTime(this.getCreatedDate()));
    }
    private String convertDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

}
