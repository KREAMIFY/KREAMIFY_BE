package com.kreamify.domain.user.service;

import com.kreamify.domain.user.domain.User;
import com.kreamify.domain.user.dto.UserSignUpRequest;
import com.kreamify.domain.user.dto.UserUpdateRequest;
import com.kreamify.domain.user.exception.DuplicateUserException;
import com.kreamify.domain.user.exception.NotFoundUserException;
import com.kreamify.domain.user.repository.UserRepository;
import com.kreamify.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service

public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //회원가입
    @Transactional
    public Long saveUser(UserSignUpRequest userSignUpRequest) {
        validateDuplicateUser(userSignUpRequest);
        return userRepository
                .save(userSignUpRequest.toEntity())
                .getId();

    }
    //회원 정보 수정
    @Transactional
    public Long updateUser(Long id,UserUpdateRequest userUpdateRequest) {
        User user = findActiveUser(id);
        user.updateUser(userUpdateRequest);

        return user.getId();

    }

    public User findActiveUser(Long id){
        return userRepository
                .findByIdAndIsDeleted(id,false)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE));
    }

    //이미 존재하는 User 경우
    private void validateDuplicateUser(UserSignUpRequest userSignUpRequest) {
        if (userRepository.existsUserByEmail(
                userSignUpRequest.getEmail()
        )) {
            throw new DuplicateUserException(ErrorCode.CONFLICT_ERROR);
        }
    }






}
