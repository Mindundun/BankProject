package com.bankproject.bankproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown=true)
public class JoinDTO {

    private String id;
    
    private String brithdate;
    private String username;
    private String password;

    private String acount;
    private String acount_pwd;
    private String role;

}
