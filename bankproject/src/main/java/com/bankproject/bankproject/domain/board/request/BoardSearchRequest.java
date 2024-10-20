package com.bankproject.bankproject.domain.board.request;

import lombok.Data;

@Data
public class BoardSearchRequest {

    private String search;

    private Integer page;

    private Integer pageOfSize;

}
