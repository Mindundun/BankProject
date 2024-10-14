package com.bankproject.bankproject.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_user") // 변경된 테이블 이름
public class User {
    
    public User() {
    }

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 생성 전략 추가
    private Long id;

    private String username;
    
    public Long getId() { // Long 타입으로 반환
        return id;
    }

    public void setId(Long id) { // Long 타입으로 매개변수
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + "]";
    }
}
