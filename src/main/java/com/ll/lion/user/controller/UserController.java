package com.ll.lion.user.controller;

import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.security.JwtTokenUtil;
import com.ll.lion.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterDto userRegisterDto) {
        User user = userService.register(userRegisterDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }



    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        // refreshToken을 쿠키에서 가져옵니다.
        String refreshToken = jwtTokenUtil.resolveToken(request, "refreshToken");
        // refreshToken에서 이메일을 추출합니다.
        String email = jwtTokenUtil.getEmail(refreshToken);
        User user = userService.getUserbyEmail(email);
        return ResponseEntity.ok(user);
    }
}
