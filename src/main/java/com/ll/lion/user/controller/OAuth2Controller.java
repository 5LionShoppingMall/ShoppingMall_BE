package com.ll.lion.user.controller;

import com.ll.lion.user.dto.LoginResponseDto;
import com.ll.lion.user.service.AuthService;
import com.ll.lion.user.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OAuth2Controller {
    //TODO 배포할 때 사이트 이름으로 로그인 리다이렉트 url 추가해야함 현재는 localhost만 되있음
    private final OAuthService oAuthService;
    private final AuthService authService;

    @GetMapping("/socialLogin/{providerTypeCode}")
    public String socialLogin(String redirectUrl, @PathVariable String providerTypeCode
            , HttpServletResponse response) {
        if (redirectUrl.startsWith("http://localhost:3000")) {
            ResponseCookie redirectUrlAfterSocialLoginCookie = ResponseCookie.from("redirectUrlAfterSocialLogin", redirectUrl)
                    .httpOnly(true)
                    .path("/")
                    .secure(true)
                    .sameSite("None") // SameSite 설정
                    .build();
            response.addHeader("Set-Cookie", redirectUrlAfterSocialLoginCookie.toString());
        }

        return "redirect:/oauth2/authorization/" + providerTypeCode;
    }

    @GetMapping("/socialLogin/Success")
    public String socialLoginSuccess(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        LoginResponseDto loginResp = oAuthService.authenticate(email);

        authService.setTokenInCookie(loginResp.getAccessToken(), loginResp.getRefreshToken(), response);

        String redirectUrl = oAuthService.getUrlAndRemoveCookie(request, response);

        if (oAuthService.isNullExistInProfile(email)) {
           return "redirect:" + redirectUrl + "/mypage/profile/edit";//수정폼으로 redirect
        }

        return "redirect:" + redirectUrl;
    }

}

