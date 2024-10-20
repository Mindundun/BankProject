package com.bankproject.bankproject.domain.board.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.global.dto.FileDTO;
import com.bankproject.bankproject.global.dto.FileResponseDTO;
import com.querydsl.core.Tuple;

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
    private List<FileResponseDTO> files;
    private LocalDateTime createdAt;
    private Integer readCount;
    private String username;

    public static BoardResponse of(Board board) {
        if (board != null) {
            return BoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .files(FileDTO.of(board.getFiles()))
                    .createdAt(board.getCreatedDate())
                    .readCount(board.getReadCount())
                    .build();
        }
        return BoardResponse.builder().build();
    }

    public static BoardResponse of(Board board, UserEntity user) {
        if (board != null && user != null) {
            return BoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .files(FileDTO.of(board.getFiles()))
                    .createdAt(board.getCreatedDate())
                    .readCount(board.getReadCount())
                    .username(user.getUsername())
                    .build();
        }
        return BoardResponse.builder().build();
    }

    public static List<BoardResponse> ofList(List<Board> boardList) {
        return boardList.stream().map(BoardResponse::of).toList();
    }

    public static List<BoardResponse> ofListWithUser(List<Tuple> tuples) {
        return tuples.stream().map(tuple -> {
            Board board = tuple.get(0, Board.class);
            UserEntity user = tuple.get(1, UserEntity.class);
            return BoardResponse.of(board, user);
        }).toList();
    }

}
