package com.ll.lion.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/oauth")
@RequiredArgsConstructor
public class OauthController {
    //TODO 배포할 때 사이트 이름으로 로그인 리다이렉트 url 추가해야함 현재는 localhost만 되있음

}
