package com.kreamify.domain.user.repository;

import java.util.Optional;
import com.kreamify.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndIsDeletedFalse(Long id);
    boolean existsUserByEmail(String email); //주어진 email를 가진 엔터티 == 이메일로 찾음
}
/*
SELECT * FROM user WHERE id = ? is_deleted = FALSE; // is_deleted : 삭제된 사용자는 조회되지 않도록 필터링
SELECT COUNT(*) > 0 FROM user WHERE email = ?;
 */
