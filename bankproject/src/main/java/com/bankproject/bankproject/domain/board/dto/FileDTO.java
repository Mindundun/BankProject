package com.bankproject.bankproject.domain.board.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {

    private String fileId; // 파일 ID

    private String fileName; // 파일 이름
    private String fileUrl; // 파일 URL

    public FileDTO(String fileName, String fileUrl) {
        this.fileId = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

}
