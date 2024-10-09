package com.bankproject.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {
    
    @GetMapping("/admin")
    public String adminP(){

        return "admin";
    }
}
