package com.bankproject.bankproject.domain.board.repository;

import java.util.List;

import com.bankproject.bankproject.domain.board.entity.Board;

public interface BoardRepositoryCustom {
    List<Board> searchByTitle(String title);
}
