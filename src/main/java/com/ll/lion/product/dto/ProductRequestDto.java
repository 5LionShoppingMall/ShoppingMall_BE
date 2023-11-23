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
    @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
    private String title;

    @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
    private Long price;

    private String imageUrl;

    @NotNull
    private String description;

    private ProductStatus status;

    @NotBlank(message = "판매자는 비워둘 수 없습니다.")
    private User seller;
}
