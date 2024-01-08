package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
=======
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
>>>>>>> heeyeong
}
