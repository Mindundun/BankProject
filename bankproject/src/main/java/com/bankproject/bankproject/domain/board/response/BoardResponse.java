package com.bankproject.bankproject.domain.board.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bankproject.bankproject.domain.board.dto.FileDTO;
import com.bankproject.bankproject.domain.board.entity.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private List<FileDTO> files;
    private LocalDateTime createdAt;
    private Integer readCount;
    private String username;

    public static BoardResponse of(Board board) {
        if (board != null) {
            return BoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .files(board.getFiles())
                    .createdAt(board.getCreatedDate())
                    .readCount(board.getReadCount())
                    .username(board.getUser().getUsername())
                    .build();
        }
        return BoardResponse.builder().build();
    }
}
