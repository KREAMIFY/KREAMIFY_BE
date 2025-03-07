package com.kreamify.domain.user.repository;

import java.util.Optional;
import com.kreamify.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndIsDeletedFalse(Long id); //쿼리 메서드
    boolean existsUserByEmail(String email); //특정 이메일을 가진 사용자가 DB에 존재하는지 여부를 확인
}
/*
SELECT * FROM user WHERE id = ? is_deleted = FALSE; // is_deleted : 삭제된 사용자는 조회되지 않도록 필터링
SELECT COUNT(*) > 0 FROM user WHERE email = ?;
 */
