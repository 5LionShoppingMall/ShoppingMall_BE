package com.ll.lion.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckEmailExistDto {
    @NotBlank
    @Email
    private String email;
}
