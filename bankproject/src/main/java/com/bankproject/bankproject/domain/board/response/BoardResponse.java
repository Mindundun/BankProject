package com.bankproject.bankproject.domain.board.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime createdAt;
    private Integer readCount;
    private String username;
    private List<FileResponse> files;

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .files(FileResponse.of(board.getFileList()))
                .createdAt(board.getCreatedDate())
                .readCount(board.getReadCount())
                .username(board.getUser().getUsername())
                .build();
    }

    public static List<BoardResponse> of(List<Board> boardList) {
        if (boardList == null || boardList.isEmpty()) {
            return new ArrayList<>();
        }
        return boardList.stream()
                .map(BoardResponse::of)
                .toList();
    }
}
