package com.ll.lion.product.dto;

import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ProductRequestDto {
    @NotNull
    private String title;

    private Long price;

    private String imageUrl;

    private String description;

    private ProductStatus status;

    private User seller;
}
