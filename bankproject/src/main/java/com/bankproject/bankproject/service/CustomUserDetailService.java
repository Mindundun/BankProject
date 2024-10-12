package com.bankproject.bankproject.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bankproject.bankproject.dto.CustomUserDetails;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUsername(username);

        if (userData != null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
