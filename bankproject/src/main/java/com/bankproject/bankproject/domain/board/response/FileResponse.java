package com.bankproject.bankproject.domain.board.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bankproject.bankproject.domain.board.dto.FileDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponse {

    private String fileId;
    private String fileName;

    private static FileResponse of(FileDTO fileDTO) {
        return FileResponse.builder()
                .fileId(fileDTO.getFileId())
                .fileName(fileDTO.getFileName())
                .build();
    }

    public static List<FileResponse> of(List<FileDTO> fileDTOList) {
        if(fileDTOList == null || fileDTOList.isEmpty()) {
            return new ArrayList<>();
        }
        return fileDTOList.stream()
                .map(FileResponse::of)
                .collect(Collectors.toList());
    }

}
