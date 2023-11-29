package com.ll.lion.community.dto.post;

import com.ll.lion.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUserDto {
    private Long id;
    private String email;
    private String role;

    public PostUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
