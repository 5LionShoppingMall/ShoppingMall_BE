package com.ll.lion.user.dto;

import java.util.Map;
import lombok.Data;

@Data
public class KakaoUserInfoDto {

    private Long id;

    private String connected_at;

    private KakaoPropertiesDto properties;

    private Map<String, Object> kakao_account;
}
