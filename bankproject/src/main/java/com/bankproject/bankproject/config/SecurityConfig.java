package com.bankproject.bankproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { //암호화 메소드 구현

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
          .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                  .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                  .requestMatchers(HttpMethod.GET, "/api/v1/board/**").permitAll()
                  .requestMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
                  .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                  .anyRequest().authenticated()
          );

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .successHandler(new CustomLoginSuccessHandler())
                        .permitAll()
                )
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=logout")              // 로그아웃 성공시 이동할 페이지
                        .invalidateHttpSession(true)    // 세션 무효화
                        .deleteCookies("JSESSIONID")    // 쿠키 삭제
                        .permitAll()
                );

                
        http
               .csrf((auth) -> auth.disable());




        http
            .logout((auth) -> auth.logoutUrl("/logout")
                    .logoutSuccessUrl("/"));

        return http.build();

    }

        
    
}
