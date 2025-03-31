package com.kreamify.domain.user.dto;


import lombok.Getter;
import java.util.List;

@Getter
public class UserDealHistoryResponse {

    private  List<UserDealResponse> userDealResponses;

    public UserDealHistoryResponse(List<UserDealResponse> userDealResponses) {
        this.userDealResponses = userDealResponses;
    }
}
