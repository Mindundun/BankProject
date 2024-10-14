package com.bankproject.bankproject.domain.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bankproject.bankproject.domain.board.dto.FileDTO;
import com.bankproject.bankproject.domain.board.enums.BoardType;
import com.bankproject.bankproject.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "bank_board")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_type")
    @Enumerated(EnumType.STRING)
    private BoardType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "JSON")
    private String files;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        if(this.readCount == null) {
            this.readCount = 0;
        }

        if(this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }

        if(this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if(this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }

    public void increaseReadCount() {
        this.readCount++;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void setFileList(List<FileDTO> fileDTOList) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.files = objectMapper.writeValueAsString(fileDTOList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파일 정보를 JSON으로 변환하는데 실패했습니다.");
        }
    }

    public List<FileDTO> getFileList() {
        if(this.files == null) {
            return new ArrayList<>();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.files, objectMapper.getTypeFactory().constructCollectionType(List.class, FileDTO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("파일 정보를 객체로 변환하는데 실패했습니다.");
        }
    }

}
