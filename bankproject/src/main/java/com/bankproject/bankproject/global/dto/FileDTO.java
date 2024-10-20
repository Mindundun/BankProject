package com.bankproject.bankproject.global.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)  // 알 수 없는 필드 무시
public class FileDTO {

    private String fileId; // 파일 ID

    private String fileName; // 파일 이름

    private String filePath; // 파일 경로

    private String fileExt; // 파일 확장자

    private Long fileSize; // 파일 크기

    private Boolean useYn; // 사용 여부

    private LocalDateTime regDate; // 생성일

    private String regId; // 등록자

    private LocalDateTime updatedDate; // 수정일

    private String updatedId; // 수정자

    public void onUpdate(String updatedId) {
        this.updatedDate = LocalDateTime.now();
        this.updatedId = updatedId;
        this.useYn = false;
    }

    public static FileResponseDTO of(FileDTO fileDTO) {
        return FileResponseDTO.builder()
                .fileId(fileDTO.getFileId())
                .fileName(fileDTO.getFileName())
                .build();
    }

    public static List<FileResponseDTO> of(List<FileDTO> files) {
        return files.stream()
                .filter(FileDTO::getUseYn)
                .map(FileDTO::of)
                .toList();
    }

}