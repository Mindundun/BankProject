package com.bankproject.bankproject.domain.account.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.account.dto.AccountItemDetail;
import com.bankproject.bankproject.global.dto.FileDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
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

    @Column(name = "item_view_files", columnDefinition = "JSON")
    private String itemfiles; // 계좌 항목 파일

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

    public void setItemFiles(List<FileDTO> fileList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.itemfiles = objectMapper.writeValueAsString(fileList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파일 정보를 JSON으로 변환하는데 실패했습니다.");
        }
    }

    public List<FileDTO> getItemFiles() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.itemfiles, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
        } catch (JsonProcessingException e) {
            log.error("파일 정보를 객체로 변환하는데 실패했습니다. AccountItem id: {}", this.id);
            throw new RuntimeException("파일 정보를 객체로 변환하는데 실패했습니다.");
        }
    }

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

    // 파일 리스트를 JSON으로 변환하여 itemfiles 필드에 저장하는 빌더 메서드
    public static class AccountItemBuilder {
        private ObjectMapper objectMapper = new ObjectMapper();

        // 커스텀 빌더 메서드 추가
        public AccountItemBuilder itemFiles(List<FileDTO> fileList) {
            try {
                this.itemfiles = objectMapper.writeValueAsString(fileList);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("파일 정보를 JSON으로 변환하는데 실패했습니다.");
            }
            return this;
        }
    }

}