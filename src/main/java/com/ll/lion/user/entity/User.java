package com.ll.lion.user.entity;


import com.ll.lion.community.entity.Comment;
import com.ll.lion.community.entity.Like;
import com.ll.lion.community.entity.Post;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_url")
    private String profilePhotoUrl;

    @Column(name = "social_provider")
    private String socialProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String role;

    private boolean emailVerified;

    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Like> likes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id", referencedColumnName = "id")
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
