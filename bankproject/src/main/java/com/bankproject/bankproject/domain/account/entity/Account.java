package com.bankproject.bankproject.domain.account.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.account.enums.AccountStatus;
import com.bankproject.bankproject.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bank_account")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    // 계좌 소유자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    //계좌 종류 (예: 예금, 적금, 대출 등)
    @ManyToOne
    @JoinColumn(name = "account_item_id")
    private AccountItem accountItem;

    // 계좌번호
    @Column(name = "account_number", unique = true)
    private String accountNumber; // 랜덤랜덤

    // 계좌 잔액
    private Long balance;

    //계좌 상태 (예: 활성, 비활성, 정지)
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus status;

    // 계좌 닉네임 (사용자가 설정한 별칭)
    @Column(name = "account_comment")
    private String accountComment;

    // 계좌 생성일
    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // 마지막 거래일
    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;

    // 금액 출입 가능 여부 (입금, 출금 등)
    @Column(name = "is_transactable")
    private boolean isTransactable;

}
