package com.ll.lion.product.repository;

import com.ll.lion.product.entity.Cart;
import com.ll.lion.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

}
