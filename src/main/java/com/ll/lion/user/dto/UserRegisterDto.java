package com.ll.lion.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String profilePictureUrl;
}
