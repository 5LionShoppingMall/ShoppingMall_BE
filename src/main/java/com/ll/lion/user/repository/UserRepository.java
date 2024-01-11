package com.ll.lion.user.repository;

import com.ll.lion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.yaml.snakeyaml.events.Event.ID;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Optional<User> findByProviderId(String providerId);

    Optional<User> findByEmail(String email);


    Optional<User> findByNickname(String nickname);
}
