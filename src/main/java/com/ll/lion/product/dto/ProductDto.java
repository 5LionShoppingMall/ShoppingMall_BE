package com.ll.lion.product.dto;

import com.ll.lion.community.entity.Like;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String title;
    private Long price;
    private String imageUrl;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private User seller;
    private List<Like> likes;
    private List<CartItem> cartItems;

    @Builder
    public ProductDto(Long id, String title, Long price, String imageUrl, String description, ProductStatus status, LocalDateTime createdAt, User seller, List<Like> likes, List<CartItem> cartItems) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.seller = seller;
        this.likes = likes;
        this.cartItems = cartItems;
    }

    public ProductDto(final ProductRequestDto dto) {
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.imageUrl = dto.getImageUrl();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
        this.seller = dto.getSeller();
    }

    public static ProductDto toDto(final Product entity) {
        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .seller(entity.getSeller())
                .likes(entity.getLikes())
                .cartItems(entity.getCartItems())
                .build();
    }

    public static Product toEntity(final ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .seller(dto.getSeller())
                .likes(dto.getLikes())
                .cartItems(dto.getCartItems())
                .build();
    }

    public static Product toEntity(final ProductRequestDto dto) {
        return Product.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .seller(dto.getSeller())
                .build();
    }
}
