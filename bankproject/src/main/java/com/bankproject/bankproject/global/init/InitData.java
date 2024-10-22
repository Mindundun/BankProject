package com.bankproject.bankproject.global.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final UserRepository userRepository; // UserRepository를 주입받습니다.
    private final BCryptPasswordEncoder passwordEncoder; // 패스워드 인코더를 주입받습니다.

    @Override
    @Transactional
    public void run(String... args) {
        // 초기 유저 데이터 삽입
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRole("ROLE_ADMIN");

        if(!userRepository.existsByUsername(user.getUsername()) ) {
            // 유저 저장
            userRepository.save(user);

        };
    }
}
