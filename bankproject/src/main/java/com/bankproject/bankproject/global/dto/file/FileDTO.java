package com.bankproject.bankproject.global.dto.file;

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
public class FileDto {

    private String fileId; // 파일 ID

    private String fileName; // 파일 이름

    private String filePath; // 파일 경로

    private String fileExt; // 파일 확장자

    private Long fileSize; // 파일 크기

    private Boolean useYn; // 사용 여부

    public void delete() {
        this.useYn = false;
    }

    public static FileResponseDto of(FileDto fileDTO) {
        if(fileDTO == null) {
            return FileResponseDto.builder().build();
        }
        return FileResponseDto.builder()
                .fileId(fileDTO.getFileId())
                .fileName(fileDTO.getFileName())
                .build();
    }

    public static List<FileResponseDto> of(List<FileDto> files) {
        if(files == null || files.isEmpty()) {
            return List.of();
        }
        return files.stream()
                .filter(FileDto::getUseYn)
                .map(FileDto::of)
                .toList();
    }

}