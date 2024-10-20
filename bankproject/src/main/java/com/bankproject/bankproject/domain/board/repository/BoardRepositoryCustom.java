package com.bankproject.bankproject.domain.board.repository;

import java.util.List;
import java.util.Optional;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.querydsl.core.Tuple;

public interface BoardRepositoryCustom {
    List<Tuple> findBoardWithUser(BoardSearchRequest request);
    List<Board> findBoard(BoardSearchRequest request);
    Long countBoard(BoardSearchRequest request);
    Optional<Tuple> findBoardWithUserById(Long id);
}
