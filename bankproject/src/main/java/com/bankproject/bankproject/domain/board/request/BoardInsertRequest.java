package com.bankproject.bankproject.domain.board.request;

import java.util.List;

import lombok.Data;

@Data
public class BoardInsertRequest {

    private String category;
    private String subCategory;
    private String title;
    private String content;

    private List<String> randomKey;

}
