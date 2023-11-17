package com.ll.lion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsCheckController {
    @GetMapping("/check")
    String corsCheck(){
        return "Hello World";
    }
}
