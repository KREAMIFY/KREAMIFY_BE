package com.kreamify.domain.user.dto;

import com.kreamify.domain.user.domain.User;
import lombok.Getter;

@Getter
public class UserSignUpRequest {
    private String nickname;
    private String email;
    private String phone;
    private String size;
    private String address;

    public User toEntity() {
        return User
                .builder()
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .size(size)
                .address(address)
                .build();
    }

}
