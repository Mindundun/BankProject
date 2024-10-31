package com.bankproject.bankproject.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankproject.bankproject.domain.account.entity.AccountItem;

@Repository
public interface AccountItemRepository extends AccountItemRepositoryCustom , JpaRepository<AccountItem, Long> {

}
