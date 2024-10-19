package com.bankproject.bankproject.domain.account.repository;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bankproject.bankproject.domain.account.entity.AccountItem;
import com.bankproject.bankproject.domain.account.request.AccountItemSearchRequest;

public interface AccountItemRepositoryCustom {

    public Page<AccountItem> findAccountItemFor(AccountItemSearchRequest request);

    public List<AccountItem> findAccountItemList(AccountItemSearchRequest request);

    public long countAccountItems(AccountItemSearchRequest request);
}
