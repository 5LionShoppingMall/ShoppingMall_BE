package com.ll.lion.user.dto;

import com.ll.lion.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String profileImageUrl;

    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getProfilePhotoUrl()
        );
    }

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .profilePhotoUrl(this.profileImageUrl)
                .build();
    }
}
