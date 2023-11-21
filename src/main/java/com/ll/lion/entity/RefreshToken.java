package com.ll.lion.entity;

import jakarta.persistence.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Token")
public class RefreshToken {

    @Id
    private String id;  // 토큰 자체를 ID로 사용합니다.
    private String userInfo;  // 사용자 정보

}
