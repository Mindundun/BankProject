package com.bankproject.bankproject.domain.board.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.global.dto.file.FileDto;
import com.bankproject.bankproject.global.dto.file.FileResponseDto;

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
    private List<FileResponseDto> files;
    private Integer readCount;
    private String regUser;
    private LocalDateTime regDate;

    public static BoardResponse of(Board board) {
        if (board != null) {
            return BoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .files(FileDto.of(board.getFileDTOWrapper().getActiveFiles()))
                    .readCount(board.getReadCount())
                    .regUser(board.getCreateUser().getUsername())
                    .regDate(board.getCreatedDate())
                    .build();
        }
        return BoardResponse.builder().build();
    }

    public static List<BoardResponse> ofList(List<Board> boardList) {
        if(boardList == null || boardList.isEmpty()) {
            return List.of();
        }
        return boardList.stream().map(BoardResponse::of).toList();
    }

}
