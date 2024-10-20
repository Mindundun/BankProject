package com.bankproject.bankproject.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.entity.QBoard;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.bankproject.bankproject.entity.QUserEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> findBoardWithUser(BoardSearchRequest request) {
        QBoard board = QBoard.board;
        QUserEntity user = QUserEntity.userEntity;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getSearch() != null) {
            builder.and(board.title.contains(request.getSearch()));
        }

        List<Tuple> result = queryFactory
                .select(board, user)
                .from(board)
                .join(user).on(board.createdBy.eq(user.username)).fetchJoin()
                .where(builder)
                .fetch();

        return result;
    }

    @Override
    public List<Board> findBoard(BoardSearchRequest request) {
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getSearch() != null) {
            builder.and(board.title.contains(request.getSearch()));
        }

        List<Board> result = queryFactory
                .select(board)
                .from(board)
                .where(builder)
                .fetch();

        return result;
    }

    @Override
    public Long countBoard(BoardSearchRequest request) {
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getSearch() != null) {
            builder.and(board.title.contains(request.getSearch()));
        }

        Long result = queryFactory
                .select(board.count())
                .from(board)
                .where(builder)
                .fetchOne();

        return result;
    }

    @Override
    public Optional<Tuple> findBoardWithUserById(Long id) {
        QBoard board = QBoard.board;
        QUserEntity user = QUserEntity.userEntity;

        Tuple result = queryFactory
                .select(board, user)
                .from(board)
                .join(user).on(board.createdBy.eq(user.username)).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    

}
