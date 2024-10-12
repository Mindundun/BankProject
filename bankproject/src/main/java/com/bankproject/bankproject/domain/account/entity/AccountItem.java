package com.bankproject.bankproject.domain.account.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "account_item")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_item_id")
    private Long id;

    @Column(name = "item_code")
    private String itemCode; // 계좌 항목 코드 (예: 01 : 예금, 02: 적금)

    @Column(name = "item_name")
    private String itemName; // 계좌 항목 이름 (예: ㅁㅁ 예금, ㅁㅁ 적금)

    @Column(name = "interest_rate")
    private Long interestRate; // 이자율 (예금, 적금 계좌에 적용)

    @Column(name = "is_dynamic_interest")
    private boolean isDynamicInterest; // 동적 이자율 적용 여부 (하루 적금 등..)

    @Column(name="min_deposit_amount")
    private Long minDepositAmount; // 최소 입금 금액

    @Column(name="max_deposit_amount")
    private Long maxDepositAmount; // 최대 입금 금액

    @Column(name = "is_used")
    private boolean isUsed; // 계좌 항목 사용 여부

    @Column(name = "created_date")
    private LocalDateTime createdDate; // 계좌 항목 생성일

    @Column(name = "updated_date")
    private LocalDateTime updatedDate; // 계좌 항목 수정일

}
