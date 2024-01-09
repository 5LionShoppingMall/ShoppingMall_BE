package com.ll.lion.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.lion.community.comment.entity.Comment;
import com.ll.lion.community.like.entity.Like;
import com.ll.lion.community.post.entity.Post;
import com.ll.lion.product.entity.Cart;
import com.ll.lion.product.entity.Product;

import com.ll.lion.user.dto.UserUpdateDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "profile_url")
    private String profilePhotoUrl;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "refresh_token_id")
    @JsonIgnore
    private RefreshToken refreshToken;

    private boolean emailVerified;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public void verifyEmail() {
        this.emailVerified = true;
    }


    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void update(UserUpdateDto userUpdateDto) {
        this.password = userUpdateDto.getPassword();
        this.nickname = userUpdateDto.getNickname();
        this.phoneNumber = userUpdateDto.getPhoneNumber();
        this.address = userUpdateDto.getAddress();
        this.profilePhotoUrl = userUpdateDto.getProfilePictureUrl();
    }
}

