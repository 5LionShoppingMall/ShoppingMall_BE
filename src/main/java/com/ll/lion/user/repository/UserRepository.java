package com.ll.lion.user.repository;

import com.ll.lion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
