package com.ll.lion.user.controller;

import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterDto userRegisterDto) {
        User user = userService.register(userRegisterDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
