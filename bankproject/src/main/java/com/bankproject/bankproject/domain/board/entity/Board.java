package com.bankproject.bankproject.domain.board.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.dto.CustomUserDetails;
import com.bankproject.bankproject.entity.UserEntity;
import com.bankproject.bankproject.global.dto.file.FileDtoConverter;
import com.bankproject.bankproject.global.dto.file.FileDtoWrapper;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bank_board")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_type")
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Convert(converter = FileDtoConverter.class)
    @Column(name = "files", columnDefinition = "JSON", nullable = false)
    private FileDtoWrapper fileDTOWrapper;

    @Column(name = "is_pin", columnDefinition = "BOOLEAN default false", nullable = false)
    private Boolean isPin;

    @Column(name = "pin_expire_date")
    private LocalDateTime pinExpireDate;

    @JoinColumn(name = "created_by", updatable = false, nullable = false)
    @ManyToOne(fetch = FetchType.LAZY) // queryDSL에서 사용하기 위해 LAZY로 설정
    private UserEntity createUser;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @JoinColumn(name = "updated_by")
    @ManyToOne(fetch = FetchType.LAZY) // queryDSL에서 사용하기 위해 LAZY로 설정
    private UserEntity updateUser;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updateDate;

    @Column(name = "read_count", columnDefinition = "INT default 0")
    private Integer readCount;

    @Column(name = "is_used", columnDefinition = "BOOLEAN default true")
    private Boolean isUsed;

    @PrePersist
    protected void onCreate() {
        if(this.readCount == null) {
            this.readCount = 0;
        }
        if(this.isUsed == null) {
            this.isUsed = true;
        }
        if(this.isPin == null) {
            this.isPin = false;
        }
        this.createUser = getCurrentUser();
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateUser = getCurrentUser();
        this.updateDate = LocalDateTime.now();
    }

    public void increaseReadCount() {
        this.readCount++;
    }

    public void delete() {
        this.isUsed = false;
    }

    private UserEntity getCurrentUser() {
        try {
            // Spring Security의 인증 정보를 가져옴
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserEntity(); // 현재 사용자 반환
        } catch (Exception e) {
            throw new RuntimeException("로그인 정보를 찾을 수 없습니다.");
        }
    }
}
