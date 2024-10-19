package com.bankproject.bankproject.domain.account.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.account.entity.AccountItem;
import com.bankproject.bankproject.domain.account.entity.QAccountItem;
import com.bankproject.bankproject.domain.account.request.AccountItemSearchRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountItemRepositoryCustomImpl implements AccountItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccountItem> findAccountItemFor(AccountItemSearchRequest request) {
        QAccountItem accountItem = QAccountItem.accountItem;

        // BooleanBuilder로 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있을 경우 조건 추가
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            builder.and(accountItem.itemName.contains(request.getSearch()));
        }

        List<AccountItem> accountItems = queryFactory
                .selectFrom(accountItem)
                .where(builder)
                .orderBy(accountItem.id.desc())
                .offset((request.getPage() - 1) * request.getSize())
                .limit(request.getSize())
                .fetch();

        long totalCount = queryFactory
                .select(accountItem.count())
                .from(accountItem)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(accountItems, PageRequest.of(request.getPage(), request.getSize()), totalCount);
    }

    public List<AccountItem> findAccountItemList(AccountItemSearchRequest request) {
        QAccountItem accountItem = QAccountItem.accountItem;

        // BooleanBuilder로 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있을 경우 조건 추가
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            builder.and(accountItem.itemName.contains(request.getSearch()));
        }

        List<AccountItem> accountItems = queryFactory
                .selectFrom(accountItem)
                .where(builder)
                .orderBy(accountItem.id.desc())
                .offset((request.getPage() - 1) * request.getSize())
                .limit(request.getSize())
                .fetch();
        return accountItems;
    }

    public long countAccountItems(AccountItemSearchRequest request) {
        QAccountItem accountItem = QAccountItem.accountItem;

        // BooleanBuilder로 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있을 경우 조건 추가
        if (request.getSearch() != null && !request.getSearch().isEmpty()) {
            builder.and(accountItem.itemName.contains(request.getSearch()));
        }

        return queryFactory
                .select(accountItem.count())
                .from(accountItem)
                .where(builder)
                .fetchOne();
    }
}
