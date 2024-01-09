package com.ll.lion.product.dto;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.common.entity.Image;
import com.ll.lion.community.like.entity.Like;
import com.ll.lion.product.entity.CartItem;
import com.ll.lion.product.entity.Product;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String title;
    private Long price;
    private List<ImageDto> images;
    private String description;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private UserInfoDto seller;
    private List<Like> likes;
    private List<CartItem> cartItems;

    public static ProductDetailDto from(final ProductRequestDto dto) {
        return ProductDetailDto.builder()
                .title(dto.getTitle())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .build();
    }

    public static ProductDetailDto from(Product product) {
        UserInfoDto seller = null;
        if (product.getSeller() != null) {
            seller = UserInfoDto.from(product.getSeller());
        }

        return ProductDetailDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .images(product.getImages().stream()
                        .map(image -> new ImageDto(image, product.getId()))
                        .collect(Collectors.toList()))
                .description(product.getDescription())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .seller(seller)
                .likes(product.getLikes())
                .cartItems(product.getCartItems())
                .build();
    }

    public Product toEntity() {
        List<Image> imageEntities = new ArrayList<>();
        if (this.images != null) {
            imageEntities = this.images.stream()
                    .map(ImageDto::toEntity)
                    .toList();
        }

        return Product.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .images(imageEntities)
                .description(this.description)
                .status(this.status)
                .createdAt(this.createdAt)
                .seller(this.seller != null ? this.seller.toEntity() : null)
                .likes(this.likes)
                .cartItems(this.cartItems)
                .build();
    }
}
