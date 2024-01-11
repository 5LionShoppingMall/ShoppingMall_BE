package com.ll.lion.product.dto;

import com.ll.lion.common.entity.Image;
import com.ll.lion.product.entity.Product;
import com.ll.lion.user.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ProductListDto {
    private Long id;
    private String title;
    private Long price;
    private UserInfoDto seller;
    private String thumbnailImage;
    private LocalDateTime createdAt;

    public static ProductListDto from(Product product) {
        UserInfoDto seller = null;
        if (product.getSeller() != null) {
            seller = UserInfoDto.from(product.getSeller());
        }
        return ProductListDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .seller(seller)
                //.thumbnailImage(product.getImages().isEmpty() ? null : product.getImages().get(0).getUrl()) // 원본 이미지
                .thumbnailImage(product.getImages().isEmpty() ? null : "https://ypj0qkoe2133.edge.naverncp.com/NcgSbfJFoQ/" +
                        product.getImages().get(0).getImageId() + "?type=f&w=400&h=400") // 사이즈 변환 이미지
                .createdAt(product.getCreatedAt())
                .build();
    }

    public Product toEntity() {
        List<Image> images = new ArrayList<>();
        if (this.thumbnailImage != null) {
            images.add(Image.builder()
                    .url(this.thumbnailImage)
                    .build());
        }

        return Product.builder()
                .id(this.id)
                .title(this.title)
                .price(this.price)
                .images(images)
                .createdAt(this.createdAt)
                .seller(this.seller != null ? this.seller.toEntity() : null)
                .build();
    }
}
