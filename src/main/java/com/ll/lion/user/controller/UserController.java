package com.ll.lion.user.controller;


import com.ll.lion.user.dto.CheckEmailExistDto;
import com.ll.lion.user.dto.CheckNicknameExistDto;
import com.ll.lion.user.dto.UserInfoDto;
import com.ll.lion.user.dto.UserRegisterDto;
import com.ll.lion.user.dto.UserUpdateDto;
import com.ll.lion.user.entity.User;
import com.ll.lion.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        User user = userService.register(userRegisterDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        log.info(userDetails.getUsername());
        UserInfoDto userInfoDto = userService.userToUserDTO(userDetails.getUsername());
        return ResponseEntity.ok(userInfoDto);
    }

    @PostMapping("/nickname-exists")
    public ResponseEntity<?> nicknameExist(@RequestBody CheckNicknameExistDto checkNicknameExistDto) {
        boolean checkNicknameExist = userService.isNicknameExist(checkNicknameExistDto.getNickname());
        if (checkNicknameExist){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody UserUpdateDto userUpdateDto) {

        User updatedUser = userService.updateUserInfo(userDetails.getUsername(), userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/email-exists")
    public ResponseEntity<?> emailExist(@Valid @RequestBody CheckEmailExistDto checkEmailExistDto) {
        boolean checkUserExist = userService.checkIfEmailExist(checkEmailExistDto.getEmail());
        if (checkUserExist){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
