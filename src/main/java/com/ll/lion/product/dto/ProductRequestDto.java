package com.ll.lion.product.dto;

import com.ll.lion.common.dto.ImageDto;
import com.ll.lion.product.entity.ProductStatus;
import com.ll.lion.user.entity.User;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProductRequestDto {
    @NotNull
    private String title;

    private Long price;

    private String description;

    private ProductStatus status;

    private List<ImageDto> images; // 추가된 필드
}
