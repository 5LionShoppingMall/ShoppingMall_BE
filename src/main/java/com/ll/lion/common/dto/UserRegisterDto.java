package com.ll.lion.common.dto;

import com.ll.lion.entity.SocialProvider;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String providerId;
    private SocialProvider provider;
    private String role;
}
