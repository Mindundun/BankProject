package com.bankproject.bankproject.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.board.entity.Board;

@Repository
public interface BoardRepository extends BoardRepositoryCustom, JpaRepository<Board, Long> {

}
