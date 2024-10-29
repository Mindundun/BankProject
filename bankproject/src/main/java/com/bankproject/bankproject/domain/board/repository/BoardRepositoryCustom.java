package com.bankproject.bankproject.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;

public interface BoardRepositoryCustom {
    List<Board> findBoardWithUser(BoardSearchRequest request, Pageable pageable);
    List<Board> findPinBoardWithUser();
    Long countBoard(BoardSearchRequest request);
    Optional<Board> findByIdWithUser(Long id);
}
