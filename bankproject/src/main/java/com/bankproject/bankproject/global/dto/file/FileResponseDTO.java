package com.bankproject.bankproject.global.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileResponseDTO {

    private String fileId; // 파일 ID

    private String fileName; // 파일 이름

}
