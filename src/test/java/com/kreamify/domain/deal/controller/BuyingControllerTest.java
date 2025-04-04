package com.kreamify.domain.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kreamify.domain.config.TestConfig;
import com.kreamify.domain.deal.dto.BidRequest;
import com.kreamify.domain.deal.dto.BuyRequest;
import com.kreamify.domain.deal.exception.NotFoundBidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
@TestConfig
@Transactional
class BuyingControllerTest {
    private static final long USER_ID = 2L;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("구매 입찰 등록 테스트")
    @Test
    void registerBuyingBidTest() throws Exception {
        // given
        int price = 315000;
        int deadline = 1;
        BidRequest bidRequest = new BidRequest(
                price,
                deadline,
                USER_ID
        );
        long product_id = 1L;
        String size = "250";

        // when
        ResultActions result = mockMvc
                .perform(
                        put("/buying/{id}", product_id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("size", size)
                                .content(objectMapper.writeValueAsString(bidRequest))
                );

        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.data.deadline")
                                .value(deadline)
                )
                .andExpect(
                        jsonPath("$.data.expiredDate")
                                .isString()
                )
                .andExpect(
                        jsonPath("$.data.price")
                                .isNumber()
                );
    }

    @DisplayName("즉시 구매 테스트")
    @Test
    void straightBuyProductTest() throws Exception {
        // given
        Long productId = 2L;
        String size = "250";
        BuyRequest buyRequest = new BuyRequest(USER_ID);

        // when
        ResultActions result = mockMvc
                .perform(
                        post("/buying/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("size", size)
                                .content(objectMapper.writeValueAsString(buyRequest)));

        // then
//        int expectedPrice = 109000;
//        String expectedProductName = "New Balance 990";
//        String expectedSize = "250";
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.data.dealId")
                                .isNumber()
                )
                .andExpect(
                        jsonPath("$.data.price")
                                .isNumber()
                )
                .andExpect(
                        jsonPath("$.data.productName")
                                .isString()
                )
                .andExpect(
                        jsonPath("$.data.size")
                                .isString()
                )
                .andDo(print());
    }

    @DisplayName("입찰 취소 테스트")
    @Test
    void cancelBuyingBidTest() throws Exception {
        // given

        Long buyingBidId = 1L;

        // when
        ResultActions result = mockMvc
                .perform(
                        delete("/users/{userId}/buying/{buyingBidId}", USER_ID, buyingBidId)
                                .contentType(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(
                        status().isOk()
                );

        mockMvc
                .perform(
                        delete("/users/{userId}/buying/{buyingBidId}",USER_ID, buyingBidId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        res ->
                                assertThat(
                                        Objects
                                                .requireNonNull(res.getResolvedException())
                                                .getClass()
                                                .isAssignableFrom(NotFoundBidException.class)
                                ).isTrue()
                );
    }

    @DisplayName("구매 입찰 변경 테스트")
    @Test
    void updateBuyingBidTest() throws Exception {
        // given
        int price = 310000;
        int deadline = 1;

        BidRequest bidRequest = new BidRequest(
                price,
                deadline,
                USER_ID
        );
        Long productId = 1L;
        String size = "250";

        // when
        ResultActions result = mockMvc
                .perform(
                        put("/buying/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("size", size)
                                .content(objectMapper.writeValueAsString(bidRequest))
                );

        // then
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.data.deadline")
                                .isNumber()
                )
                .andExpect(
                        jsonPath("$.data.expiredDate")
                                .isString()
                )
                .andExpect(
                        jsonPath("$.data.price")
                                .isNumber()
                );
    }
}
