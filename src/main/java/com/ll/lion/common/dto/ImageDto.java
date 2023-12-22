package com.ll.lion.common.dto;

import com.ll.lion.common.entity.Image;
import com.ll.lion.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String imageId;
    private String name;
    private String url;
    private Long size;
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
        return Image.builder()
                .id(dto.getId())
                .imageId(dto.getImageId())
                .name(dto.getName())
                .url(dto.getUrl())
                .size(dto.getSize())
                .product(ProductDto.toEntity(dto.getProductDto()))
                .build();
    }
}
