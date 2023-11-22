package com.ll.lion.product.dto;

import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDto {
    private String title;
    private Long price;
    private String imageUrl;
    private String description;
    private ProductStatus status;
    private User seller;
}
