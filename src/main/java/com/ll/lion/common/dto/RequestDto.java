package com.ll.lion.common.dto;

import com.ll.lion.product.dto.ProductRequestDto;
import com.ll.lion.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto<T> {
    private String error;
    private List<T> listData;
    private T objData;

    public void requestObjectValidate(final T obj) {
        this.objData = Optional.of(obj)
                .orElseThrow(() -> new IllegalArgumentException("Entity 값이 없습니다."));
    }
}
