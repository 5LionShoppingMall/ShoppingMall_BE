package com.ll.lion.product.dto;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ProductRequestDto {
    @NotNull
    private String title;

    private Long price;

    private String description;

    private ProductStatus status;
}
