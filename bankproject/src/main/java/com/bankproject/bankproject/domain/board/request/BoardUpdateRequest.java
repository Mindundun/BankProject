package com.bankproject.bankproject.domain.board.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String randomKey;

    private List<String> deleteFileIds;

    private Boolean isPin;

    private String pinExpireDate;

}
