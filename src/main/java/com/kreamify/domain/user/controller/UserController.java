package com.kreamify.domain.user.controller;


import com.kreamify.domain.user.dto.UserResponse;
import com.kreamify.domain.user.dto.UserSignUpRequest;
import com.kreamify.domain.user.dto.UserUpdateRequest;
import com.kreamify.domain.user.service.UserService;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")


public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원 가입
    @Operation(summary = "회원 가입", description = "회원가입 정보를 전달받아 회원가입을 합니다")
    @PostMapping

    public ResponseEntity<ApiResponse<Long>> createUser(@Validated @RequestBody UserSignUpRequest  userSignUpRequest) {
        return ResponseEntity.ok(ApiResponse.of(userService.saveUser(userSignUpRequest)));

    }

    //회원 정보 수정
    @Operation(summary = "회원 정보 수정",description = "회원 ID와 수정정보를 이용하여 회원정보를 각각 수정 ")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateUser (
            @PathVariable Long id,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.of(userService.updateUser(id,userUpdateRequest)));
    }

    //회원 정보 조회
    @Operation(summary = "회원조회",description = "회원 ID로 회원 조회")
    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse<UserResponse>> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(userService.findUser(id)));
    }

    //회원 삭제
    @Operation(summary = "회원삭제",description = "회원 ID로 회원 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(userService.deleteUser(id)));
    }


}
