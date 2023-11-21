package com.ll.lion.common.config;

import com.ll.lion.user.dto.RefreshTokenDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, RefreshTokenDto> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, RefreshTokenDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // Value serializer 설정
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshTokenDto.class));

        return redisTemplate;
    }
}
