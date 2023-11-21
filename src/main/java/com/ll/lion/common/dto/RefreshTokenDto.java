package com.ll.lion.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenDto {

    private String id;
    private String email;

}
