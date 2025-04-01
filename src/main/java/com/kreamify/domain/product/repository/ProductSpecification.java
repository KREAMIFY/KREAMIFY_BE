package com.kreamify.domain.product.repository;

import com.kreamify.domain.product.domain.Product;
import com.kreamify.domain.product.domain.ProductOption;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

public class ProductSpecification {

    public static final String NAME_DELETED = "isDeleted";
    public static final String NAME_OPTIONS = "options";
    public static final String NAME_BRAND = "brand";
    public static final String NAME_SIZE = "size";
    public static final String NAME_PRICE = "price";
    public static final String NAME_RELEASE_DATE = "releaseDate";
    public static final String NAME_ENGLISH_NAME = "englishName";
    public static final String NAME_KOREAN_NAME = "koreanName";
    public static final String NAME_MODEL_NUMBER = "modelNumber";
    public static final String SORT = "sort";
    public static final String KEYWORD = "keyword";
    public static final String SEPARATOR_COMMA = ",";
    public static final String SEPARATOR_LIKE = "%";

    public static Specification<Product> filterProduct(Map<String, String> filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 기본적으로 삭제되지 않은 상품만 조회
            predicates.add(criteriaBuilder.equal(root.get(NAME_DELETED), Boolean.FALSE));

            // 조인을 한 번만 수행하도록 Optional 사용
            Optional<Join<Product, ProductOption>> sizeJoin = Optional.empty();

            // 필터 조건 추가
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                String key = entry.getKey();
                List<String> values = Arrays.asList(entry.getValue().split(SEPARATOR_COMMA));

                switch (key) {
                    case NAME_BRAND:
                        predicates.add(criteriaBuilder.in(root.get(NAME_BRAND)).value(values));
                        break;

                    case NAME_SIZE:
                        if (sizeJoin.isEmpty()) {
                            sizeJoin = Optional.of(root.join(NAME_OPTIONS, JoinType.INNER));
                        }
                        predicates.add(criteriaBuilder.in(sizeJoin.get().get(NAME_SIZE)).value(values));
                        break;

                    case KEYWORD:
                        String likeValue = SEPARATOR_LIKE + entry.getValue() + SEPARATOR_LIKE;
                        predicates.add(criteriaBuilder.or(
                                criteriaBuilder.like(root.get(NAME_BRAND), likeValue),
                                criteriaBuilder.like(root.get(NAME_ENGLISH_NAME), likeValue),
                                criteriaBuilder.like(root.get(NAME_KOREAN_NAME), likeValue),
                                criteriaBuilder.like(root.get(NAME_MODEL_NUMBER), likeValue)
                        ));
                        break;
                }
            }

            // 정렬 처리 (필터 루프 이후 별도로 설정)
            if (filter.containsKey(SORT) && NAME_RELEASE_DATE.equals(filter.get(SORT))) {
                query.orderBy(criteriaBuilder.desc(root.get(NAME_RELEASE_DATE)));
            }

            // 중복 제거
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
