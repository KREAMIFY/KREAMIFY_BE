package com.kreamify.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kreamify.domain.config.TestConfig;
import com.kreamify.domain.user.dto.UserSignUpTestRequest;
import com.kreamify.domain.user.dto.UserUpdateTestRequest;
import com.kreamify.domain.user.exception.DuplicateUserException;
import com.kreamify.domain.user.exception.InvalidArgumentException;
import com.kreamify.domain.user.exception.NotFoundUserException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@AutoConfigureMockMvc
@TestConfig
@SpringBootTest
class UserControllerTest {

    private static UserSignUpTestRequest userSignUpRequest;
    private static UserUpdateTestRequest userUpdateTestRequest;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    //사용자 두명 생성

    @BeforeAll
    void setUp() throws Exception{
        //첫번째 사용자(조회 데이터)
        UserSignUpTestRequest userSignUpRequest1 = new UserSignUpTestRequest(
                "test1",
                "test1@email.com",
                "01012232324",
                "235",
                "Seoul"

        );
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest1))
        ).andExpect(status().isOk());
        //두번째 사용자(탈퇴 데이터)
        UserSignUpTestRequest userSignUpRequest2 = new UserSignUpTestRequest(
                "test3",
                "test3@test.com",
                "01033334444",
                "235",
                "Busan"
        );
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest2))
        ).andExpect(status().isOk());
        // 현재 저장된 사용자 목록 확인
        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @DisplayName("회원 가입 테스트")
    @Test
    void createUserTest() throws Exception {
        // given
        String nickname = "test";
        String email = "test@email.com";
        String phone = "01012341234";
        String size = "235";
        String address = "Seoul";

        userSignUpRequest = new UserSignUpTestRequest(
                nickname,
                email,
                phone,
                size,
                address
        );

        // when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest)
                )
        );
        // then
        result
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("사용자 생성 중복테스트")
    @Test
    void saveUserMethodDuplicateExceptionTest() throws Exception {
        String nickname = "test1";
        String email = "test1@email.com";
        String phone = "01012341234";
        String size = "235";
        String address = "Seoul";

        userSignUpRequest = new UserSignUpTestRequest(
                nickname,
                email,
                phone,
                size,
                address
        );

        //when(중복 가입 시도)
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        );

        // then
        result
                .andExpect(
                        status().is4xxClientError()
                )
                .andExpect(res -> assertTrue(res
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(DuplicateUserException.class)
                        )
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("사용자 생성 형식과 다른 경우 예외 처리")
    @Test
    void saveUserMethodValidExceptionTest() throws Exception {
        String nickname = "test";
        String email = "test"; // 이메일 형식에 맞지 않게 기입했을 경우
        String phone = "01012341234";
        String size = "235";
        String address = "Seoul";

        userSignUpRequest = new UserSignUpTestRequest(
                nickname,
                email,
                phone,
                size,
                address
        );

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignUpRequest))
        );

        // then
        result
                .andExpect(//1. 회원가입 실패해서 서버오류
                        status().is5xxServerError()
                ) //2. 이메일 형식 에러
                .andExpect(
                        res -> assertTrue(
                                res
                                        .getResolvedException()
                                        .getClass() //응답에서 발생한 오류 가져옴
                                        .isAssignableFrom(ConstraintViolationException.class)
                                // ConstraintViolationException: 이메일 형식 오류 , 값 누락 등으로 검증 실패할때 발생
                        )
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void updateUserTest() throws Exception {

        Long userId = 1L;
        // given
        String option = "nickname";
        String value = "test4";

        userUpdateTestRequest = new UserUpdateTestRequest(
                option,
                value
        );

        // when
        ResultActions result = mockMvc.perform(patch("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateTestRequest))
        );

        // then
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.data").value(userId)
                )
                .andDo(
                        print()

                );

    }

    @DisplayName("없는 사용자 조회하여 수정하려고 하는경우 예외처리")
    @Test
    void updateUserMethodNotFoundUserExceptionTest() throws Exception {
        Long userId = 3L;
        // given
        String option = "nickname";
        String value = "test4";

        userUpdateTestRequest = new UserUpdateTestRequest(
                option,
                value
        );

        // when
        ResultActions result = mockMvc.perform(patch("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        userUpdateTestRequest)
                )
        );

        // then
        result
                .andExpect(
                        status().is4xxClientError()//클라리언트에러기대
                )
                .andExpect(
                        res -> assertTrue(res
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(NotFoundUserException.class)
                        )
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("잘못된 항목 수정하는 경우 예외 처리")
    @Test
    void updateUserMethodInvalidOptionExceptionTest() throws Exception {
        Long userId = 1L;
        // given
        String option = "Invalid Option"; // 존재하지 않는 옵션 선택
        String value = "updatedNickname";

        userUpdateTestRequest = new UserUpdateTestRequest(
                option,
                value
        );

        // when
        ResultActions result = mockMvc.perform(patch("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        userUpdateTestRequest)
                )
        );

        // then
        result
                .andExpect(
                        status().is4xxClientError()
                )
                .andExpect(
                        res -> assertTrue(res
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(InvalidArgumentException.class)
                        )
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("사용자 조회 테스트")
    @Test
    void findUserTest() throws Exception {


        // 2. 생성된 회원의 ID를 기반으로 조회 테스트(1번 데이터는 조회)
        Long userId = 1L;

        // when
        ResultActions result = mockMvc.perform(get("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.nickname").isString())
                .andExpect(jsonPath("$.data.email").isString())
                .andExpect(jsonPath("$.data.phone").isString())
                .andExpect(jsonPath("$.data.size").isString())
                .andExpect(jsonPath("$.data.address").isString())
                .andDo(print());
    }

    @DisplayName("사용자 조회 실패:예외발생")
    @Test
    void findUserMethodNotFoundExceptionTest() throws Exception {
        // given
        Long userId = 3L; //2명의 사용자가 저장되어있으니깐 3번째 사용자 조회하면 에러

        // when
        ResultActions result = mockMvc.perform(get("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andExpect(
                        status().is4xxClientError()
                )
                .andExpect(
                        res -> assertTrue(res
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(NotFoundUserException.class)
                        )
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("사용자 탈퇴 테스트")
    @Test
    void deleteUserTest() throws Exception {
        // given
        Long userId = 2L;

        // when
        ResultActions result = mockMvc.perform(delete("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.data")
                                .value(userId)
                )
                .andDo(
                        print()
                );
    }

    @DisplayName("존재하지않는 사용자를 삭제하려고 할때 에러 처리")
    @Test
    void deleteUserMethodNotFoundExceptionTest() throws Exception {
        // given
        Long userId = 3L; //2명의 사용자밖에 없기에 3번 사용자를 하면 에러

        // when
        ResultActions result = mockMvc.perform(delete("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result
                .andExpect(
                        status().is4xxClientError()
                )
                .andExpect(
                        res -> assertTrue(res
                                .getResolvedException()
                                .getClass()
                                .isAssignableFrom(NotFoundUserException.class)
                        )
                )
                .andDo(
                        print()
                );
    }
}
