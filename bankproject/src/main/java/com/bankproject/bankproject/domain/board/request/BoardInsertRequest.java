package com.bankproject.bankproject.domain.board.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardInsertRequest {

    private String category;
    private String subCategory;

    @NotNull(message = "제목은 필수 입력값입니다.")
    @Size(max = 255, message = "제목은 255자 이내로 입력해주세요.")
    private String title;

    @NotNull(message = "내용은 필수 입력값입니다.")
    private String content;

    private String randomKey;

}
