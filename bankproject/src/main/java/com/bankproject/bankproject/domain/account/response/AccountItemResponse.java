package com.bankproject.bankproject.domain.account.response;

import java.util.List;

import com.bankproject.bankproject.domain.account.dto.AccountItemDetail;
import com.bankproject.bankproject.domain.account.entity.AccountItem;
import com.bankproject.bankproject.global.dto.file.FileDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountItemResponse {

    private Long id;

    private String itemCode; // 계좌 항목 코드 (조회시 사용)

    private String itemName; // 계좌 항목 이름

    private String itemDesc; // 계좌 항목 설명

    private List<FileDto> files; // 계좌 항목 파일

    private AccountItemDetail detail; // 계좌 항목 상세 설명

    public static AccountItemResponse of(AccountItem accountItem) {
        AccountItemResponse response = AccountItemResponse.builder()
                .id(accountItem.getId())
                .itemCode(accountItem.getItemCode())
                .itemName(accountItem.getItemName())
                .itemDesc(accountItem.getItemDesc())
                .files(accountItem.getFiles())
                .detail(accountItem.getDetail())
                .build();
        return response;
    }

    public static List<AccountItemResponse> of(List<AccountItem> accountItems) {
        return accountItems.stream()
                .map(AccountItemResponse::of)
                .toList();
    }
}
