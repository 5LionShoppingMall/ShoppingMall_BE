package com.ll.lion.product.dto;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.entity.Image;
import com.ll.lion.community.entity.Like;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Long price;
    private List<ImageDto> images;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private User seller;
    private List<Like> likes;
    private List<CartItem> cartItems;

    public ProductDto(final ProductRequestDto dto) {
        this.title = dto.getTitle();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
    }

    public ProductDto(final Product entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.images = entity.getImages().stream().map(ImageDto::new).toList();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
        this.seller = entity.getSeller();
        this.likes = entity.getLikes();
        this.cartItems = entity.getCartItems();
    }

    public static Product toEntity(final ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .images(dto.getImages().stream().map(ImageDto::toEntity).toList())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .seller(dto.getSeller())
                .likes(dto.getLikes())
                .cartItems(dto.getCartItems())
                .build();
    }
}
