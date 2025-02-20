package com.kreamify.domain.user.controller;


import com.kreamify.domain.user.dto.UserResponse;
import com.kreamify.domain.user.dto.UserSignUpRequest;
import com.kreamify.domain.user.dto.UserUpdateRequest;
import com.kreamify.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kreamify.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 가입", description = "회원가입 정보를 전달받아 회원가입을 합니다")
    @PostMapping

    public ResponseEntity<ApiResponse<Long>> createUser(@Valid @RequestBody UserSignUpRequest  userSignUpRequest) {
        return ResponseEntity.ok(ApiResponse.of(userService.saveUser(userSignUpRequest)));

    }
    @Operation(summary = "회원 정보 수정", description = "회원 ID와 수정 정보 dto 를 이용하여 회원정보을 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(ApiResponse.of(userService.updateUser(id, userUpdateRequest)));

    }
    @Operation(summary = "회원 조회", description = "회원 ID을 이용하여 회원을 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(userService.findUser(id)));
    }
    @Operation(summary = "회원 삭제", description = "회원 ID을 이용하여 회원을 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Long>> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of(userService.deleteUser(id)));
    }


}
