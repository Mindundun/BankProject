package com.bankproject.bankproject.domain.board.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.entity.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> searchByTitle(String title) {
        return queryFactory
                .selectFrom(QBoard.board)
                .where(QBoard.board.title.contains(title))
                .fetch();
    }

}
