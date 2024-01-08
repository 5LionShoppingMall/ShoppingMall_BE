package com.ll.lion.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {
    private String password;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String profilePictureUrl;
}
