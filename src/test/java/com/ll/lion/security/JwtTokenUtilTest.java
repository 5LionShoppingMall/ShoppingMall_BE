package com.ll.lion.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.ll.lion.user.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void showSecret(){
        System.out.println(jwtTokenUtil.getSecretKey());
    }
}