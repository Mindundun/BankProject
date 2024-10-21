package com.bankproject.bankproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                  .requestMatchers("/admin/**").permitAll()//.hasRole("ADMIN")
                  .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                  .anyRequest().authenticated()
          );

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉트
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
