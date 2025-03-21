package com.kreamify.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpTestRequest {

    private String nickname;
    private String email;
    private String phone;
    private String size;
    private String address;
    private Boolean isDeleted = false;

    public UserSignUpTestRequest(String nickname, String email, String phone, String size, String address) {
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.size = size;
        this.address = address;
        //this.isDeleted = false; // 기본값 설정
    }
}
