package com.kreamify.domain.deal.domain;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.user.domain.User;
import com.kreamify.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "buying_bid")
public class BuyingBid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private int suggestPrice;

    @Column(nullable = false)
    private int deadline;

    @Column(columnDefinition = "VARCHAR(45) default '입찰 중'")
    private String status = "입찰 중";

    protected BuyingBid() {

    }

    @Builder
    private BuyingBid(
            Long id,
            User user,
            Product product,
            String size,
            int suggestPrice,
            int deadline
    ) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.size = size;
        this.suggestPrice = suggestPrice;
        this.deadline = deadline;
    }
}
