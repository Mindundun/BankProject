package com.bankproject.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String mainP(){
        return "index";
        // 민경아 인덱스로 바꾸래서 바꿨다 기존:main
    }
}
