package com.bankproject.bankproject.domain.board.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.board.entity.Board;
import com.bankproject.bankproject.domain.board.entity.QBoard;
import com.bankproject.bankproject.domain.board.request.BoardSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findBoardWithUser(BoardSearchRequest request, Pageable pageable) {
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getSearch() != null) {
            builder.and(board.title.contains(request.getSearch()));
        }

        builder.and(
            board.isPin.eq(false) // 핀 아닌 게시글
            .or(board.isPin.eq(true).and(board.pinExpireDate.before(LocalDateTime.now()))) // 핀 게시글이지만 유효 기간이 지난 경우
        );


        List<Board> result = queryFactory
                .select(board)
                .from(board)
                .join(board.createUser).fetchJoin()
                .where(builder)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return result;
    }

    @Override
    public List<Board> findPinBoardWithUser() {
        QBoard board = QBoard.board;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(board.isPin.eq(true));
        LocalDateTime now = LocalDateTime.now();
        builder.and(board.pinExpireDate.after(now));

        List<Board> result = queryFactory
                .select(board)
                .from(board)
                .join(board.createUser).fetchJoin()
                .where(builder)
                .orderBy(board.id.desc())
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
    public Optional<Board> findByIdWithUser(Long id) {
        QBoard board = QBoard.board;

        Board result = queryFactory
                .select(board)
                .from(board)
                .join(board.createUser).fetchJoin()
                .where(board.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    

}
