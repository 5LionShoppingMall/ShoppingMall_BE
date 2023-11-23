package com.ll.lion.product.dto;

import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ProductRequestDto {
    private String title;
    private Long price;
    private String imageUrl;
    private String description;
    private ProductStatus status;
    private User seller;

    public ProductDto requestObjectValidate(final ProductRequestDto dto) {
        return Optional.ofNullable(dto)
                .map(ProductDto::new)
                .orElseThrow(() -> new IllegalArgumentException("Entity가 비었습니다."));
    }
}
