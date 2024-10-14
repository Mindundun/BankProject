package com.bankproject.bankproject.domain.board.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardUpdateRequest {

    private String title;

    private String content;

    private String randomKey;

    private List<String> deleteFileId;

}
