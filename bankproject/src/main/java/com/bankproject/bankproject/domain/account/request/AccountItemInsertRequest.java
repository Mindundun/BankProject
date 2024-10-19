package com.bankproject.bankproject.domain.account.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountItemInsertRequest {

    private String itemCode; // 계좌 항목 코드 (조회시 사용)

    private String itemName; // 계좌 항목 이름

    private String itemDesc; // 계좌 항목 설명

    private String ramdomKey; // 계좌 파일 랜덤 키

    private String itemDetail; // 계좌 항목 상세 // 일단 JSON으로 받아서 처리

}
