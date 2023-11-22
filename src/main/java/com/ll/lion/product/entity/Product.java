package com.ll.lion.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.lion.common.endtity.DateEntity;
import com.ll.lion.community.entity.Like;
import com.ll.lion.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "products")
public class Product extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "product")
    private List<Like> likes;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    @Builder
    public Product(Long id, String name, Long price, String imageUrl, String description, ProductStatus status, User seller, List<Like> likes, List<CartItem> cartItems) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
        this.seller = seller;
        this.likes = likes;
        this.cartItems = cartItems;
    }
}
