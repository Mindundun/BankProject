package com.bankproject.bankproject.domain.account.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountItemSearchRequest {

    private String search; // 검색어

    @Positive(message = "page는 1보다 커야 합니다.")
    private Integer page; // 페이지

    @Positive(message = "size는 1보다 커야 합니다.")
    private Integer size; // 페이지 크기


    public Integer getPage() {
        return page == null ? 1 : page;
    }

    public Integer getSize() {
        return size == null ? 50 : size;
    }

}
