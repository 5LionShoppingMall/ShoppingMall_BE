
package com.ll.lion.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocialLoginDto {
    @NotEmpty
    private String providerTypeCode;
    @NotEmpty
    private String email;
    @NotEmpty
    private String profileImageUrl;
    @NotEmpty
    private String ProviderId;
    @NotEmpty
    private String nickname;
}