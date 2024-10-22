package com.bankproject.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(HttpSession session, HttpServletRequest request) {
        // 로그인 페이지로 이동할 때 이전 페이지 정보를 세션에 저장한다.
        Object userObject = session.getAttribute("user");
        if(userObject != null) {
            return "redirect:/";
        }
        return "login";
    }
    
    
}
