<<<<<<< HEAD
//package com.ll.lion.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class JwtTokenProviderTest {
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
////    @Test
////    public void showSecret(){
////        System.out.println(jwtTokenProvider.getSecretKey());
////    }
//}
=======
package com.ll.lion.security;

import com.ll.lion.user.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

//    @Test
//    public void showSecret(){
//        System.out.println(jwtTokenProvider.getSecretKey());
//    }
}
>>>>>>> heeyeong
