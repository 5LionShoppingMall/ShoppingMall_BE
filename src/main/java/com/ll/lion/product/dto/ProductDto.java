package com.ll.lion.product.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.entity.Image;
import com.ll.lion.community.entity.Like;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private Long price;

    @JsonManagedReference
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
        this.images = dto.getImages();
    }

    public ProductDto(final Product entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.status = entity.getStatus();
        this.createdAt = entity.getCreatedAt();
        this.seller = entity.getSeller();
        this.likes = entity.getLikes();
        this.cartItems = entity.getCartItems();
        // 여기에서 images를 초기화합니다.
    }

    public static Product toEntity(ProductDto dto) {
        try {
            List<Image> imageEntities = dto.getImages().stream()
                    .map(ImageDto::toEntity)
                    .toList();
            return Product.builder()
                    .id(dto.getId())
                    .title(dto.getTitle())
                    .price(dto.getPrice())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .images(imageEntities)
                    .createdAt(dto.getCreatedAt())
                    .seller(dto.getSeller())
                    .likes(dto.getLikes())
                    .cartItems(dto.getCartItems())
                    .build();
        } catch (Exception e) {
            log.error("Error converting ProductDto to Product entity", e);
            throw e;  // 이 부분은 필요에 따라 조정할 수 있습니다.
        }
    }
}
