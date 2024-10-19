package com.bankproject.bankproject.domain.account.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountItemUpdateRequest {
    
    private Long id; // 계좌 항목 ID

    private String itemName; // 계좌 항목 이름

    private String itemDesc; // 계좌 항목 설명

    private String ramdomKey; // 계좌 파일 랜덤 키

    private List<String> deleteFileIds; // 삭제할 파일 ID

}
