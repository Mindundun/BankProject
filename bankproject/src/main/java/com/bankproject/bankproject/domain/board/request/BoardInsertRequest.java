package com.bankproject.bankproject.domain.board.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardInsertRequest {

    private String category;
    private String subCategory;
    private String title;
    private String content;

    private String randomKey;

}
