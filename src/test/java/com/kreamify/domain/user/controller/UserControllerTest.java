package com.kreamify.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kreamify.domain.config.TestConfig;
import com.kreamify.domain.user.dto.UserSignUpTestRequest;
import com.kreamify.domain.user.exception.DuplicateUserException;
import jakarta.validation.ConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
@TestConfig
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("회원 가입 테스트")
    @Test
    void createUserTest() throws Exception {
        // Given
        UserSignUpTestRequest userSignUpRequest = new UserSignUpTestRequest(
                "test",
                "test@test.com",
                "01012345678",
                "235",
                "Seoul"
        );

        // When
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        );

        // Then
        result.andExpect(status().isOk()).andDo(print());
    }

    @DisplayName("회원 생성 중복 테스트")
    @Test
    void saveUserDuplicate() throws Exception {
        // Given (첫 번째 회원가입)
        UserSignUpTestRequest userSignUpRequest = new UserSignUpTestRequest(
                "test",
                "test1@test.com",
                "01012345678",
                "235",
                "Seoul"
        );

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        ).andExpect(status().isOk());

        // When (중복 가입 시도)
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        );

        // Then (중복 가입 예외 발생 검증)
        result.andExpect(status().is4xxClientError())
                .andExpect(res -> assertThrows(DuplicateUserException.class, () -> {
                    throw res.getResolvedException();
                })).andDo(print());
    }
    @DisplayName("회원 생성 예외 테스트")
    @Test
    void saveUserExceptionTest() throws Exception {
        // Given(이메일 형식 오류)
        UserSignUpTestRequest userSignUpRequest = new UserSignUpTestRequest(
                "test",
                "test",
                "01012345678",
                "235",
                "Seoul"
        );

        // When
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        );

        // Then
        result.andExpect(status().is5xxServerError()).andExpect(res -> assertThrows(ConstraintViolationException.class,() -> {throw res.getResolvedException();}
            )
        )
                .andDo(print());
    }

}
