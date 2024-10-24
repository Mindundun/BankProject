package com.bankproject.bankproject.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractTermsDTO {

    private Integer days; // 계약기간 (일단위)
    private Integer months; // 계약기간 (월단위)
    private Integer years; // 계약기간 (년단위)

}
