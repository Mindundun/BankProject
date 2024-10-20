package com.bankproject.bankproject.global.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean(name = "auditorProvider") // 빈 이름을 "auditorProvider"로 설정
    AuditorAware<String> auditorProvider() {
        return new CustomAuditorAware(); // 커스텀 AuditorAware 클래스 반환
    }

}
