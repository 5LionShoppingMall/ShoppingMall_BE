package com.ll.lion.controller;

import com.ll.lion.common.dto.LoginRequestDto;
import com.ll.lion.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        String token = authService.authenticate(email, password);

        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }

    @PostMapping("/check")
    public String check(){
        return "JWT SUCCESS";
    }
}
