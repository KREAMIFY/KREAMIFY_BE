package com.kreamify.domain.deal.domain;

import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BidResponse;
import com.kreamify.domain.deal.dto.SellingBidResponse;
import com.kreamify.domain.deal.model.DealStatus;
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
@Table(name = "selling_bid")
public class SellingBid extends BaseEntity {

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
    private String status;

    protected SellingBid() {

    }

    @Builder
    private SellingBid(
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

    public void updateSellingBid(int price, int deadline) {
        this.suggestPrice = price;
        this.deadline = deadline;
    }

    public BidResponse toBidResponse(BidRequest bidRequest) {
        return new BidResponse(
                suggestPrice,
                deadline,
                this
                        .getCreatedDate()
                        .plusDays(bidRequest.deadline())
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        );
    }
    //거래상태
    public void changeStatus(DealStatus dealStatus) {
        this.status = dealStatus.getStatus();
    }
    private String converDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
    public SellingBidResponse toSellingBidResponse() {
        return new SellingBidResponse(
                this.product.getImage(),
                this
                        .getProduct()
                        .getKoreanName(),
                this.size,
                this.suggestPrice,
                this.converDateTime(
                        getCreatedDate().plusDays(this.getDeadline())
                )
        );

    }

}
