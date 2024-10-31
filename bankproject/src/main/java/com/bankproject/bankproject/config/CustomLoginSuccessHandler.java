package com.bankproject.bankproject.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.bankproject.bankproject.dto.CustomUserDetails;
import com.bankproject.bankproject.entity.UserEntity;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 로그인이 성공한 경우 유저를 세션에 올리고 메인페이지 혹은 보던 페이지로 리다이렉트 시키고자한다.
    // 이를 위해 AuthenticationSuccessHandler를 상속받아서 로그인 성공시 처리할 내용을 구현한다.
    // 이 클래스는 로그인 성공시 처리할 내용을 구현한 클래스이다.

    // 로그인 성공시 처리할 내용을 구현한다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인 성공 CustomLoginSuccessHandler 실행");
        log.debug("authentication.getName() => {}", authentication.getName());
        log.debug("authentication.getPrincipal() => {}", authentication.getPrincipal());

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity userEntity = userDetails.getUserEntity();

        // 여기에서 유저에 대한 작업을 진행한다.

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("user", userEntity); // 일단 유저 엔티티를 세션에 올린다.
        newSession.setMaxInactiveInterval(60 * 30); // 세션 유지시간을 30분으로 설정한다.

        // 로그인 성공 후 기존 요청했던 페이지로 리다이렉트 시킨다.
        response.sendRedirect("/"); // 기본 페이지로 이동

    }

    

}
