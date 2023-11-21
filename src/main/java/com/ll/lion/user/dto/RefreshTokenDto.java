package com.ll.lion.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenDto {

    private String id;
    private String email;

}
