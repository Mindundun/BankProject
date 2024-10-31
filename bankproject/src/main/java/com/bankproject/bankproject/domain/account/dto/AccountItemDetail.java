package com.bankproject.bankproject.domain.account.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountItemDetail {

    private Boolean isFloatingRate; // 변동금리 여부

    private Double minRate;         // 최소 금리

    private Double maxRate;         // 최대 금리

    private List<ContractTermsDTO> contractTerms; // 계약 조건 => 계약기간

    
}
