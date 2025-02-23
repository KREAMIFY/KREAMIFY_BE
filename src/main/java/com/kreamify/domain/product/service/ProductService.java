package com.kreamify.domain.product.service;

import com.kreamify.domain.deal.domain.Deal;
import com.kreamify.domain.deal.dto.BidDetail;
import com.kreamify.domain.deal.dto.BuyingBidPriceResponse;
import com.kreamify.domain.deal.dto.DealPriceResponse;
import com.kreamify.domain.deal.dto.SellingBidPriceResponse;
import com.kreamify.domain.deal.repository.BuyingRepository;
import com.kreamify.domain.deal.repository.DealRepository;
import com.kreamify.domain.deal.repository.SellingRepository;
import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.domain.ProductOption;
import com.kreamify.domain.product.dto.*;
import com.kreamify.domain.product.exception.NotFoundProductException;
import com.kreamify.domain.product.repository.ProductOptionRepository;
import com.kreamify.domain.product.repository.ProductRepository;
import com.kreamify.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//상품 등록 로직을 구현하는 서비스 계층
public class ProductService {

    private static final int NO_BID = 0;
    private static final int NO_DEAL = 0;
    private static final int ZERO = 0;

    //리포지토리를 통해 실제 데이터베이스에 상품 데이터를 저장
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final DealRepository dealRepository;
    private final BuyingRepository buyingRepository;
    private final SellingRepository sellingRepository;

    @Transactional(readOnly = true)
    public List<ProductsResponse> getProducts() {
        return productRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::toProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = findActiveProduct(id);

        return new ProductResponse(
                product,
                productOptionRepository
                        .findByProduct(product)
                        .stream()
                        .map(productOption -> new OptionResponse(
                                productOption.getSize(),
                                productOption.getLowestPrice(),
                                productOption.getHighestPrice()
                        ))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public DetailResponse getProductDetail(Long id) {
        Product product = findActiveProduct(id);

        Optional<Deal> optDeal = dealRepository.findFirstByProductOrderByCreatedDateDesc(product);
        int recentDealPrice = optDeal.isEmpty() ? NO_DEAL : optDeal
                .get()
                .getPrice();

        List<Deal> dealPrices = dealRepository.findAllByProductOrderByCreatedDateDesc(product);
        List<BidDetail> buyingBids = buyingRepository.findAllByProductGroupBy(product.getId());
        List<BidDetail> sellingBids = sellingRepository.findAllByProductGroupBy(product.getId());

        return toDetailResponse(recentDealPrice, dealPrices, buyingBids, sellingBids);
    }

    @Transactional(readOnly = true)
    public DetailResponse getProductDetailByOption(Long id, String size) {
        Product product = findActiveProduct(id);

        Optional<Deal> optDeal = dealRepository.findFirstByProductAndSizeOrderByCreatedDateDesc(
                product, size);
        int recentDealPrice = optDeal.isEmpty() ? NO_DEAL : optDeal
                .get()
                .getPrice();
        List<Deal> dealPrices = dealRepository.findAllByProductAndSizeOrderByCreatedDateDesc(
                product, size);
        List<BidDetail> buyingBids = buyingRepository.findAllByProductAndSizeGroupBy(
                product.getId(), size);
        List<BidDetail> sellingBids = sellingRepository.findAllByProductAndSizeGroupBy(
                product.getId(), size);

        return toDetailResponse(recentDealPrice, dealPrices, buyingBids, sellingBids);
    }

    @Transactional
    public Long registerProduct(ProductRequest productRequest) {
        //DTO 객체의 toEntity() 메서드를 호출하여 Product 엔티티 객체로 변환
        Product product = productRequest.toEntity();
        for (String size : productRequest.getSizes()) {
            product.addOption(size);
        }

        return productRepository
                .save(product)
                .getId();
    }

    @Transactional
    public Long modifyProduct(Long id, ProductRequest productRequest) {
        Product product = findActiveProduct(id);
        product.changeProductInfo(productRequest);
        for (String size : productRequest.getSizes()) {
            modifyOption(product, size);
        }
        return product.getId();
    }

    @Transactional(readOnly = true)
    public Product findActiveProduct(Long id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundProductException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    private void modifyOption(Product product, String size) {
        boolean isExist = productOptionRepository.existsByProductAndSize(product, size);
        if (!isExist) {
            product.addOption(size);
        }
    }

    private ProductsResponse toProductResponse(Product product) {
        Optional<ProductOption> optLowestPrice = productOptionRepository
                .findFirstByProductAndLowestPriceNotOrderByLowestPrice(product, ZERO);

        Optional<ProductOption> optHighestPrice = productOptionRepository
                .findFirstByProductOrderByHighestPriceDesc(product);

        int lowestPrice = optLowestPrice.isEmpty() ? NO_BID : optLowestPrice
                .get()
                .getLowestPrice();

        int highestPrice = optHighestPrice.isEmpty() ? NO_BID : optHighestPrice
                .get()
                .getHighestPrice();

        return new ProductsResponse(product, lowestPrice, highestPrice);
    }

    private DetailResponse toDetailResponse(
            int recentDealPrice,
            List<Deal> dealPrices,
            List<BidDetail> buyingBids,
            List<BidDetail> sellingBids
    ) {

        List<DealPriceResponse> dealPriceRes = dealPrices
                .stream()
                .map(deal -> new DealPriceResponse(
                        deal.getSize(),
                        deal.getPrice(),
                        deal
                                .getCreatedDate()
                                .format(DateTimeFormatter.ofPattern("yy/MM/dd"))
                ))
                .toList();

        List<BuyingBidPriceResponse> buyingBidRes = buyingBids
                .stream()
                .map(buyingBid -> new BuyingBidPriceResponse(
                        buyingBid.getSize(),
                        buyingBid.getPrice(),
                        buyingBid.getQuantity()
                ))
                .toList();

        List<SellingBidPriceResponse> sellingBidRes = sellingBids
                .stream()
                .map(sellingBid -> new SellingBidPriceResponse(
                        sellingBid.getSize(),
                        sellingBid.getPrice(),
                        sellingBid.getQuantity()
                ))
                .toList();

        return new DetailResponse(recentDealPrice, dealPriceRes, buyingBidRes, sellingBidRes);
    }

}