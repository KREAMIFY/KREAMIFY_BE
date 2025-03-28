package com.kreamify.domain.product.controller;

import com.kreamify.global.error.ErrorCode;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    private static final Long NO_ID = 0L;
    private static final Long ID = 2L;
    private static final String SIZE = "240";

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("모든 상품 조회 테스트")
    @Test
    void getProductsTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/products/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("특정 상품 상세 조회 테스트")
    @Test
    void getProductTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/products/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.brand").isString())
                .andExpect(jsonPath("$.data.englishName").isString())
                .andExpect(jsonPath("$.data.koreanName").isString());
    }

    @DisplayName("존재하지 않는 상품 상세 조회 예외 테스트")
    @Test
    void getNoProductTest() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/products/{id}", NO_ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND_RESOURCE.getMessage()));
    }

}