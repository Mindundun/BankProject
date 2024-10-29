package com.bankproject.bankproject.domain.account.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.account.dto.AccountItemDetail;
import com.bankproject.bankproject.global.dto.file.FileDto;
import com.bankproject.bankproject.global.dto.file.FileDtoConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Table(name = "account_item")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class AccountItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_item_id")
    private Long id;

    @Column(name = "item_code")
    private String itemCode; // 계좌 항목 코드 (조회시 사용)

    @Column(name = "item_name")
    private String itemName; // 계좌 항목 이름

    @Column(name = "item_desc")
    private String itemDesc; // 계좌 항목 설명

    @Convert(converter = FileDtoConverter.class)
    @Column(name = "files", columnDefinition = "JSON")
    private List<FileDto> files; // 계좌 항목 파일

    @Column(name = "item_detail", columnDefinition = "JSON")
    private String itemDetail; // 계좌 항목 상세 설명

    // audit
    @Column(name = "is_used", columnDefinition = "TINYINT default 1")
    private Boolean isUsed; // 계좌 항목 사용 여부

    @CreatedDate
    @Column(name = "reg_date")
    private LocalDateTime createdDate; // 계좌 항목 생성일

    @CreatedBy
    @Column(name = "reg_id")
    private String regId; // 계좌 항목 등록자

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate; // 계좌 항목 수정일

    @LastModifiedBy
    @Column(name = "updated_id")
    private String updatedId; // 계좌 항목 수정자

    public AccountItemDetail getDetail() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.itemDetail, AccountItemDetail.class);
        } catch (Exception e) {
            log.error("상세 정보를 객체로 변환하는데 실패했습니다. AccountItem id: {}", this.id);
            return new AccountItemDetail();
            // throw new RuntimeException("상세 정보를 객체로 변환하는데 실패했습니다.");
        }
    }

}