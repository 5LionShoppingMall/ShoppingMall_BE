package com.ll.lion.user.service;

import com.ll.lion.user.dto.RefreshTokenDto;
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
        //Map선언과 비슷함 Redis선언 String과 Dto객체를 key-value쌍으로 연결해서 조회하겠다는 의미
        ValueOperations<String, RefreshTokenDto> valueOperations = redisTemplate.opsForValue();
        //Redis에 key-value쌍을 저장
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
