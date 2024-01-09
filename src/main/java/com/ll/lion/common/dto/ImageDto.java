package com.ll.lion.common.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.lion.common.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String imageId; // ncp 사용시 keyName이 들어감
    private String name;
    private String url;
    private Long size;

    private Long productId;
    @JsonProperty("isChanged")
    private boolean isChanged;

    public ImageDto(final Image entity, Long productId) {

        this.id = entity.getId();
        this.imageId = entity.getImageId();
        this.name = entity.getName();
        this.url = entity.getUrl();
        this.size = entity.getSize();
        this.productId = productId;
    }

    public static Image toEntity(final ImageDto dto) {

        return Image.builder()
                .id(dto.getId())
                .imageId(dto.getImageId())
                .name(dto.getName())
                .url(dto.getUrl())
                .size(dto.getSize())
                .build();
    }
}
