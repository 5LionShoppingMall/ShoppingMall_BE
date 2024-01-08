package com.ll.lion.user.controller;

import com.ll.lion.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/oauth")
@RequiredArgsConstructor
public class Oauth2Controller {
    //TODO 배포할 때 사이트 이름으로 로그인 리다이렉트 url 추가해야함 현재는 localhost만 되있음

    private final UserService userService;

    @GetMapping("/socialLogin/{providerTypeCode}")
    public String socialLogin(String redirectUrl, @PathVariable String providerTypeCode
    , HttpServletResponse response) {
        if (redirectUrl.startsWith("http://localhost:3000")) {
            Cookie cookie = new Cookie("redirectUrlAfterSocialLogin", redirectUrl);
            cookie.setMaxAge(60*10);
            response.addCookie(cookie);
        } //프론트 응답에 backend url을 쿠키로 심어서 code를 backend로 가져오도록

        return "redirect:/oauth2/authorization/" + providerTypeCode;
    }

}

