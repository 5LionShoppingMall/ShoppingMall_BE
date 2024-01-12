package com.ll.lion.product.entity;

import com.ll.lion.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(mappedBy = "cart")
    private User user;

    public Cart(User user){
        this.user = user;
    }

    public CartItem addItem(Product product, int quantity){
        CartItem item = CartItem.builder()
                .cart(this)
                .product(product)
                .quantity(quantity)
                .build();
        cartItems.add(item);
        return item;
    }

    public void removeItem(CartItem cartItem) {
        cartItems.removeIf(item -> item.getId().equals(cartItem.getId()));
    }
}
