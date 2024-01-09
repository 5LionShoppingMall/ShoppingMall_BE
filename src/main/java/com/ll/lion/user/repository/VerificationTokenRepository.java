package com.ll.lion.user.repository;

import com.ll.lion.user.entity.User;
import com.ll.lion.user.entity.VerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
