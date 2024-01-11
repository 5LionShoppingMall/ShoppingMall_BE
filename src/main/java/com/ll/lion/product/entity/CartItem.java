package com.ll.lion.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Setter
    @Column(nullable = false)
    private Integer quantity;

    public CartItem(Cart cart, Product pro, Integer quantity){
        this.cart = cart;
        this.product = pro;
        this.quantity = quantity;
    }
}
