package com.bankproject.bankproject.global.config.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomAuditorAware implements AuditorAware<String> {

    @SuppressWarnings("null")
    @Override
    public Optional<String> getCurrentAuditor() {
        // Spring Security의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 없는 경우 (예: 비인증 요청)
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // 인증된 사용자의 이름 (또는 ID)을 반환
        return Optional.of(authentication.getName()); // 흠 oauth2로 인증을 받는 경우에는 어떻게 처리할까?
    }
}
