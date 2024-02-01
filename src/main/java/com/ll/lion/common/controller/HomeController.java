package com.ll.lion.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/welcome")
    @ResponseBody
    public String home() {
        return "hello";
    }
}
