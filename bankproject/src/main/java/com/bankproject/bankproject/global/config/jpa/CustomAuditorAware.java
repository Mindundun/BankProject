package com.bankproject.bankproject.global.config.jpa;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bankproject.bankproject.entity.UserEntity;

public class CustomAuditorAware implements AuditorAware<UserEntity> {

    @SuppressWarnings("null")
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        // Spring Security의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 익명 사용자일 경우
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        // 인증된 사용자가 UserEntity 타입인 경우에만 반환
        if (authentication.getPrincipal() instanceof UserEntity) {
            return Optional.of((UserEntity) authentication.getPrincipal());
        } else {
            return Optional.empty();
        }
    }

}
