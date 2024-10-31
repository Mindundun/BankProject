package com.bankproject.bankproject.domain.board.request;

import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardInsertRequest {

    private BoardType category;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String randomKey;

    private Boolean isPin;

    private String pinExpireDate;

}
