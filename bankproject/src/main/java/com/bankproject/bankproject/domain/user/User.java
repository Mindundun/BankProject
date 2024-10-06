package com.bankproject.bankproject.domain.user;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

@Entity
public class User {
    
    public User(){

    }

    @Id 
    @GeneratedValue
    private Long id;
    // private String password;

    private String username;
    
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id =id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username =username;
    }
    
    @Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}
}