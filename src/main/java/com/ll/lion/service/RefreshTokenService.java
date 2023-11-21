package com.ll.lion.service;

import com.ll.lion.common.dto.RefreshTokenDto;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, RefreshTokenDto> redisTemplate;

    public void saveToken(RefreshTokenDto refreshTokenDto) {
        ValueOperations<String, RefreshTokenDto> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshTokenDto.getId(), refreshTokenDto);
        // 만료 시간 설정: 14일
        redisTemplate.expire(refreshTokenDto.getId(), 14, TimeUnit.DAYS);
    }

    public Optional<RefreshTokenDto> getToken(String id) {
        ValueOperations<String, RefreshTokenDto> valueOperations = redisTemplate.opsForValue();
        RefreshTokenDto foundRefreshToken = valueOperations.get(id);
        return Optional.ofNullable(foundRefreshToken);
    }

}
