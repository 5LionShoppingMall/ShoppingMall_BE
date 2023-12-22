package com.ll.lion.common.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ll.lion.common.entity.Image;
import com.ll.lion.product.dto.ProductDto;
import com.ll.lion.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String imageId;
    private String name;
    private String url;
    private Long size;

    @JsonBackReference
    private ProductDto productDto;

    public ImageDto(final Image entity) {
        this.id = entity.getId();
        this.imageId = entity.getImageId();
        this.name = entity.getName();
        this.url = entity.getUrl();
        this.size = entity.getSize();
        this.productDto = new ProductDto(entity.getProduct());
    }

    public static Image toEntity(final ImageDto dto) {
        Product product = null;
        if (dto.getProductDto() != null) {
            product = ProductDto.toEntity(dto.getProductDto());
        }

        return Image.builder()
                .id(dto.getId())
                .imageId(dto.getImageId())
                .name(dto.getName())
                .url(dto.getUrl())
                .size(dto.getSize())
                .product(product)
                .build();
    }
}
