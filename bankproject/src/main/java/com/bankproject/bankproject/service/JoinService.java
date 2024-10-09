package com.bankproject.bankproject.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankproject.bankproject.dto.JoinDTO;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.repository.UserRepository;
@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 데이터를 DB에 넣기 위함
    public void joinProcess(JoinDTO joinDTO) {


        //db에 이미 동일한 username을 가진 회원이 존재하는지 검사해야함..


        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword())); //비밀번호 암호화
        data.setRole("ROLE_USER");


        userRepository.save(data);
    }
}