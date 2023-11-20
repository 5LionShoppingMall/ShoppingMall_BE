package com.ll.lion.common.dto;

import com.ll.lion.entity.SocialProvider;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private String address;
    private String providerId;
    private SocialProvider provider;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
