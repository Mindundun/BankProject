package com.bankproject.bankproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankproject.bankproject.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);
}
