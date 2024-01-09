/*
package com.ll.lion.product.dto;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.entity.Image;
import com.ll.lion.community.entity.Like;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Long price;
    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private UserInfoDto seller;
    @Builder.Default
    private List<Like> likes = new ArrayList<>();
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public static ProductDto from(final ProductRequestDto dto) {
        return ProductDto.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }

    public static ProductDto from(final Product entity) {
        UserInfoDto seller = null;
        if (entity.getSeller() != null) {
            seller = UserInfoDto.from(entity.getSeller());
        }

        return ProductDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .price(entity.getPrice())
                .images(entity.getImages().stream()
                        .map(image -> new ImageDto(image, entity.getId()))
                        .collect(Collectors.toList()))
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .seller(seller)
                .likes(entity.getLikes())
                .cartItems(entity.getCartItems())
                .build();
    }

    public Product toEntity() {
        return Product.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .images(this.images.stream()
                        .map(ImageDto::toEntity)
                        .collect(Collectors.toList()))
                .description(this.description)
                .status(this.status)
                .createdAt(this.createdAt)
                .seller(this.seller != null ? this.seller.toEntity() : null)
                .likes(this.likes)
                .cartItems(this.cartItems)
                .build();
    }
}
*/
