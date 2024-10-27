package com.bankproject.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(HttpSession session, HttpServletRequest request, Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
        
        // 로그아웃 시 세션 무효화
        if (logout != null) {
            session.invalidate();  // 세션 무효화
            model.addAttribute("logoutMsg", "로그아웃 되었습니다.");
        }

        // 로그인 페이지로 이동할 때 이전 페이지 정보를 세션에 저장한다.
        Object userObject = session.getAttribute("user");
        if(userObject != null) {
            return "redirect:/";
        }

        if(error != null) {
            model.addAttribute("errorMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return "login";
    }
    
    
}
