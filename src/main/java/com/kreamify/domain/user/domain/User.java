package com.kreamify.domain.user.domain;


import com.kreamify.domain.user.dto.UserResponse;
import com.kreamify.domain.user.dto.UserUpdateRequest;
import com.kreamify.domain.user.exception.InvalidArgumentException;
import com.kreamify.global.error.ErrorCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import java.util.Arrays;

@Entity
@Table(name = "users")
@Getter

public class User {
    @Id
    //id -> pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 증가
    private Long id;

    @NotBlank(message = "닉네임을 입력해주세요") //공백, Null 허용 안함
    @Column(nullable = false, unique = true, length = 45)
    private String nickname;

    @NotBlank(message = "이메일 주소를 입력해주세요")
    @Email(message = "이메일 주소를 올바르게 작성해주세요") // 이메일 형식 검증
    @Column(nullable = false,unique = true,length = 50)
    private String email;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "전화번호를 올바르게 작성해주세요") //정규식 검사
    @Column(nullable = false, unique = true, length = 45)
    private String phone;

    private String size; //필수 항목 아님(null 허용)

    @NotBlank(message = "주소를 입력해주세요")
    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, columnDefinition = "TINYINT default false")
    private boolean isDeleted;



    protected User() {} //JPA에서 엔터티 객체를 관리할때 기본 생성자 필요




    @Builder //생성자 (Builder 패턴)
    private User (
            Long id,
            String nickname,
            String email,
            String phone,
            String size,
            String address
    ) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.size = size;
        this.address = address;
    }
    //회원 정보 수정
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        switch (UpdateInfo.getUpdateOption(userUpdateRequest.getOption())) {
            case NICKNAME -> this.nickname = userUpdateRequest.getValue();
            case EMAIL -> this.email = userUpdateRequest.getValue();
            case PHONE -> this.phone = userUpdateRequest.getValue();
            case SIZE -> this.size = userUpdateRequest.getValue();
            case ADDRESS -> this.address = userUpdateRequest.getValue();

        }
    }
    //회원 정보 조회
    public UserResponse toResponse() {
        return UserResponse
                .builder()
                .id(id)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .size(size)
                .address(address)
                .build();
    }

    //사용자의 정보를 수정할때 변경 가능한 항목만
    enum UpdateInfo {

        NICKNAME("nickname"),
        EMAIL("email"),
        PHONE("phone"),
        SIZE("size"),
        ADDRESS("address");

        private String option;

        UpdateInfo(String option) {
            this.option = option;
        }

        public static UpdateInfo getUpdateOption(String input){
            return Arrays
                    .stream(UpdateInfo.values())
                    .filter(u -> u.option.equals(input))
                    .findFirst()
                    .orElseThrow(() -> new InvalidArgumentException(ErrorCode.INVALID_INPUT));
        }

    }
    //회원 삭제
    public void deleteUser() {
        this.isDeleted = true;
    }


}
